package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umutsoysal.ajandam.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MrRobot on 12.12.2017.
 */

public class DevamsizlikAdapter  extends BaseAdapter {

    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] lesson;
    String[] devam;
    String[] devamsizlik;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public DevamsizlikAdapter(Context context, String[] ders,String[] devam,String devamsizlik[]) {
        this.context = context;
        this.devam = devam;
        this.devamsizlik=devamsizlik;
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
        TextView dersADi,devamSAyisi,devamsizligi;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.devamsizlik_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        // oluşan itemviewin içindeki alanları Anasayfadan gelen değerler ile set ediyoruz
        dersADi = (TextView) itemView.findViewById(R.id.now_lesson);
        devamSAyisi=(TextView)itemView.findViewById(R.id.devam);
        devamsizligi=(TextView)itemView.findViewById(R.id.devamsizlik);


        dersADi.setText(Html.fromHtml(lesson[position]));
        devamSAyisi.setText(devam[position]);
        devamsizligi.setText(devamsizlik[position]);


        return itemView;
    }



}