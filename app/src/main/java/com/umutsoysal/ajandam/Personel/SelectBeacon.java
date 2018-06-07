package com.umutsoysal.ajandam.Personel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.irozon.sneaker.Sneaker;
import com.umutsoysal.ajandam.Adapter.BeaconInfoAdapter;
import com.umutsoysal.ajandam.Beacon.Beaconinfo;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class SelectBeacon extends Activity
{

    FloatingActionButton scanButton;
    ListView beaconList;
    ImageButton back;
    String[] BeaconName;
    String[] DeviceMAc;
    String[] BeaconUid;
    String[] Major;
    String[] Minor;
    BeaconInfoAdapter adapter;
    public static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private BluetoothAdapter BTAdapter;
    public static int REQUEST_BLUETOOTH = 1;
    public static Boolean clickTrue = true;
    public static HashSet<Beaconinfo> beaconInfoList = new HashSet<>();
    Beaconinfo beaconinfo;
    ProgressBar scanProgressbar;
    BluetoothClient mClient;
    SearchRequest request;
    public static String academicianID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_beacon);
        back = (ImageButton) findViewById(R.id.back);
        scanButton = (FloatingActionButton) findViewById(R.id.scanButton);
        beaconList = (ListView) findViewById(R.id.BeaconList);
        scanProgressbar = (ProgressBar) findViewById(R.id.progress_2);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                academicianID = null;
            } else {
                academicianID = extras.getString("id");
            }
        } else {
            academicianID = (String) savedInstanceState.getSerializable("id");
            academicianID = Main2Activity.akademisyenID;
        }

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
            }
        });

        mClient = new BluetoothClient(SelectBeacon.this);

        //ble beacon scanner start
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null)
        {
            new AlertDialog.Builder(getApplicationContext())
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

        }
        else
        {
            BeaconSearch();
        }


        if (ContextCompat.checkSelfPermission(SelectBeacon.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(SelectBeacon.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            askForLocationPermissions();
        }
        else
        {
            BeaconSearch();
        }


        //taramayi baslat butonuna basildiginda yapilacak islemler..
        scanButton.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view)
            {


                if (clickTrue)
                {
                    scanButton.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                    clickTrue = false;
                    BeaconSearch();
                    scanProgressbar.setVisibility(View.VISIBLE);

                }
                else
                {
                    scanButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                    clickTrue = true;

                    mClient.stopSearch();
                    scanProgressbar.setVisibility(View.GONE);
                }


            }
        });



        ///listeden herhangi bir beaconi secmesi...
        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                alertDialog(BeaconUid[i]);
            }
        });

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
                    Toast.makeText(SelectBeacon.this, "Can not proceed! i need permission", Toast.LENGTH_SHORT).show();
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

    private void askForLocationPermissions()
    {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(SelectBeacon.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            new android.support.v7.app.AlertDialog.Builder(SelectBeacon.this)
                    .setTitle("Location permessions needed")
                    .setMessage("you need to allow this permission!")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(SelectBeacon.this,
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
            ActivityCompat.requestPermissions(SelectBeacon.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // Device scan callback.
    public void BeaconSearch()
    {
        scanProgressbar.setVisibility(View.VISIBLE);
        beaconInfoList.clear();
        request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)
                .searchBluetoothClassicDevice(5000)
                .searchBluetoothLeDevice(2000)
                .build();

        mClient.search(request, new SearchResponse()
        {
            @Override
            public void onSearchStarted()
            {

            }

            @Override
            public void onDeviceFounded(SearchResult device)
            {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
                String[] temp = beacon.toString().split("@Len");


                if (!device.getName().equals("NULL"))
                {
                    beaconinfo = new Beaconinfo();
                    String u[] = temp[0].split("\n");
                    String d[] = u[0].split(":");
                    beaconinfo.setBeaconUUID(d[1]);
                    beaconinfo.setBeaconName(device.getName());
                    beaconinfo.setDeviceMac(device.getAddress());
                    if (d[1] != null)
                    {
                        beaconinfo.setBeaconMajor(d[1].substring(0, 3));
                        beaconinfo.setBeaconMinor(d[1].substring(2, 5));
                    }

                    beaconInfoList.add(beaconinfo);


                }


            }

            @Override
            public void onSearchStopped()
            {

                BeaconName = new String[beaconInfoList.size()];
                DeviceMAc = new String[beaconInfoList.size()];
                BeaconUid = new String[beaconInfoList.size()];
                Major = new String[beaconInfoList.size()];
                Minor = new String[beaconInfoList.size()];


                int i = 0;
                for (Beaconinfo beaconInfo : beaconInfoList)
                {
                    BeaconName[i] = beaconInfo.getBeaconName();
                    DeviceMAc[i] = beaconInfo.getDeviceMac();
                    BeaconUid[i] = beaconInfo.getBeaconUUID();
                    Major[i] = beaconInfo.getBeaconMajor();
                    Minor[i] = beaconInfo.getBeaconMinor();
                    i++;
                }


                adapter = new BeaconInfoAdapter(getApplicationContext(), BeaconName, DeviceMAc, BeaconUid, Major, Minor);
                beaconList.setAdapter(adapter);

                scanProgressbar.setVisibility(View.GONE);
                scanButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                clickTrue = true;
            }

            @Override
            public void onSearchCanceled()
            {

            }
        });

    }

    private class BeaconUUIDKaydet extends AsyncTask<Object, Void, Object>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
       /*     dialog.dismiss();
            progressDialog = new ProgressDialog(DuyuruGonder.this);
            progressDialog.setMessage("Gönderiliyor..");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected Object doInBackground(Object... params) {

            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://spring-kou-service.herokuapp.com/api/academician/saveUUID";
            //post json using okhttp
            RequestBody formBody = new FormBody.Builder()
                    .add("academicianId", (String)params[0])
                    .add("UUID", (String)params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }

            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            String a = result.toString();

            if (a.equals("true")) {

                        Sneaker.with(SelectBeacon.this)
                                .setTitle("Kayıt Başarılı!!")
                                .setMessage("Beacon uuid sisteme kaydedilmiştir.")
                                .sneakSuccess();

            } else {
                Sneaker.with(SelectBeacon.this)
                        .setTitle("Hata!!")
                        .setMessage("Beklenmedik hata oluştu.Tekrar deneyiniz!")
                        .sneakSuccess();
            }
        }
    }


    public void alertDialog(final String uuid)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectBeacon.this);
        builder.setTitle("Beacon kaydet");
        builder.setMessage("Yeni bir beacon eklemek üzeresiniz! Sistemde kayıtlı olan beaconınız kaldırılıp yerine eklenecektir.");
        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Tamam butonuna basılınca yapılacaklar
                new BeaconUUIDKaydet().execute(academicianID,uuid);

            }
        });


        builder.show();
    }

}
