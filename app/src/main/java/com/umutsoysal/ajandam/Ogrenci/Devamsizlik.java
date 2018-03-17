package com.umutsoysal.ajandam.Ogrenci;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;

import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Devamsizlik extends Activity {

    ImageButton back;
    ListView liste;
    DevamsizlikAdapter adapter;
    public static String id="";
    private ProgressDialog progressDialog;
    String[] dersAdi;
    String[] devam;
    String[] devamsizlik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devamsizlik);

        back=(ImageButton)findViewById(R.id.back);
        liste=(ListView)findViewById(R.id.derslistesi);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id= null;
            } else {
                id= extras.getString("id");
            }
        } else {
            id= (String) savedInstanceState.getSerializable("id");
            id=MainActivity.id;
        }
        Task tsk = new Task();
        tsk.execute();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });




    }

    private class Task extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Devamsizlik.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();
            String jsonStr=sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/rollcall/RollCallInfo?studentId="+id);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("devam_bilgileri");

                    // looping through All Contacts
                    dersAdi=new String[object.length()];
                    devam=new String[object.length()];
                    devamsizlik=new String[object.length()];

                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        dersAdi[i] = c.getString("dersAdi");
                        devam[i] = c.getString("devamBilgisi");
                        devamsizlik[i] = c.getString("devamsizlikBilgisi");


                    }

                    adapter=new DevamsizlikAdapter(Devamsizlik.this,dersAdi,devam,devamsizlik);

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
                                "Bilgiler alınırken beklenmedik hata oldu.Tekrar deneyiniz!",
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
            liste.setAdapter(adapter);

        }

    }

}
