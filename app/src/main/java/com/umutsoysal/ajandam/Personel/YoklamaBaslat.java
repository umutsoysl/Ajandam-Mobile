package com.umutsoysal.ajandam.Personel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.umutsoysal.ajandam.Adapter.YoklamaAdapter;
import com.umutsoysal.ajandam.R;

public class YoklamaBaslat extends Activity {

    EditText sure;
    Button start;
    ListView liste;
    YoklamaAdapter adapter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoklama_baslat);


        sure=(EditText)findViewById(R.id.sure);
        start=(Button)findViewById(R.id.submit);
        liste=(ListView)findViewById(R.id.ogrenciListesi);
        back=(ImageButton)findViewById(R.id.back2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
            }
        });

        final String[] name=new String[]{"İsmail Reşat Akcan","Ramazan Demir","Umut Soysal","Mehmet Karahan","Nurullah Atay","Taha Yüksel","Fatih Kadığil","İlker Aksu","Özge Ulusoy"};
        final String[] no=new String[]{"140202042","140202092","140202040","140202013","140202028","140202088","10202042","130507025","160505050"};

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter=new YoklamaAdapter(getApplicationContext(),name,no);
                liste.setAdapter(adapter);
            }
        });

    }
}
