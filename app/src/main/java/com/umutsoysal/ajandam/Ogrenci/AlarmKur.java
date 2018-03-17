package com.umutsoysal.ajandam.Ogrenci;

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
import com.umutsoysal.ajandam.R;
import es.dmoral.toasty.Toasty;

public class AlarmKur extends Activity {

    ImageButton back;
    ListView liste;
    Button kaydet;
    String []dersler;
    DersListesiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_kur);

        liste=(ListView)findViewById(R.id.dersler);
        back=(ImageButton)findViewById(R.id.back);
        kaydet=(Button)findViewById(R.id.kaydet);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        dersler=new String[]{"Bilgisayar Ağları","Yazılım Proje Yönetimi","Dağıtık Sistemlere Giriş","Nesnelerin Interneti","Web Uygulama Güvenliği","Iş Hayatı Iş Guvenliği","Matematik Uygulamaları","Linux Ağ Yönetimi"};
        adapter=new DersListesiAdapter(AlarmKur.this,dersler);
        liste.setAdapter(adapter);
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toasty.success(getApplicationContext(), "Bilgiler Kaydedildi.",
                        Toast.LENGTH_LONG).show();

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

    }
}
