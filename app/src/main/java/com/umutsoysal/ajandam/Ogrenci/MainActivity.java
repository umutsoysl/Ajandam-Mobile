package com.umutsoysal.ajandam.Ogrenci;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.OgrenciDersListesiAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.R;

import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Boolean exit = false;
    ListView derslistesi;
    OgrenciDersListesiAdapter adapter;
    private ProgressDialog progressDialog;
    String[] clock;
    String[] day;
    String[] name;
    String[] location;
    String[] dersiVeren;
    String[] dersId;
    public static String username="";
    public static String okulnumber="";
    public static int index=0;
    private Calendar calendar;
    String dayOfTheWeek;
    TextView saati,yeri,dersiveren,dersinismi;
    public static String jsonStr="";
    TextView Textviewusername,Textviewnumber;
    public static String id="";
    public static String macID="";
    public static int position=0;
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        derslistesi=(ListView)findViewById(R.id.derslistesi);
        saati=(TextView)findViewById(R.id.saat);
        yeri=(TextView)findViewById(R.id.location);
        dersiveren=(TextView)findViewById(R.id.bugun_dersiVeren);
        dersinismi=(TextView)findViewById(R.id.now_lesson);

        getSupportActionBar().setTitle("Bugün");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        Textviewusername=(TextView)header.findViewById(R.id.navusername);
        Textviewnumber=(TextView)header.findViewById(R.id.navnumber);

        db = new Sqllite(MainActivity.this);
        Bilgiler = db.getOgrenci();
        jsonStr= Bilgiler.get(0).get("json").toString();

        Task tsk = new Task();
        tsk.execute();

        derslistesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MainActivity.position=position;
               new GetDevamsizlik().execute();
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);}
        // finish activity
        else {
            Snackbar.make(drawer, " Press again for exit!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Task tsk = new Task();
            tsk.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(getApplicationContext(), Devamsizlik.class);
            i.putExtra("id", MainActivity.id);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(getApplicationContext(), AlarmKur.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {
            Intent i = new Intent(getApplicationContext(), Duyurular.class);
            i.putExtra("id", MainActivity.id);
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_out) {
            db=new Sqllite(getApplicationContext());
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
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray bilgiler = jsonObj.getJSONArray("data");
                    for (int i = 0; i < bilgiler.length(); i++) {
                        JSONObject c = bilgiler.getJSONObject(i);
                        String is = c.getString("name");
                        String soy = c.getString("surname");
                        username= is+" "+soy;
                        okulnumber = c.getString("number");
                        id=c.getString("id");

                    }
                    // Getting JSON Array node
                    JSONObject jsonObj2 = new JSONObject(jsonStr);
                    JSONArray object = jsonObj2.getJSONArray("lessons");

                    // looping through All Contacts
                        name=new String[object.length()];
                        clock=new String[object.length()];
                        day=new String[object.length()];
                        location=new String[object.length()];
                        dersiVeren=new String[object.length()];
                        dersId=new String[object.length()];
                    calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    if (Calendar.MONDAY == dayOfWeek) {
                        dayOfTheWeek ="Pazartesi";
                    } else if (Calendar.TUESDAY == dayOfWeek) {
                        dayOfTheWeek ="Salı";
                    } else if (Calendar.WEDNESDAY == dayOfWeek) {
                        dayOfTheWeek ="Çarşamba";
                    } else if (Calendar.THURSDAY == dayOfWeek) {
                        dayOfTheWeek ="Perşembe";
                    } else if (Calendar.FRIDAY == dayOfWeek) {
                        dayOfTheWeek ="Cuma";
                    }
                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                         name[i] = c.getString("name");
                         day[i] = c.getString("day");
                         dersId[i]=c.getString("id");
                        if(day[i].equals(dayOfTheWeek))
                        {
                            index=i;
                        }
                         clock[i] = c.getString("clock");
                         location[i] = c.getString("location");

                        JSONObject phone = c.getJSONObject("academician");
                        String n = phone.getString("name");
                        String s = phone.getString("surname");
                        dersiVeren[i]=n+" "+s;

                    }

                    adapter=new OgrenciDersListesiAdapter(MainActivity.this,clock,day,name,dersiVeren,location);

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
            if(day.length>0)
            {
                String dd[]=clock[index].split(":");
                saati.setText(dd[0]+"\n"+dd[1]);
                dersinismi.setText(name[index]);
                dersiveren.setText(dersiVeren[index]);
                yeri.setText(location[index]);


            }

            Textviewusername.setText(username);
            Textviewnumber.setText(okulnumber);
        }


    }

    private class GetDevamsizlik extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr=sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/rollcall?lessonId="+dersId[position]+"&studentId="+MainActivity.id);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray object = jsonObj.getJSONArray("devamlilik_sayisi");
                    String a=object.toString();
                    int as=2;
                    // looping through All Contacts
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.error(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            progressDialog.dismiss();
        }
    }


}
