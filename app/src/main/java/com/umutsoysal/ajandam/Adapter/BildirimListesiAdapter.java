package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.umutsoysal.ajandam.R;

import java.util.Random;

/**
 * Created by MrRobot on 13.12.2017.
 */

public class BildirimListesiAdapter extends BaseAdapter {

    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] baslik;
    String[] icerik;
    String[] tarih;
    String[] gonderen;
    String[] dersAdi;
    LayoutInflater inflater;
    String ay="";
    int[] color;
    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public BildirimListesiAdapter(Context context, String[] baslik,String[] icerik,String[] tarih,String[] dersAdi,String[] gonderen) {
        this.context = context;
        this.baslik=baslik;
        this.icerik=icerik;
        this.tarih=tarih;
        this.dersAdi=dersAdi;
        this.gonderen=gonderen;
    }

    @Override
    public int getCount() {
        return baslik.length;
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
        TextView icerik2,header,dateDay,dateString,kisi,dersadi;
        RelativeLayout circle;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = inflater.inflate(R.layout.bildirim_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        icerik2 = (TextView) itemView.findViewById(R.id.detail);
        header=(TextView)itemView.findViewById(R.id.duyuru_title);
        dateDay=(TextView)itemView.findViewById(R.id.date);
        dateString=(TextView)itemView.findViewById(R.id.date_string);
        kisi=(TextView)itemView.findViewById(R.id.gonderen);
        dersadi=(TextView)itemView.findViewById(R.id.dersAdi);
        circle=(RelativeLayout) itemView.findViewById(R.id.date_frame);

        color=new int[15];
        color[0]=R.color.bir;
        color[1]=R.color.iki;
        color[2]=R.color.uc;
        color[3]=R.color.dort;
        color[4]=R.color.bes;
        color[5]=R.color.altire;
        color[6]=R.color.yedi;
        color[7]=R.color.sekiz;
        color[8]=R.color.dokuz;
        color[9]=R.color.on;
        color[10]=R.color.onbir;
        color[11]=R.color.oniki;
        color[12]=R.color.onuc;
        color[13]=R.color.ondort;
        color[14]=R.color.Grafikbes;

        Random random=new Random();
        int count=random.nextInt(14);


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            circle.setBackgroundTintList(context.getResources().getColorStateList(color[count]));
        }




        if(icerik[position].length()>60)
        {
            icerik2.setText(icerik[position].substring(0,59)+"...");
        }
        else {
            icerik2.setText(icerik[position]);
        }

        String temp=tarih[position].toString();
        String parca[]=temp.split("-");
        if(parca[1].equals("1")||parca[1].equals("01"))
        {
            ay="Ock";
        }
        if(parca[1].equals("2")||parca[1].equals("02"))
        {
            ay="Şbt";
        }
        if(parca[1].equals("3")||parca[1].equals("03"))
        {
            ay="Mrt";
        }
        if(parca[1].equals("4")||parca[1].equals("04"))
        {
            ay="Nis";
        }
        if(parca[1].equals("5")||parca[1].equals("05"))
        {
            ay="May";
        }
        if(parca[1].equals("6")||parca[1].equals("06"))
        {
            ay="Haz";
        }
        if(parca[1].equals("7")||parca[1].equals("07"))
        {
            ay="Tem";
        }
        if(parca[1].equals("8")||parca[1].equals("08"))
        {
            ay="Agu";
        }
        if(parca[1].equals("9")||parca[1].equals("09"))
        {
            ay="Eyl";
        }
        if(parca[1].equals("10"))
        {
            ay="Eki";
        }
        if(parca[1].equals("11"))
        {
            ay="Kas";
        }
        if(parca[1].equals("12"))
        {
            ay="Ara";
        }

        dateString.setText(ay+","+parca[0]);
        dateDay.setText(parca[2]);
        dersadi.setText(dersAdi[position]);
        header.setText(baslik[position]);
        kisi.setText(gonderen[position]);


        return itemView;
    }



}