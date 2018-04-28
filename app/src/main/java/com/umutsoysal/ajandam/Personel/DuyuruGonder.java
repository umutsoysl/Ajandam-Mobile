package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umutsoysal.ajandam.Adapter.AkademisyenBildirimAdapter;
import com.umutsoysal.ajandam.Adapter.DersListesiAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;

import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DuyuruGonder extends Activity {

    ImageButton back;
    Button gonder;
    EditText baslik, icerik;
    DersListesiAdapter adapter;
    private ProgressDialog progressDialog;
    public static String id = "";
    public static String jsonStr = "";
    String dersID[];
    Spinner dersListesi;
    String dersAdi[];
    Dialog dialog, dialogEdit;
    String dersAdiListe[];
    String tarihListe[];
    String baslikListe[];
    String icerikListe[];
    String duyuruSahibi[];
    String duyuruID[];
    public static String duyuruDuzenlemeID;
    AkademisyenBildirimAdapter bildirimListesiAdapter;
    public static int Selectindexlesson = 0;
    RelativeLayout add;
    public static int DuyuruSecilenID = 0;
    ImageButton add2;
    public static String BAslik, metin;
    ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyuru_gonder);


        back = (ImageButton) findViewById(R.id.back);
        add = (RelativeLayout) findViewById(R.id.duyuruEkle);
        add2 = (ImageButton) findViewById(R.id.images);
        liste = (ListView) findViewById(R.id.duyurular);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duyuruGonder();
            }
        });

        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duyuruGonder();
            }
        });


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = null;
                jsonStr = null;
            } else {
                id = extras.getString("id");
                jsonStr = extras.getString("jsonStr");
            }
        } else {
            id = (String) savedInstanceState.getSerializable("id");
            id = Main2Activity.akademisyenID;
            jsonStr = Main2Activity.jsonStr;
        }

        new DuyuruListele().execute();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
            }
        });


        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                YoYo.with(Techniques.FlipInX)
                        .duration(700)
                        .playOn(view);
                ayrinti_goster(position);
            }
        });


    }


    private class DuyuruDuzenle extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            final MediaType mediaType
                    = MediaType.parse("application/json");

            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://spring-kou-service.herokuapp.com/api/announcement/update";


            String a = dersID[Selectindexlesson];
            String b = duyuruID[Integer.parseInt(duyuruDuzenlemeID)];
            String jsonStr = "{ \"id\" : \"" + b + "\",\"title\" : \"" + BAslik + "\",\"content\": \"" + metin + "\",\"lesson\" : " +
                    "{ \"id\" : \"" + a + "\"}" +
                    "}";
            jsonStr = jsonStr.replace("\n","\\n");

            //post json using okhttp
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, jsonStr))
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
            dialogEdit.dismiss();
            dialogEdit.cancel();

            if (result == null) {

                Toasty.error(getApplicationContext(), "Malesef beklenmedik hata oluştu!!!", Toast.LENGTH_SHORT, true).show();

            } else {
                String a = result.toString();

                if (a.equals("true")) {

                    Toasty.success(getApplicationContext(),BAslik + " duyurusu güncellenmiştir.",Toast.LENGTH_LONG,true).show();

                    new DuyuruListele().execute();

                } else {
                    Toasty.error(getApplicationContext(), "Beklenmedik hata oluştu",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private class DuyuruyuGonder extends AsyncTask<Object, Void, Object> {
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
            final MediaType mediaType
                    = MediaType.parse("application/json");

            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://spring-kou-service.herokuapp.com/api/announcement";


            int a = Selectindexlesson;

            String jsonStr = "{ \"title\" : \"" + BAslik + "\",\"content\": \"" + metin + "\",\"lesson\" : " +
                    "{ \"id\" : \"" + dersID[Selectindexlesson] + "\"}," + "\"academician\" : " +
                    "{ \"id\" : \"" + id + "\"}" +
                    "}";

            jsonStr = jsonStr.replace("\n","\\n");

            //post json using okhttp
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(mediaType, jsonStr))
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
            dialog.cancel();
            dialog.dismiss();

            String a = result.toString();

            if (a.equals("true")) {

                Toasty.success(getApplicationContext(), BAslik + " duyurusu gönderilmiştir.",
                        Toast.LENGTH_LONG,true).show();
                new DuyuruListele().execute();
            } else {
                Toasty.error(getApplicationContext(), "Beklenmedik hata oluştu",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private class DersListele extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("lessons");

                    // looping through All Contacts
                    dersAdi = new String[object.length()];
                    dersID = new String[object.length()];

                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        dersAdi[i] = c.getString("name");
                        dersID[i] = c.getString("id");
                    }


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
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter adapter2 = new ArrayAdapter(DuyuruGonder.this,
                    android.R.layout.simple_spinner_item, dersAdi);
            dersListesi.setAdapter(adapter2);

        }

    }

    public void duyuruGonder() {

        dialog = new Dialog(DuyuruGonder.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.duyuru_gonder);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialog.setCanceledOnTouchOutside(true);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        View dis = (View) dialog.findViewById(R.id.arkaplan);
        RelativeLayout kapat = (RelativeLayout) dialog.findViewById(R.id.kapat);
        RelativeLayout gonder = (RelativeLayout) dialog.findViewById(R.id.gonder);
        baslik = (EditText) dialog.findViewById(R.id.baslik);
        icerik = (EditText) dialog.findViewById(R.id.icerik);
        dersListesi = (Spinner) dialog.findViewById(R.id.dersler);
        final TextView yazi = (TextView) dialog.findViewById(R.id.yazi);
        final ProgressBar prs = (ProgressBar) dialog.findViewById(R.id.loading);
        prs.setVisibility(View.INVISIBLE);
        yazi.setVisibility(View.VISIBLE);
        new DersListele().execute();
        Selectindexlesson = dersListesi.getSelectedItemPosition() + 1;

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                view.startAnimation(animation1);
                if (baslik.getText().length() > 0 && icerik.getText().length() > 0) {

                    BAslik = baslik.getText().toString();
                    metin = icerik.getText().toString();
                    prs.setVisibility(View.VISIBLE);
                    yazi.setVisibility(View.INVISIBLE);
                    new DuyuruyuGonder().execute();

                } else {
                    prs.setVisibility(View.INVISIBLE);
                    yazi.setVisibility(View.VISIBLE);
                    Toasty.warning(getApplicationContext(), "Lütfen Boşlukları Doldurunuz..",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                v.startAnimation(animation1);
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void duyuruEditDialog(final int pozisyon) {

        dialogEdit = new Dialog(DuyuruGonder.this);
        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setContentView(R.layout.duyuru_gonder);

        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialogEdit.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialogEdit.setCanceledOnTouchOutside(true);

        dialogEdit.setCanceledOnTouchOutside(true);
        dialogEdit.setCancelable(true);

        View dis = (View) dialogEdit.findViewById(R.id.arkaplan);
        RelativeLayout kapat = (RelativeLayout) dialogEdit.findViewById(R.id.kapat);
        RelativeLayout gonder = (RelativeLayout) dialogEdit.findViewById(R.id.gonder);
        TextView bb = (TextView) dialogEdit.findViewById(R.id.dialogdeneemeemsd);
        baslik = (EditText) dialogEdit.findViewById(R.id.baslik);
        icerik = (EditText) dialogEdit.findViewById(R.id.icerik);
        dersListesi = (Spinner) dialogEdit.findViewById(R.id.dersler);
        final TextView yazi = (TextView) dialogEdit.findViewById(R.id.yazi);
        final ProgressBar prs = (ProgressBar) dialogEdit.findViewById(R.id.loading);

        new DersListele().execute();
        prs.setVisibility(View.INVISIBLE);
        yazi.setVisibility(View.VISIBLE);
        bb.setText("Duyuru Düzenle");
        yazi.setText("Düzenle");

        baslik.setText(baslikListe[pozisyon]);
        icerik.setText(icerikListe[pozisyon]);


        Selectindexlesson = dersListesi.getSelectedItemPosition();

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baslik.getText().length() > 0 && icerik.getText().length() > 0) {

                    Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                    animation1.setDuration(900);
                    view.startAnimation(animation1);

                    Selectindexlesson = dersListesi.getSelectedItemPosition();
                    duyuruDuzenlemeID = String.valueOf(pozisyon);
                    BAslik = baslik.getText().toString();
                    metin = icerik.getText().toString();
                    prs.setVisibility(View.VISIBLE);
                    yazi.setVisibility(View.INVISIBLE);
                    new DuyuruDuzenle().execute();

                } else {
                    prs.setVisibility(View.INVISIBLE);
                    yazi.setVisibility(View.VISIBLE);
                    Toasty.warning(getApplicationContext(), "Lütfen Boşlukları Doldurunuz..",
                            Toast.LENGTH_LONG,true).show();

                }
            }
        });

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                v.startAnimation(animation1);
                dialogEdit.dismiss();
            }
        });

        dialogEdit.show();

    }


    private class DuyuruListele extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DuyuruGonder.this);
            progressDialog.setMessage("Yükleniyor..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();

            String aza = id;
            String jsonStr = sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/announcement/academician?academicianId=" + id);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("duyurular");

                    // looping through All Contacts
                    duyuruID = new String[object.length()];
                    dersAdiListe = new String[object.length()];
                    tarihListe = new String[object.length()];
                    baslikListe = new String[object.length()];
                    icerikListe = new String[object.length()];
                    duyuruSahibi = new String[object.length()];
                    int a = object.length();
                    for (int i = 0; i < object.length(); i++) {
                        JSONObject c = object.getJSONObject(i);
                        duyuruID[i] = c.getString("id");
                        baslikListe[i] = c.getString("title");
                        icerikListe[i] = c.getString("content");
                        tarihListe[i] = c.getString("date");

                        JSONObject lesson = c.getJSONObject("lesson");
                        dersAdiListe[i] = lesson.getString("name");
                        duyuruSahibi[i] = "  ";

                    }

                    bildirimListesiAdapter = new AkademisyenBildirimAdapter(getApplicationContext(), baslikListe, icerikListe, tarihListe, dersAdiListe, duyuruSahibi);


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
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
            progressDialog.dismiss();
            liste.setAdapter(bildirimListesiAdapter);

        }

    }

    public void ayrinti_goster(final int pozisyon) {

        dialog = new Dialog(DuyuruGonder.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.duyuru_more_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dialog.setCanceledOnTouchOutside(true);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        View dis = (View) dialog.findViewById(R.id.arkaplan);
        RelativeLayout kapat = (RelativeLayout) dialog.findViewById(R.id.kapat);
        RelativeLayout sil = (RelativeLayout) dialog.findViewById(R.id.sil);
        RelativeLayout edit = (RelativeLayout) dialog.findViewById(R.id.edit);
        TextView header = (TextView) dialog.findViewById(R.id.baslik);
        TextView metin = (TextView) dialog.findViewById(R.id.icerik);
        final ImageView cizgi=(ImageView)dialog.findViewById(R.id.sadas);
        cizgi.setVisibility(View.VISIBLE);
        sil.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                view.startAnimation(animation1);

                dialog.dismiss();
                dialog.cancel();
                duyuruEditDialog(pozisyon);
            }
        });

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                view.startAnimation(animation1);

                dialog.dismiss();
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DuyuruGonder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                // set dialog message
                alertDialogBuilder
                        .setMessage("Silmek istediğine emin misin?")
                        .setCancelable(false)
                        .setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DuyuruSecilenID = pozisyon;
                                new DuyuruSil().execute();

                            }
                        })
                        .setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);


            }
        });

        header.setText(baslikListe[pozisyon].toString());
        metin.setText(icerikListe[pozisyon].toString());

        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(900);
                v.startAnimation(animation1);
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    private class DuyuruSil extends AsyncTask<Object, Void, Object> {

        private String TAG = DuyuruGonder.DuyuruSil.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object... params) {
            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://spring-kou-service.herokuapp.com/api/announcement/delete";

            RequestBody formBody = new FormBody.Builder()
                    .add("announcementId", duyuruID[DuyuruSecilenID])
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = httpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.e(TAG, "Got response from server using OkHttp ");
                    return response.body().string();
                }

            } catch (IOException e) {
                Log.e(TAG, "error in getting response post request okhttp");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result.equals("true")) {
                dialog.dismiss();
                dialog.cancel();
                new DuyuruListele().execute();
                Toasty.success(getApplicationContext(), "Duyuru silme başarılı", Toast.LENGTH_LONG,true).show();
            } else {
                Toasty.error(getApplicationContext(), "SİLİNEMEDİ!!!Beklenmedik hata", Toast.LENGTH_LONG).show();
            }

        }

    }

}
