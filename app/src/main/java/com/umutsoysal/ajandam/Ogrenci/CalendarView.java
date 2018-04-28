package com.umutsoysal.ajandam.Ogrenci;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.Personel.Main2Activity;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarView extends AppCompatActivity
{
    private static final String TAG = "";
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    public static String[] devam;
    public static String dersId;
    public static String ogrenciid;
    public static String dersAdi;
    private ProgressDialog progressDialog;
    public static String jsonStr;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // referansa ulaşıp ilgili sohbetleri getirebilmemiz için gerekli yapı
            ogrenciid = bundle.getString("ogrenci");
            dersId=bundle.getString("ders");
            dersAdi=bundle.getString("name");
        }

        new OkHttpAync().execute(this, "post",dersId ,ogrenciid );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        ((CalendarView) this).getSupportActionBar().setTitle(dersAdi);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }




        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


    }

    /**
     * Save current states of the Caldroid here
     */

    private class OkHttpAync extends AsyncTask<Object, Void, Object>
    {

        private String TAG = OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CalendarView.this);
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
                OkHttpClient httpClient = new OkHttpClient();
                String url = "http://spring-kou-service.herokuapp.com/api/rollcall/LessonRollCall";

                RequestBody formBody = new FormBody.Builder()
                        .add("studentId", pass)
                        .add("lessonId", requestParam)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                Response response = null;
                try
                {
                    response = httpClient.newCall(request).execute();
                     jsonStr=response.body().string();
                    if (response.isSuccessful()&&!jsonStr.contains("false"))
                    {
                        Log.e(TAG, "Got response from server using OkHttp ");


                        if (jsonStr != null)
                        {

                            try
                            {
                                JSONObject jsonObj = new JSONObject(jsonStr);

                                JSONArray object = jsonObj.getJSONArray("data");

                                // looping through All Contacts
                                devam = new String[object.length()];
                                for (int i = 0; i < object.length(); i++)
                                {
                                    JSONObject c = object.getJSONObject(i);
                                    devam[i] = c.getString("date");

                                }

                            }
                            catch (final JSONException e)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toasty.error(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        }
                        else
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toasty.error(getApplicationContext(),
                                            "Bilgiler alınırken beklenmedik hata oldu.Tekrar deneyiniz!",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                }
                catch (IOException e)
                {
                    Log.e(TAG, "error in getting response post request okhttp");
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            progressDialog.cancel();
            progressDialog.dismiss();
            if(!jsonStr.toString().contains("false"))
            {
                for (int i = 0; i < devam.length; i++)
                {
                    String dat[] = devam[i].split("-");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(dat[0]), Integer.parseInt(dat[1]), Integer.parseInt(dat[2]));
                    Date greenDate = cal.getTime();

                    if (caldroidFragment != null)
                    {
                        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
                        ColorDrawable green = new ColorDrawable(Color.GREEN);
                        caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
                        caldroidFragment.setTextColorForDate(R.color.white, greenDate);
                    }
                    // Attach to the activity
                    FragmentTransaction t =getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.calendar1, caldroidFragment);
                    t.commit();
                }
            }else{
                FragmentTransaction t =getSupportFragmentManager().beginTransaction();
                t.replace(R.id.calendar1, caldroidFragment);
                t.commit();
            }

        }

    }




}