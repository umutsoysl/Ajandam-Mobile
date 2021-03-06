package com.umutsoysal.ajandam.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DersListesiAdapter extends BaseAdapter
{

    // Declare Variables
    Dialog dialog;
    Context context = null;
    String[] lesson;
    String[] clock;
    String[] day;
    String[] location;

    String[] db_lesson;
    String[] db_clock;
    String[] db_day;
    String[] db_location;
    ArrayList<HashMap<String, String>> alarmlar;
    LayoutInflater inflater;
    Sqllite db;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public DersListesiAdapter(Context context, String[] ders, String[] saat, String[] gun, String[] yer)
    {
        this.context = context;
        lesson = ders;
        clock = saat;
        day = gun;
        location = yer;
    }

    @Override
    public int getCount()
    {
        return lesson.length;
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
        TextView dersADi;
        final Switch secim;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.ders_list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        dersADi = (TextView) itemView.findViewById(R.id.dersAdi);
        secim = (Switch) itemView.findViewById(R.id.select_lesson);

        db = new Sqllite(context);
        alarmlar = db.alarmGET();
        int byt = alarmlar.size();

        if(alarmlar.size()>0)
        {
            db_lesson = new String[byt];
            db_clock = new String[byt];
            db_day = new String[byt];
            db_location = new String[byt];

            for (int i = 0; i < alarmlar.size(); i++)
            {
                db_lesson[i]=alarmlar.get(i).get("ders");
                db_day[i]=alarmlar.get(i).get("gun");
                db_location[i]=alarmlar.get(i).get("sinif");
                db_clock[i]=alarmlar.get(i).get("saat");

                if(lesson[position].contains(db_lesson[i])&&day[position].contains(db_day[i]))
                {
                    secim.setOnCheckedChangeListener(null);
                    secim.setChecked(!secim.isChecked());
                }
            }


        }

        secim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                secim.setOnCheckedChangeListener(null);
                secim.setChecked(!secim.isChecked());
                if (secim.isChecked())
                {
                    db.alarmEkle(lesson[position], clock[position], location[position], day[position]);
                }
                else
                {
                    db.AlarmSil(lesson[position]);
                }

            }
        });

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                secim.setOnCheckedChangeListener(null);
                secim.setChecked(!secim.isChecked());
                if (secim.isChecked())
                {
                    db.alarmEkle(lesson[position], clock[position], location[position], day[position]);
                }
                else
                {
                    db.AlarmSil(lesson[position]);
                }

            }
        });

        dersADi.setText(lesson[position]);

        return itemView;
    }


}