package com.umutsoysal.ajandam.Personel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umutsoysal.ajandam.Adapter.OgrenciDersListesiAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    public static int index = 0;
    public static String username = "";
    public static String okulnumber = "";
    public static String jsonStr = "";
    public static String akademisyenID;
    ListView derslistesi;
    OgrenciDersListesiAdapter adapter;
    String[] clock;
    String[] day;
    String[] name;
    String[] location;
    String[] bolum;
    String[] dersID;
    String dayOfTheWeek;
    ArrayList<Integer> saatFarki = new ArrayList<>();
    ArrayList<Integer> inlastindex = new ArrayList<>();
    TextView saati, yeri, dersiveren, hocaninismi;
    TextView Textviewusername, Textviewnumber;
    TextView Hsaati, Hyeri, Hdersiveren, Hdersinismi;
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;
    private Boolean exit = false;
    private ProgressDialog progressDialog;
    private Calendar calendar;
    private static int scount = 0;
    private static int lcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bugün");

        derslistesi = (ListView) findViewById(R.id.derslistesi);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.listheader, null);
        saati = (TextView) findViewById(R.id.saat);
        yeri = (TextView) findViewById(R.id.location);
        dersiveren = (TextView) findViewById(R.id.bugun_dersiVeren);
        hocaninismi = (TextView) findViewById(R.id.isim);
        Hsaati = (TextView) listHeader.findViewById(R.id.saat);
        Hyeri = (TextView) listHeader.findViewById(R.id.location);
        Hdersiveren = (TextView) listHeader.findViewById(R.id.bugun_dersiVeren);
        Hdersinismi = (TextView) listHeader.findViewById(R.id.now_lesson);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Textviewusername = (TextView) header.findViewById(R.id.navusername);
        Textviewnumber = (TextView) header.findViewById(R.id.navnumber);

        derslistesi.addHeaderView(listHeader);
        /* Handle list View scroll events */
        derslistesi.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /* Check if the first item is already reached to top.*/
                if (derslistesi.getFirstVisiblePosition() == 0) {
                    View firstChild = derslistesi.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }
                }
            }
        });

        Hsaati.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (scount % 2 == 0)
                {
                    Hsaati.setText("Saat");
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(Hsaati);
                }
                else
                {
                    Hsaati.setText(clock[index]);
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(Hsaati);

                }
                scount++;
            }
        });

        Hyeri.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (lcount % 2 == 0)
                {
                    Hyeri.setText("Sınıf");
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(Hyeri);

                }
                else
                {
                    Hyeri.setText(location[index]);
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(Hyeri);
                }
                lcount++;
            }
        });


        db = new Sqllite(Main2Activity.this);
        Bilgiler = db.getAkademisyen();
        jsonStr = Bilgiler.get(0).get("json").toString();
        Task tsk = new Task();
        tsk.execute();


        derslistesi.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                Intent i = new Intent(getApplicationContext(), DersiAlanOgrenciler.class);
                i.putExtra("id", dersID[position]);
                i.putExtra("ders", name[position]);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (exit)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        // finish activity
        else
        {
            Snackbar.make(drawer, " Çıkış yapmak için tekrar basın!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            exit = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Task tsk = new Task();
            tsk.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            Intent i = new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(i);
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {
            Intent i = new Intent(getApplicationContext(), YoklamaBaslat.class);
            startActivity(i);
        }
        else if (id == R.id.nav_slideshow)
        {
            Intent i = new Intent(getApplicationContext(), Alarm.class);
            startActivity(i);

        }
        else if (id == R.id.nav_manage)
        {

            Intent i = new Intent(getApplicationContext(), DuyuruGonder.class);
            i.putExtra("id", Main2Activity.akademisyenID);
            i.putExtra("jsonStr", Main2Activity.jsonStr);
            startActivity(i);

        }
        else if (id == R.id.nav_send)
        {
            Intent i = new Intent(getApplicationContext(), AcademicianPassChange.class);
            startActivity(i);
        }
        else if (id == R.id.nav_out)
        {
            db = new Sqllite(getApplicationContext());
            db.resetAkademisyen();
            db.resetUSER();
            Intent i = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Task extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Main2Activity.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray bilgiler = jsonObj.getJSONArray("data");
                    for (int i = 0; i < bilgiler.length(); i++)
                    {
                        JSONObject c = bilgiler.getJSONObject(i);
                        akademisyenID = c.getString("id");
                        String is = c.getString("name");
                        String soy = c.getString("surname");
                        username = is + " " + soy;
                        String[] param = c.getString("department").split("_");
                        okulnumber = param[0] + " " + param[1];

                    }
                    // Getting JSON Array node
                    JSONObject jsonObj2 = new JSONObject(jsonStr);
                    JSONArray object = jsonObj2.getJSONArray("lessons");

                    // looping through All Contacts
                    name = new String[object.length()];
                    clock = new String[object.length()];
                    day = new String[object.length()];
                    location = new String[object.length()];
                    bolum = new String[object.length()];
                    dersID = new String[object.length()];
                    calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    if (Calendar.MONDAY == dayOfWeek)
                    {
                        dayOfTheWeek = "Pazartesi";
                    }
                    else if (Calendar.TUESDAY == dayOfWeek)
                    {
                        dayOfTheWeek = "Salı";
                    }
                    else if (Calendar.WEDNESDAY == dayOfWeek)
                    {
                        dayOfTheWeek = "Çarşamba";
                    }
                    else if (Calendar.THURSDAY == dayOfWeek)
                    {
                        dayOfTheWeek = "Perşembe";
                    }
                    else if (Calendar.FRIDAY == dayOfWeek)
                    {
                        dayOfTheWeek = "Cuma";
                    }

                    String simdikiSaat;
                    SimpleDateFormat formatter24 = new SimpleDateFormat("HH");

                    Date now = Calendar.getInstance().getTime();
                    Calendar cal2 = Calendar.getInstance();
                    now = cal2.getTime();
                    cal2.setTime(now);
                    now = cal2.getTime();
                    // Different formatters for 12 and 24 hour timestamps
                    simdikiSaat = formatter24.format(now);

                    int fark = 0;


                    for (int i = 0; i < object.length(); i++)
                    {
                        JSONObject c = object.getJSONObject(i);
                        name[i] = c.getString("name");
                        day[i] = c.getString("day");
                        clock[i] = c.getString("clock");
                        String[] ar = clock[i].split(":");

                        if (dayOfTheWeek != null && day[i].equals(dayOfTheWeek))
                        {
                            int a = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                            saatFarki.add(a);
                            index = i;
                            inlastindex.add(index);
                        }

                        dersID[i] = c.getString("id");
                        location[i] = c.getString("location");

                        JSONObject department = c.getJSONObject("academician");
                        String[] n = department.getString("department").split("_");
                        bolum[i] = n[0] + " " + n[1];

                    }

                    adapter = new OgrenciDersListesiAdapter(Main2Activity.this, clock, day, name, bolum, location);

                }
                catch (final JSONException e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toasty.error(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
            else
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toasty.error(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(String aVoid)
        {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
            progressDialog.dismiss();
            derslistesi.setAdapter(adapter);
            if (day.length > 0)
            {
                if (dayOfTheWeek != null)
                {
                    if(saatFarki.size()!=0)
                    {
                        int temp = saatFarki.get(0);
                        for (int i = 0; i < saatFarki.size(); i++)
                        {
                            if (saatFarki.get(i) <= temp)
                            {
                                index = inlastindex.get(i);
                            }
                        }


                        Hsaati.setText(clock[index]);
                        Hdersinismi.setText(username);
                        Hdersiveren.setText(name[index]);
                        Hyeri.setText(location[index]);
                    }
                    else{
                        Hsaati.setVisibility(View.INVISIBLE);
                        Hdersinismi.setText(username);
                        Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                        Hyeri.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    Hsaati.setVisibility(View.INVISIBLE);
                    Hdersinismi.setText(username);
                    Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                    Hyeri.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                Hsaati.setVisibility(View.INVISIBLE);
                Hdersinismi.setText(name[index]);
                Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                Hyeri.setVisibility(View.INVISIBLE);
            }

            Textviewusername.setText(username);
            Textviewnumber.setText(okulnumber);
            hocaninismi.setText(username);

        }

    }

}
