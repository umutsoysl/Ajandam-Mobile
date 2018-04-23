package com.umutsoysal.ajandam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.OgrenciDersListesiAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.Personel.Main2Activity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPage extends Activity
{

    Button giris;
    EditText username;
    EditText password;
    TextView uyariMesaji;
    private ProgressDialog progressDialog;
    final OkHttpClient client = new OkHttpClient();
    public static String govde;
    private String TAG = LoginPage.class.getSimpleName();
    Sqllite db;
    ArrayList<HashMap<String, String>> Bilgiler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);

        //nesne cagirilma olaylari
        username = (EditText) findViewById(R.id.username);
        giris = (Button) findViewById(R.id.submit);
        password = (EditText) findViewById(R.id.password);
        uyariMesaji = (TextView) findViewById(R.id.uyari);

        //giris butonuna basildiğinda..
        giris.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (username.getText().length() > 0 && password.getText().length() > 0)
                {
                    uyariMesaji.setVisibility(View.INVISIBLE);

                    new OkHttpAync().execute(this, "post", username.getText().toString(), password.getText().toString());
                }

                else
                {
                    uyariMesaji.setVisibility(View.VISIBLE);
                    uyariMesaji.setText("Boş alanları doldurunuz!!");
                }
            }
        });

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

        private String TAG = LoginPage.OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginPage.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            String requestType = (String) params[1];
            String requestParam = (String) params[2];
            String pass = (String) params[3];

            Log.e(TAG, "processing http request in async task");

            if ("post".equals(requestType))
            {
                Log.e(TAG, "processing post http request using OkHttp");
                return postHttpResponse(requestParam, pass);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
            progressDialog.cancel();
            if (result.toString().length() > 4)
            {
                db = new Sqllite(LoginPage.this);
                db.userEkle(username.getText().toString(), password.getText().toString(),"");
                uyariMesaji.setVisibility(View.INVISIBLE);
                Log.e(TAG, "populate UI after response from service using OkHttp client");
                govde = result.toString();

                if ((username.getText().toString()).matches("\\d+(?:\\.\\d+)?"))
                {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);

                    Bilgiler = db.getOgrenci();
                    db.resetOgrenci();
                    db.ogrenciEkle(govde);
                    db.close();
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                    Bilgiler = db.getOgrenci();

                    db.resetAkademisyen();
                    db.akademisyenEkle(govde);
                    db.close();
                    startActivity(i);
                }

            }
            else
            {
                uyariMesaji.setVisibility(View.VISIBLE);
                uyariMesaji.setText("Kullanıcı veya şifre yanlış!");
            }

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