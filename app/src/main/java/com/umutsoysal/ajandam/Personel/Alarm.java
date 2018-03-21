package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.DersListesiAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class Alarm extends Activity {
    ImageButton back;
    ListView liste;
    Button kaydet;
    String[] dersler;
    private ProgressDialog progressDialog;
    String[] clock;
    String[] day;
    String[] name;
    String[] location;
    String[] dersId;
    DersListesiAdapter adapter;
    public static String jsonStr = "";
    public static int position = 0;
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;
    Switch tumdersler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        liste = (ListView) findViewById(R.id.dersler);
        back = (ImageButton) findViewById(R.id.back);
        kaydet = (Button) findViewById(R.id.kaydet);
        tumdersler = (Switch) findViewById(R.id.tumdersler);


        db = new Sqllite(Alarm.this);
        Bilgiler = db.getAkademisyen();
        jsonStr = Bilgiler.get(0).get("json").toString();

        Task tsk = new Task();
        tsk.execute();

        tumdersler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tumdersler.isChecked()){
                    for(int i=0;i<name.length;i++){
                        db.alarmEkle(name[i],clock[i],location[i],day[i]);
                    }
                    liste.setVisibility(View.INVISIBLE);
                }else{
                    liste.setVisibility(View.VISIBLE);
                }


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
            }
        });

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toasty.success(getApplicationContext(), "Bilgiler Kaydedildi.",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
            }
        });

    }

    private class Task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Alarm.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();

            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONObject jsonObj2 = new JSONObject(jsonStr);
                    JSONArray object = jsonObj2.getJSONArray("lessons");

                    // looping through All Contacts
                    name = new String[object.length()];
                    clock = new String[object.length()];
                    day = new String[object.length()];
                    location = new String[object.length()];


                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        name[i] = c.getString("name");
                        day[i] = c.getString("day");
                        clock[i] = c.getString("clock");
                        location[i] = c.getString("location");

                    }

                    adapter = new DersListesiAdapter(Alarm.this, name);

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
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
            progressDialog.dismiss();
            liste.setAdapter(adapter);

        }


    }


}
