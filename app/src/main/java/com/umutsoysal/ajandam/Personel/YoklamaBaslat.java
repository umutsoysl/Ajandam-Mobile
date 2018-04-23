package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.umutsoysal.ajandam.Adapter.YoklamaAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.Ogrenci.DersProgrami;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.umutsoysal.ajandam.Ogrenci.DersProgrami.name;
import static com.umutsoysal.ajandam.Personel.YoklamaBaslat.jsonStr;

public class YoklamaBaslat extends Activity
{

    EditText sure;
    Button start;
    ListView liste;
    YoklamaAdapter adapter;
    ImageButton back;
    private ProgressDialog progressDialog,progressDialog2;
    public static String jsonStr="";
    public String[] name;
    public String[] number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoklama_baslat);


        sure = (EditText) findViewById(R.id.sure);
        start = (Button) findViewById(R.id.submit);
        liste = (ListView) findViewById(R.id.ogrenciListesi);
        back = (ImageButton) findViewById(R.id.back2);

        new getStudents().execute(this, "post", Main2Activity.akademisyenID,Main2Activity.dersID[Main2Activity.index] );

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
            }
        });



        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String id=Main2Activity.dersID[Main2Activity.index];
                new OkHttpAync().execute(this, "post", sure.getText().toString(),id , "1554545556445a5564asd5466");


            }
        });

    }


    public Object postHttpResponse(String studentId, String lessonId, String uuid)
    {
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
            progressDialog = new ProgressDialog(YoklamaBaslat.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
            progressDialog.cancel();
            if (result != null && result.toString().equals("true"))
            {

                Toasty.success(YoklamaBaslat.this, "Yoklamaya Kaydedildin.").show();
                sure.setText("");
                sure.clearFocus();
                new getStudents().execute(this, "post", Main2Activity.akademisyenID,Main2Activity.dersID[Main2Activity.index] );
            }
            else
            {
                Toasty.error(YoklamaBaslat.this, "İşlem Başarısız!!").show();
            }
            new Task().execute();

        }

    }

    public Object postStudent(String academicianId, String lessonId)
    {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "http://spring-kou-service.herokuapp.com/api/rollcall/getRollcall";

        RequestBody formBody = new FormBody.Builder()
                .add("academicianId", academicianId)
                .add("lessonId", lessonId)
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

    private class getStudents extends AsyncTask<Object, Void, Object>
    {

        private String TAG = OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(YoklamaBaslat.this);
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... params)
        {
            String requestType = (String) params[1];
            String academicianId = (String) params[2];
            String lessonId = (String) params[3];

            Log.e(TAG, "processing http request in async task");

            if ("post".equals(requestType))
            {
                Log.e(TAG, "processing post http request using OkHttp");
                return postStudent(academicianId, lessonId);
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
            progressDialog.cancel();
            if (result != null && result.toString().length()>5)
            {
               jsonStr=result.toString();
               new Task().execute();
            }
            else
            {
                Toasty.error(YoklamaBaslat.this, "İşlem Başarısız!!").show();
            }


        }

    }


    public class Task extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = null;
                    try
                    {
                        jsonObj = new JSONObject(jsonStr);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                    JSONArray bilgiler = jsonObj.getJSONArray("data");

                    name = new String[bilgiler.length()];
                    number = new String[bilgiler.length()];

                    int a=bilgiler.length();

                    for (int i = 0; i < bilgiler.length(); i++)
                    {
                        JSONObject c2 = bilgiler.getJSONObject(i);
                        JSONObject c = c2.getJSONObject("student");
                        String is = c.getString("name");
                        String soy = c.getString("surname");
                        name[i] = is + " " + soy;
                        number[i] = c.getString("number");

                    }

                    adapter = new YoklamaAdapter(getApplicationContext(), name, number);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


            }
            return null;
        }


        @Override
        protected void onPostExecute(String aVoid)
        {
            super.onPostExecute(aVoid);
            liste.setAdapter(adapter);
        }

    }


}
