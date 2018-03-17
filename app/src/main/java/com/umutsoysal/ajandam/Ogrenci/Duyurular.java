package com.umutsoysal.ajandam.Ogrenci;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.BildirimListesiAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;

import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Duyurular extends Activity {

    ImageButton back;
    ListView liste;
    private ProgressDialog progressDialog;
    BildirimListesiAdapter adapter;
    String dersAdi[];
    String tarih[];
    String baslik[];
    String icerik[];
    String duyuruSahibi[];
    public static String id="";
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyurular);

        back=(ImageButton)findViewById(R.id.back);
        liste=(ListView)findViewById(R.id.bildirimler);




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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        new DuyuruListele().execute();

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               ayrinti_goster(position);
            }
        });

    }


    private class DuyuruListele extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Duyurular.this);
            progressDialog.setMessage("Yükleniyor..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();

            String jsonStr=sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/announcement/student?studentId="+id);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("duyurular");

                    // looping through All Contacts
                    dersAdi=new String[object.length()];
                    tarih=new String[object.length()];
                    baslik=new String[object.length()];
                    icerik=new String[object.length()];
                    duyuruSahibi=new String[object.length()];

                    int a=object.length();
                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        baslik[i] = c.getString("title");
                        icerik[i]=c.getString("content");
                        tarih[i]=c.getString("date");

                        JSONObject akademisyen = c.getJSONObject("academician");
                        String n = akademisyen.getString("name");
                        String s = akademisyen.getString("surname");
                        duyuruSahibi[i]=n+" "+s;

                        JSONObject lesson = c.getJSONObject("lesson");
                        dersAdi[i]=lesson.getString("name");

                    }

                    adapter=new BildirimListesiAdapter(getApplicationContext(),baslik,icerik,tarih,dersAdi,duyuruSahibi);


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


    public void ayrinti_goster(int position) {

        dialog = new Dialog(Duyurular.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.duyuru_more_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialog.setCanceledOnTouchOutside(true);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        View dis =(View) dialog.findViewById(R.id.arkaplan);
        RelativeLayout kapat=(RelativeLayout)dialog.findViewById(R.id.kapat);
        TextView header= (TextView) dialog.findViewById(R.id.baslik);
        TextView metin= (TextView) dialog.findViewById(R.id.icerik);

        header.setText(baslik[position].toString());
        metin.setText(icerik[position].toString());

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


}
