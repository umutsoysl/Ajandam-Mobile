package com.umutsoysal.ajandam.Ogrenci;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.umutsoysal.ajandam.Adapter.SohbetAdapter;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.activity.ChatActivity;


public class SohbetOdasi extends Fragment
{
   ListView dersListesi;
   SohbetAdapter adapter;
    String[] dersAdi,deriVern,id;

    public SohbetOdasi()
    {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.sohbetler, container, false);
        dersListesi=(ListView)item.findViewById(R.id.derslistesi);

       dersAdi=DersProgrami.name;
       deriVern=DersProgrami.dersiVeren;
       id=DersProgrami.dersId;

        adapter=new SohbetAdapter(getActivity(),dersAdi,deriVern,id);
        dersListesi.setAdapter(adapter);

        dersListesi.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("subject", dersAdi[position]);
                intent.putExtra("username", DersProgrami.username + "-" + DersProgrami.okulnumber);
                startActivity(intent);
            }
        });

        return item;
    }
}