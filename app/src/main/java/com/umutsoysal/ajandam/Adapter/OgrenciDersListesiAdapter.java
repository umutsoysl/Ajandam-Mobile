package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umutsoysal.ajandam.R;

public class OgrenciDersListesiAdapter extends BaseAdapter
{

    // Declare Variables
    Dialog dialog;
    Context context = null;
    String[] clock;
    String[] day;
    String[] lesson;
    String[] person;
    String[] location;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public OgrenciDersListesiAdapter(Context context, String[] saat, String[] gun, String ders[], String kim[], String[] nerde)
    {
        this.context = context;
        this.clock = saat;
        this.day = gun;
        lesson = ders;
        this.person = kim;
        location = nerde;

    }

    @Override
    public int getCount()
    {
        return clock.length;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {

        // Declare Variables
        TextView gun, saat, ders, dersiVeren, yer;
        RelativeLayout frame,cizgi;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.ogrenci_ders_listesi_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        // oluşan itemviewin içindeki alanları Anasayfadan gelen değerler ile set ediyoruz
        gun = (TextView) itemView.findViewById(R.id.gun);
        saat = (TextView) itemView.findViewById(R.id.saat);
        ders = (TextView) itemView.findViewById(R.id.now_lesson);
        dersiVeren = (TextView) itemView.findViewById(R.id.bugun_dersiVeren);
        yer = (TextView) itemView.findViewById(R.id.yer);
        frame=(RelativeLayout)itemView.findViewById(R.id.frame);
        cizgi=(RelativeLayout)itemView.findViewById(R.id.cizgi);


        if(position-1>=0&&day[position].equals(day[position-1]))
        {
            frame.setVisibility(View.GONE);
           // cizgi.setVisibility(View.VISIBLE);
        }
        else {
            frame.setVisibility(View.VISIBLE);
            //cizgi.setVisibility(View.INVISIBLE);
        }


        if (day[position].equals("Pazartesi"))
        {
            gun.setText("Pzts");
        }
        else
        {
            gun.setText(day[position].substring(0, 4));
        }
        saat.setText(clock[position]);
        ders.setText(lesson[position]);
        dersiVeren.setText(person[position]);
        yer.setText(location[position]);


        return itemView;
    }


}