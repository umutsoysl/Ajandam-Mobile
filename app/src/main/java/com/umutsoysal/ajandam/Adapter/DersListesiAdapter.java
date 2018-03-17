package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umutsoysal.ajandam.R;


public class DersListesiAdapter extends BaseAdapter {

    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] lesson;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public DersListesiAdapter(Context context, String[] ders) {
        this.context = context;
        lesson=ders;

    }

    @Override
    public int getCount() {
        return lesson.length;
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
        TextView dersADi;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.ders_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        dersADi = (TextView) itemView.findViewById(R.id.dersAdi);


        dersADi.setText(lesson[position]);

        return itemView;
    }



}