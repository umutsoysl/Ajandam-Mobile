package com.umutsoysal.ajandam.Ogrenci;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.activity.ChatActivity;
import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Devamsizlik extends Fragment
{

    ImageButton back;
    ListView liste;
    DevamsizlikAdapter adapter;
    public static String id;
    private ProgressDialog progressDialog;
    String[] dersAdi;
    String[] devam;
    String[] devamsizlik;
    String[] dersID;
    BarChart mBarChart;

    public Devamsizlik()
    {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.activity_devamsizlik, container, false);
        LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater2.inflate(R.layout.devamszilik_list_header, null);
        mBarChart = (BarChart) listHeader.findViewById(R.id.hesablar);
        liste = (ListView) item.findViewById(R.id.derslistesi);
        liste.addHeaderView(listHeader);
        id = DersProgrami.id;

        if (id != null)
        {
            Task tsk = new Task();
            tsk.execute();
        }

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), CalendarView.class);
                intent.putExtra("ogrenci",DersProgrami.id );
                intent.putExtra("ders",dersID[position-1] );
                intent.putExtra("name",dersAdi[position-1] );
                startActivity(intent);
            }
        });


        return item;

    }

    private class Task extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Lütfen Bekleyiniz..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/rollcall/RollCallInfo?studentId=" + id);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("devam_bilgileri");

                    // looping through All Contacts
                    dersAdi = new String[object.length()];
                    devam = new String[object.length()];
                    devamsizlik = new String[object.length()];
                    dersID=new String[object.length()];

                    for (int i = 0; i < object.length(); i++)
                    {
                        JSONObject c = object.getJSONObject(i);
                        dersAdi[i] = c.getString("dersAdi");
                        devam[i] = c.getString("devamBilgisi");
                        devamsizlik[i] = c.getString("devamsizlikBilgisi");
                        dersID[i] = c.getString("dersId");


                    }

                    adapter = new DevamsizlikAdapter(getActivity(), dersAdi, devam, devamsizlik);

                }
                catch (final JSONException e)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toasty.error(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
            else
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toasty.error(getActivity(),
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

            ArrayList<BarEntry> bargroup2 = new ArrayList<>();
            ArrayList<String> gelir = new ArrayList<String>();
            for(int i=0;i<devam.length;i++){
                bargroup2.add(new BarEntry(Integer.parseInt(devam[i]), i));
                String harf[]=dersAdi[i].split(" ");
                gelir.add(harf[0].substring(0,2)+harf[1].substring(0,2));
            }
// creating dataset for Bar Group1
            BarDataSet barDataSet1 = new BarDataSet(bargroup2, " ");
            mBarChart.setDescription("Devam durumu");


            barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
            barDataSet1.setValueTextSize(13);
            BarData dataa = new BarData(gelir, barDataSet1);
            mBarChart.setData(dataa);

            mBarChart.animateY(2500);


        }

    }

}
