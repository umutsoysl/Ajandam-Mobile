package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.umutsoysal.ajandam.Adapter.DersListesiAdapter;
import com.umutsoysal.ajandam.Ogrenci.AlarmKur;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.R;

public class Alarm extends Activity {
    ImageButton back;
    ListView liste;
    Button kaydet;
    String []dersler;
    DersListesiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        liste=(ListView)findViewById(R.id.dersler);
        back=(ImageButton)findViewById(R.id.back);
        kaydet=(Button)findViewById(R.id.kaydet);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

        dersler=new String[]{"Bilgisayar Mühendisliğine Giriş","Yazılım Proje Yönetimi","Araştırma Problemleri","Iş Hayatı Iş Guvenliği","Bilgisayar Ağları","Yazılım Mühendisliğine Giriş"};
        adapter=new DersListesiAdapter(Alarm.this,dersler);
        liste.setAdapter(adapter);
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getApplicationContext(), "Bilgiler Kaydedildi.",
                        Toast.LENGTH_LONG).show();

                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

    }
}
