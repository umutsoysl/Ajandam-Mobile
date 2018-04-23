package com.umutsoysal.ajandam.Ogrenci;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.HttpHandler;
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Devamsizlik()
    {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.activity_devamsizlik, container, false);

        liste = (ListView) item.findViewById(R.id.derslistesi);
        id = DersProgrami.id;

        if (id != null)
        {
            Task tsk = new Task();
            tsk.execute();
        }

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

                    for (int i = 0; i < object.length(); i++)
                    {
                        JSONObject c = object.getJSONObject(i);
                        dersAdi[i] = c.getString("dersAdi");
                        devam[i] = c.getString("devamBilgisi");
                        devamsizlik[i] = c.getString("devamsizlikBilgisi");


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

        }

    }

}
