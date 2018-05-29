package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.Ogrenci.Devamsizlik;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class DersiAlanOgrenciler extends Activity {

    ImageButton back;
    ListView liste;
    DevamsizlikAdapter adapter;
    public static String id="";
    public static String dersADi="";
    private ProgressDialog progressDialog;
    String[] ogrenciBilgisi;
    String[] devam;
    String[] devamsizlik;
    TextView dersim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dersi_alan_ogrenciler);

        back=(ImageButton)findViewById(R.id.back);
        liste=(ListView)findViewById(R.id.derslistesi);
        dersim=(TextView)findViewById(R.id.dersAdi);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id= null;
                dersADi=null;
            } else {
                id= extras.getString("id");
                dersADi=extras.getString("ders");
            }
        } else {
            id= (String) savedInstanceState.getSerializable("id");
            dersADi= (String) savedInstanceState.getSerializable("ders");
        }

        dersim.setText(dersADi);


        Task tsk = new Task();
        tsk.execute();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

    }

    private class Task extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DersiAlanOgrenciler.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();
            String jsonStr=sh.makeServiceCall("http://spring-kou-service.herokuapp.com/api/lesson/rollcall?lessonId="+id);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("ogrenci_devam_bilgileri");

                    // looping through All Contacts
                    ogrenciBilgisi=new String[object.length()];
                    devam=new String[object.length()];
                    devamsizlik=new String[object.length()];

                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        JSONObject ogrenci = c.getJSONObject("ogrenci");
                        String adi = ogrenci.getString("name");
                        String soyadi = ogrenci.getString("surname");
                        String numarasi= ogrenci.getString("number");
                        ogrenciBilgisi[i]="<b>"+adi+" "+soyadi+"</b><br> "+numarasi;

                        JSONObject yokla = c.getJSONObject("devamsizlik");
                        devam[i] = yokla.getString("devamBilgisi");
                        devamsizlik[i] = yokla.getString("devamsizlikBilgisi");


                    }

                    adapter=new DevamsizlikAdapter(DersiAlanOgrenciler.this,ogrenciBilgisi,devam,devamsizlik);

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.warning(getApplicationContext(), "Ögrencilerin henüz devam bilgisi girilmemiştir..",
                                    Toast.LENGTH_LONG,true).show();
                            Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                            startActivity(i);
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.warning(getApplicationContext(), "Ögrencilerin henüz devam bilgisi girilmemiştir..",
                                Toast.LENGTH_LONG,true).show();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        startActivity(i);
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
            if(ogrenciBilgisi==null)
            {
                Toasty.warning(getApplicationContext(), "Ögrencilerin henüz devam bilgisi girilmemiştir..",
                        Toast.LENGTH_LONG,true).show();
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);

            }else{

                liste.setAdapter(adapter);
            }


        }

    }
}
