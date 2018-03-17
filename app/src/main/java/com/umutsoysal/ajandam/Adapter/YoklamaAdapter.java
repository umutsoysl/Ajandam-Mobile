package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umutsoysal.ajandam.R;

/**
 * Created by MrRobot on 12.12.2017.
 */

public class YoklamaAdapter extends BaseAdapter {

    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] name;
    String[] no;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public YoklamaAdapter(Context context, String[] name,String[] no) {
        this.context = context;
        this.name = name;
        this.no=no;

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
        TextView isim,numara;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.yoklama_liste_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        // oluşan itemviewin içindeki alanları Anasayfadan gelen değerler ile set ediyoruz
        isim = (TextView) itemView.findViewById(R.id.name);
        numara=(TextView)itemView.findViewById(R.id.ogrencino);

        isim.setText(name[position]);
        numara.setText(no[position]);

        return itemView;
    }



}