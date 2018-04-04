package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class AcademicianPassChange extends Activity
{
    EditText password1, password2, mevcut_sifre;
    String[] name;
    String[] password;
    ImageButton back, ok;
    FrameLayout toolbar;
    ArrayList<HashMap<String, String>> user;
    private ProgressDialog progressDialog;
    final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academician_pass_change);

        password1 = (EditText) findViewById(R.id.change_password);
        password2 = (EditText) findViewById(R.id.change_password2);
        mevcut_sifre = (EditText) findViewById(R.id.mevcut_password);
        back = (ImageButton) findViewById(R.id.back_change);
        ok = (ImageButton) findViewById(R.id.ok_pass);
        toolbar = (FrameLayout) findViewById(R.id.frameLayout01);

        mevcut_sifre.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.showSoftInput(mevcut_sifre, InputMethodManager.SHOW_IMPLICIT);


        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AcademicianPassChange.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (password1.getText().toString().trim().equals(password2.getText().toString().trim()) && password1.getText().toString().trim().length() >= 1 && password2.getText().toString().trim().length() >= 1)
                {

                    if(!password[0].equals(mevcut_sifre.getText().toString().trim()))
                    {
                        Toasty.error(getApplicationContext(),"Şifre Yanlış",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {

                      new SifreYenile().execute();
                    }
                }
                else {
                    Toasty.error(getApplicationContext(),"Şifreler eşleşmiyor.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Sqllite db = new Sqllite(getApplicationContext());
        user = db.getUSERINFO();
        if (user.size() != 0)
        {
            password = new String[user.size()];
            name = new String[user.size()];
            for (int i = 0; i < user.size(); i++)
            {
                name[i] = user.get(i).get("username");
                password[i] = user.get(i).get("password");

            }
        }
    }

    public Object postHttpResponse(String requestParam, String pass,String newpass)
    {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://spring-kou-service.herokuapp.com/api/login/changePassword";

        RequestBody formBody = new FormBody.Builder()
                .add("username", requestParam)
                .add("password", pass)
                .add("newPassword", newpass)
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


    private class SifreYenile extends AsyncTask<Object, Void, Object>
    {

        private String TAG = SifreYenile.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AcademicianPassChange.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            String requestType = "post";
            String requestParam = name[0];
            String pass = password[0];
            String newpass =password2.getText().toString().trim();

            Log.e(TAG, "processing http request in async task");

            if ("post".equals(requestType))
            {
                Log.e(TAG, "processing post http request using OkHttp");
                return postHttpResponse(requestParam,pass,newpass);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
            progressDialog.cancel();
            if (result.toString().equals("true"))
            {
                Sqllite db=new Sqllite(getApplicationContext());
                db.resetUSER();
                db.userEkle(name[0],password2.getText().toString().trim());

                Toasty.success(getApplicationContext(), "\n" +
                        "Şifre Başarıyla Yenilendi", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AcademicianPassChange.this, Main2Activity.class);
                startActivity(intent);
            }
            else {
                Toasty.error(getApplicationContext(),"Başarısız!!!",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(AcademicianPassChange.this, Main2Activity.class);
        startActivity(intent);
    }


}
