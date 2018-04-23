package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umutsoysal.ajandam.R;



public class SohbetAdapter extends BaseAdapter
{

    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] name;
    String[] no;
    String[] dersID;
    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public SohbetAdapter(Context context, String[] baslik,String[] icerik,String[] id) {
        this.context = context;
        this.name = baslik;
        this.no=icerik;
        this.dersID=id;

    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView isim,icerik;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.sohbet_odasi_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        // oluşan itemviewin içindeki alanları Anasayfadan gelen değerler ile set ediyoruz
        isim = (TextView) itemView.findViewById(R.id.sohbetBaslik);
        icerik=(TextView)itemView.findViewById(R.id.icerik);
        TextView nampp = (TextView) itemView.findViewById(R.id.textHarf);
        RelativeLayout frame = (RelativeLayout) itemView.findViewById(R.id.frame);

        isim.setText(name[position]);
        icerik.setText(no[position]);

        String names[] =name[position].split(" ");
        nampp.setText(names[0].substring(0, 1) + names[1].substring(0, 1));

        String color[] = new String[]{"#0095ff", "#ed5d5d", "#9a4bed", "#f38d46", "#5fba7d", "#b7701b", "#dfb956", "#a0514d", "#81c6c1", "#e280d0", "#8bc34a"};
        int bgcolor[] = new int[]{R.color.chat1, R.color.chat2, R.color.chat3, R.color.chat4, R.color.chat5, R.color.chat6, R.color.chat7, R.color.chat8, R.color.chat9, R.color.chat10, R.color.chat11};
        String nu =dersID[position];
        if (nu.equals("0"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[0]));
            }
        }
        else if (nu.equals("1"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[1]));
            }

        }
        else if (nu.equals("2"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[2]));
            }

        }
        else if (nu.equals("3"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[3]));
            }

        }
        else if (nu.equals("4"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[4]));
            }

        }
        else if (nu.equals("5"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[5]));
            }

        }
        else if (nu.equals("6"))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[6]));
            }

        }
        else if (nu.equals("7"))
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[7]));
            }

        }
        else if (nu.equals("8"))
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[8]));
            }

        }
        else if (nu.equals("9"))
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[9]));
            }

        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                frame.setBackgroundTintList(context.getResources().getColorStateList(bgcolor[10]));
            }

        }

        return itemView;
    }



}
