package com.umutsoysal.ajandam.Ogrenci;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umutsoysal.ajandam.Adapter.OgrenciDersListesiAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.activity.ChatActivity;
import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.droidsonroids.gif.GifTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class DersProgrami extends Fragment
{
    public static String username = "";
    public static String okulnumber = "";
    public static int index = 0;
    public static String jsonStr = "";
    public static String id = "";
    public static String macID = "";
    public static int position = 0;
    ListView derslistesi;
    OgrenciDersListesiAdapter adapter;
    String[] clock;
    String[] day;
    public static String[] name;
    String[] location;
    public static String[] dersiVeren;
    public static String[] dersId;
    ArrayList<Integer> saatFarki = new ArrayList<>();
    ArrayList<Integer> inlastindex = new ArrayList<>();
    String dayOfTheWeek;
    TextView saati, yeri, dersiveren, dersinismi;
    TextView Hsaati, Hyeri, Hdersiveren, Hdersinismi;
    TextView Textviewusername, Textviewnumber;
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;
    private Boolean exit = false;
    private ProgressDialog progressDialog;
    private Calendar calendar;
    private static final String LOG_TAG = "MainActivity";
    private static int scount = 0;
    private static int lcount = 0;
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 5000;
    private boolean isScanning = false;
    public static String BeaconName = "";
    public static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ArrayList<String> subjectLists = new ArrayList<>();
    private FirebaseDatabase dbFire;
    private DatabaseReference dbRef;
    private BluetoothAdapter BTAdapter;
    public static int REQUEST_BLUETOOTH = 1;
    public static Boolean burdayim = false;
    FloatingActionButton scanButton;
    ScanCallback mCallback;
    BluetoothLeScanner btScanner;
    private Handler mHandler;


    public DersProgrami()
    {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.activity_ders_programi, container, false);

        derslistesi = (ListView) item.findViewById(R.id.derslistesi);
        LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater2.inflate(R.layout.listheader, null);
        saati = (TextView) item.findViewById(R.id.saat);
        yeri = (TextView) item.findViewById(R.id.location);
        dersiveren = (TextView) item.findViewById(R.id.bugun_dersiVeren);
        dersinismi = (TextView) item.findViewById(R.id.now_lesson);
        Hsaati = (TextView) listHeader.findViewById(R.id.saat);
        Hyeri = (TextView) listHeader.findViewById(R.id.location);
        Hdersiveren = (TextView) listHeader.findViewById(R.id.bugun_dersiVeren);
        Hdersinismi = (TextView) listHeader.findViewById(R.id.now_lesson);
        scanButton = (FloatingActionButton) item.findViewById(R.id.scanButton);


        scanButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view)
            {



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    scanButton.setImageDrawable(getResources().getDrawable(R.drawable.scananim, getActivity().getTheme()));
                }
                else
                {
                    scanButton.setImageDrawable(getResources().getDrawable(R.drawable.scananim));
                }

                final AnimationDrawable animationDrawable = (AnimationDrawable) scanButton.getDrawable();
                animationDrawable.start();

                btManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
                btAdapter = btManager.getAdapter();

                burdayim = false;
                //scanHandler.post(scanRunnable);

                //ble beacon scanner start
                BTAdapter = BluetoothAdapter.getDefaultAdapter();
                // Phone does not support Bluetooth so let the user know and exit.
                if (BTAdapter == null)
                {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Not compatible")
                            .setMessage("Your phone does not support Bluetooth")
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    System.exit(0);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if (!BTAdapter.isEnabled())
                {
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, REQUEST_BLUETOOTH);

                }else{

                    btScanner = btAdapter.getBluetoothLeScanner();
                    btScanner.startScan(leScanCallback2);
                }

                Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
                Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

                List<String> s = new ArrayList<String>();
                for (BluetoothDevice bt : pairedDevices)
                    s.add(bt.getName());

            }
        });


        dbFire = FirebaseDatabase.getInstance();
        dbRef = dbFire.getReference("ChatSubjects");

        dbRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                subjectLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    subjectLists.add(ds.getKey());
                    Log.d("LOGVALUE", ds.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

                Toast.makeText(getActivity(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        derslistesi.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("subject", name[position - 1]);
                intent.putExtra("username", username + "-" + okulnumber);
                startActivity(intent);
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


        btManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            askForLocationPermissions();
        }
        else
        {
            //do your work
        }


        derslistesi.addHeaderView(listHeader);
        /* Handle list View scroll events */
        derslistesi.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                /* Check if the first item is already reached to top.*/
                if (derslistesi.getFirstVisiblePosition() == 0)
                {
                    View firstChild = derslistesi.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null)
                    {
                        topY = firstChild.getTop();
                    }
                }
            }
        });


        db = new Sqllite(getActivity());
        Bilgiler = db.getOgrenci();
        jsonStr = Bilgiler.get(0).get("json").toString();

        Task tsk = new Task();
        tsk.execute();

        return item;
    }


    private class Task extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray bilgiler = jsonObj.getJSONArray("data");
                    for (int i = 0; i < bilgiler.length(); i++)
                    {
                        JSONObject c = bilgiler.getJSONObject(i);
                        String is = c.getString("name");
                        String soy = c.getString("surname");
                        username = is + " " + soy;
                        okulnumber = c.getString("number");
                        id = c.getString("id");

                    }
                    // Getting JSON Array node
                    JSONObject jsonObj2 = new JSONObject(jsonStr);
                    JSONArray object = jsonObj2.getJSONArray("lessons");

                    // looping through All Contacts
                    name = new String[object.length()];
                    clock = new String[object.length()];
                    day = new String[object.length()];
                    location = new String[object.length()];
                    dersiVeren = new String[object.length()];
                    dersId = new String[object.length()];
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
                    String tempDay = "";

                    int fark = 0;
                    int i = 0;
                    for (int a = 0; a < object.length(); a++)
                    {
                        JSONObject c = object.getJSONObject(a);
                        if (c.getString("day").equals("Pazartesi"))
                        {
                            name[i] = c.getString("name");
                            day[i] = c.getString("day");
                            dersId[i] = c.getString("id");
                            clock[i] = c.getString("clock");
                            String[] ar = clock[i].split(":");

                            if (day[i].equals(dayOfTheWeek))
                            {
                                int b = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                                saatFarki.add(b);
                                index = i;
                                inlastindex.add(index);
                            }

                            location[i] = c.getString("location");

                            JSONObject phone = c.getJSONObject("academician");
                            String n = phone.getString("name");
                            String s = phone.getString("surname");
                            dersiVeren[i] = n + " " + s;

                            i++;
                        }

                    }

                    for (int a = 0; a < object.length(); a++)
                    {
                        JSONObject c = object.getJSONObject(a);
                        if (c.getString("day").equals("Salı"))
                        {
                            name[i] = c.getString("name");
                            day[i] = c.getString("day");
                            dersId[i] = c.getString("id");
                            clock[i] = c.getString("clock");
                            String[] ar = clock[i].split(":");

                            if (day[i].equals(dayOfTheWeek))
                            {
                                int b = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                                saatFarki.add(b);
                                index = i;
                                inlastindex.add(index);
                            }

                            location[i] = c.getString("location");

                            JSONObject phone = c.getJSONObject("academician");
                            String n = phone.getString("name");
                            String s = phone.getString("surname");
                            dersiVeren[i] = n + " " + s;

                            i++;
                        }

                    }


                    for (int a = 0; a < object.length(); a++)
                    {
                        JSONObject c = object.getJSONObject(a);
                        if (c.getString("day").equals("Çarşamba"))
                        {
                            name[i] = c.getString("name");
                            day[i] = c.getString("day");
                            dersId[i] = c.getString("id");
                            clock[i] = c.getString("clock");
                            String[] ar = clock[i].split(":");

                            if (day[i].equals(dayOfTheWeek))
                            {
                                int b = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                                saatFarki.add(b);
                                index = i;
                                inlastindex.add(index);
                            }

                            location[i] = c.getString("location");

                            JSONObject phone = c.getJSONObject("academician");
                            String n = phone.getString("name");
                            String s = phone.getString("surname");
                            dersiVeren[i] = n + " " + s;

                            i++;
                        }

                    }


                    for (int a = 0; a < object.length(); a++)
                    {
                        JSONObject c = object.getJSONObject(a);
                        if (c.getString("day").equals("Perşembe"))
                        {
                            name[i] = c.getString("name");
                            day[i] = c.getString("day");
                            dersId[i] = c.getString("id");
                            clock[i] = c.getString("clock");
                            String[] ar = clock[i].split(":");

                            if (day[i].equals(dayOfTheWeek))
                            {
                                int b = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                                saatFarki.add(b);
                                index = i;
                                inlastindex.add(index);
                            }

                            location[i] = c.getString("location");

                            JSONObject phone = c.getJSONObject("academician");
                            String n = phone.getString("name");
                            String s = phone.getString("surname");
                            dersiVeren[i] = n + " " + s;

                            i++;
                        }

                    }

                    for (int a = 0; a < object.length(); a++)
                    {
                        JSONObject c = object.getJSONObject(a);
                        if (c.getString("day").equals("Cuma"))
                        {
                            name[i] = c.getString("name");
                            day[i] = c.getString("day");
                            dersId[i] = c.getString("id");
                            clock[i] = c.getString("clock");
                            String[] ar = clock[i].split(":");

                            if (day[i].equals(dayOfTheWeek))
                            {
                                int b = Math.abs(Integer.parseInt(ar[0]) - Integer.parseInt(simdikiSaat));
                                saatFarki.add(b);
                                index = i;
                                inlastindex.add(index);
                            }

                            location[i] = c.getString("location");

                            JSONObject phone = c.getJSONObject("academician");
                            String n = phone.getString("name");
                            String s = phone.getString("surname");
                            dersiVeren[i] = n + " " + s;

                            i++;
                        }

                    }


                    adapter = new OgrenciDersListesiAdapter(getActivity(), clock, day, name, dersiVeren, location);
                    i = 0;
                }
                catch (final JSONException e)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toasty.error(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
            else
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toasty.error(getActivity(),
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

                    if (saatFarki.size() != 0)
                    {
                        int temp = saatFarki.get(0);
                        for (int i = 0; i < saatFarki.size(); i++)
                        {
                            if (saatFarki.get(i) <= temp)
                            {
                                index = inlastindex.get(i);
                            }
                        }

                        String dd[] = clock[index].split(":");
                        saati.setText(clock[index]);
                        dersinismi.setText(name[index]);
                        dersiveren.setText(dersiVeren[index]);
                        yeri.setText(location[index]);

                        Hsaati.setText(clock[index]);
                        Hdersinismi.setText(name[index]);
                        Hdersiveren.setText(dersiVeren[index]);
                        Hyeri.setText(location[index]);
                    }
                    else
                    {
                        Hsaati.setVisibility(View.INVISIBLE);
                        Hdersinismi.setText("Sn." + username);
                        Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                        Hyeri.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    Hsaati.setVisibility(View.INVISIBLE);
                    Hdersinismi.setText("Sn." + username);
                    Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                    Hyeri.setVisibility(View.INVISIBLE);
                }

            }
            else
            {
                Hsaati.setVisibility(View.INVISIBLE);
                Hdersinismi.setText("Sn." + username);
                Hdersiveren.setText("Bugün herhangi bir dersiniz bulunmamaktadır!");
                Hyeri.setVisibility(View.INVISIBLE);
            }

            //   Textviewusername.setText(username);
            // Textviewnumber.setText(okulnumber);
        }


    }


    private void askForLocationPermissions()
    {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setTitle("Location permessions needed")
                    .setMessage("you need to allow this permission!")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
//                                        //Do nothing
                        }
                    })
                    .show();

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        }
        else
        {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                    //Do you work
                }
                else
                {
                    Toast.makeText(getActivity(), "Can not proceed! i need permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static boolean isPermissionGranted(@NonNull String[] grantPermissions, @NonNull int[] grantResults,
                                              @NonNull String permission)
    {
        for (int i = 0; i < grantPermissions.length; i++)
        {
            if (permission.equals(grantPermissions[i]))
            {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    // Device scan callback.
    private ScanCallback leScanCallback2 = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            if(result!=null)
            {
                BeaconName = (result.getDevice().getName());


                if (BeaconName != null && BeaconName.toString().trim().equals("iTAG") || BeaconName.toString().trim().equals("FF:FF:90:01:A8:89"))
                {
                    String uuid = "00002A00-0000-1000-8000-00805F9B34FB";
                    new OkHttpAync().execute(this, "post", id, dersId[index], uuid);
                    btScanner.stopScan(leScanCallback2);
                }
            }
            // auto scroll for text view
        /*    final int scrollAmount = peripheralTextView.getLayout().getLineTop(peripheralTextView.getLineCount()) - peripheralTextView.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                peripheralTextView.scrollTo(0, scrollAmount);*/
        }
    };


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
        {


            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5)
            {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
                { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound)
            {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

                Log.i(LOG_TAG, "UUID: " + uuid + "\\nmajor: " + major + "\\nminor" + minor);

                btAdapter.stopLeScan(leScanCallback);
             //   new OkHttpAync().execute(this, "post", id, dersId[index], uuid);


            }
            else
            {
                btAdapter.stopLeScan(leScanCallback);
                Toasty.error(getActivity(), "İşlem Başarısız!!").show();
            }

        }
    };

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private Runnable scanRunnable = new Runnable()
    {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run()
        {

            if (isScanning)
            {
                if (btAdapter != null)
                {
                    btAdapter.stopLeScan(leScanCallback);
                }
            }
            else
            {
                if (btAdapter != null)
                {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

           // scanHandler.postDelayed(this, scan_interval_ms);

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Object postHttpResponse(String studentId, String lessonId, String uuid)
    {
        btScanner.stopScan(leScanCallback2);
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://spring-kou-service.herokuapp.com/api/rollcall";

        RequestBody formBody = new FormBody.Builder()
                .add("studentId", studentId)
                .add("lessonId", lessonId)
                .add("beaconId", uuid)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = null;
        try
        {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful())
            {
                return response.body().string();
            }

        }
        catch (IOException e)
        {
        }
        return null;

    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object>
    {

        private String TAG = OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Object doInBackground(Object... params)
        {
            String requestType = (String) params[1];
            String studentId = (String) params[2];
            String lessonId = (String) params[3];
            String uuid = (String) params[4];

            Log.e(TAG, "processing http request in async task");

            if ("post".equals(requestType))
            {
                Log.e(TAG, "processing post http request using OkHttp");
                return postHttpResponse(studentId, lessonId, uuid);
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            burdayim = true;
            btAdapter.stopLeScan(leScanCallback);
            progressDialog.dismiss();
            progressDialog.cancel();
            if (result != null && result.toString().equals("true"))
            {
                Toasty.success(getActivity(), "Yoklamaya Kaydedildin.").show();
                scanButton.setImageDrawable(getResources().getDrawable(R.drawable.yuvarlak));

            }
            else
            {
                Toasty.error(getActivity(), "İşlem Başarısız!! Yoklamada varlığınızı kontrol ediniz!").show();

            }



            btScanner.stopScan(leScanCallback2);
            burdayim=true;
          /*  Intent i = new Intent(getActivity(), Devamsizlik.class);
            startActivity(i);
            getActivity().finish();*/

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Destroy");
        btScanner.stopScan(leScanCallback2);
    }



}

