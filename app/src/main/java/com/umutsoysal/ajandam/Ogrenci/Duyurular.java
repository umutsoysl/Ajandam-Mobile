package com.umutsoysal.ajandam.Ogrenci;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umutsoysal.ajandam.Adapter.BildirimListesiAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Duyurular extends Fragment
{

    ImageButton back;
    ListView liste;
    private ProgressDialog progressDialog;
    BildirimListesiAdapter adapter;
    String dersAdi[];
    String tarih[];
    String baslik[];
    String icerik[];
    String duyuruSahibi[];
    public static String id;
    Dialog dialog;

    public Duyurular()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.activity_duyurular, container, false);
        liste = (ListView) item.findViewById(R.id.bildirimler);

        id = DersProgrami.id;

        if(id!=null)
        {
            new DuyuruListele().execute();
        }

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                YoYo.with(Techniques.FlipInX)
                        .duration(700)
                        .playOn(view);
                ayrinti_goster(position);

            }
        });
        return item;
    }


    private class DuyuruListele extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Yükleniyor..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall("https://spring-kou-service.herokuapp.com/api/announcement/student?studentId=" + id);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray object = jsonObj.getJSONArray("duyurular");

                    // looping through All Contacts
                    dersAdi = new String[object.length()];
                    tarih = new String[object.length()];
                    baslik = new String[object.length()];
                    icerik = new String[object.length()];
                    duyuruSahibi = new String[object.length()];

                    int a = object.length();
                    for (int i = 0; i < object.length(); i++)
                    {
                        JSONObject c = object.getJSONObject(i);
                        baslik[i] = c.getString("title");
                        icerik[i] = c.getString("content");
                        tarih[i] = c.getString("date");

                        JSONObject akademisyen = c.getJSONObject("academician");
                        String n = akademisyen.getString("name");
                        String s = akademisyen.getString("surname");
                        duyuruSahibi[i] = n + " " + s;

                        JSONObject lesson = c.getJSONObject("lesson");
                        dersAdi[i] = lesson.getString("name");

                    }

                    adapter = new BildirimListesiAdapter(getActivity(), baslik, icerik, tarih, dersAdi, duyuruSahibi);


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

        }

    }


    public void ayrinti_goster(int position)
    {

        dialog = new Dialog(getActivity());
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
        TextView header = (TextView) dialog.findViewById(R.id.baslik);
        TextView metin = (TextView) dialog.findViewById(R.id.icerik);

        header.setText(baslik[position].toString());
        metin.setText(icerik[position].toString());

        kapat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


        dialog.show();

    }


}
