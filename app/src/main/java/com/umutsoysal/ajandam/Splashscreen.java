package com.umutsoysal.ajandam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.Personel.Main2Activity;

import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Splashscreen extends Activity {

    private ProgressDialog progressDialog;
    final OkHttpClient client = new OkHttpClient();
    public static String govde;
    private String TAG = LoginPage.class.getSimpleName();
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;
    public static String username;
    public static String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


        if(checkInternetConnection()) {
            db = new Sqllite(getApplicationContext());
            Bilgiler = db.getUSERINFO();
            if (Bilgiler.size() > 0) {
                username = Bilgiler.get(0).get("username").toString();
                password = Bilgiler.get(0).get("password").toString();
                new OkHttpAync().execute();
            } else {
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);
            }

        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Splashscreen.this).create();
            alertDialog.setTitle("İnternet Bağlantısı Yok");
            alertDialog.setMessage("Lütfen internet erişimine izin veriniz. !");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Splashscreen.this.finish();
                            System.exit(0);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }

    public Object postHttpResponse(String requestParam, String pass)
    {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://spring-kou-service.herokuapp.com/api/login";

        RequestBody formBody = new FormBody.Builder()
                .add("username", requestParam)
                .add("password", pass)
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
                Log.e(TAG, "Got response from server using OkHttp ");
                return response.body().string();
            }

        }
        catch (IOException e)
        {
            Log.e(TAG, "error in getting response post request okhttp");
        }
        return null;

    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object>
    {

        private String TAG = Splashscreen.OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params)
        {

            Log.e(TAG, "processing http request in async task");

                Log.e(TAG, "processing post http request using OkHttp");
                return postHttpResponse(username,password);

        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);

            if (result.toString().length()>4)
            {
                Log.e(TAG, "populate UI after response from service using OkHttp client");
                govde=result.toString();

                if((username.toString()).matches("\\d+(?:\\.\\d+)?")) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    db = new Sqllite(Splashscreen.this);
                    Bilgiler = db.getOgrenci();
                    if(Bilgiler.size()>0) {
                        db.resetOgrenci();
                    }
                    db.ogrenciEkle(govde);
                    db.close();
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                    db = new Sqllite(Splashscreen.this);
                    Bilgiler = db.getOgrenci();
                    if(Bilgiler.size()>0) {
                        db.resetAkademisyen();
                    }
                    db.akademisyenEkle(govde);
                    db.close();
                    startActivity(i);
                }

            }
            else {

            }

        }

    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
// test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else {
            Log.v("Internet", "Internet Connection Not Present");
            return false;
        }
    }


    @Override
    public void onBackPressed()
    {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);

    }


}
