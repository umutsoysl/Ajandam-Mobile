package com.umutsoysal.ajandam.conversation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.adapter.CustomAdapter;
import com.umutsoysal.ajandam.conversation.model.Message;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends Activity
{

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private ArrayList<Message> chatLists = new ArrayList<>();
    private CustomAdapter customAdapter;
    private String subject;
    private String username;
    private ListView listView;
    private ImageView floatingActionButton;
    private EmojiconEditText inputChat;
    ImageButton back;
    TextView dersismi;
    ImageView emojiButton;
    View rootView;
    EmojIconActions emojIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rootView = findViewById(R.id.root_view);
        listView = (ListView)findViewById(R.id.chatListView);
        inputChat = ( EmojiconEditText)findViewById(R.id.inputChat);
        floatingActionButton = (ImageView) findViewById(R.id.fab);
        back=(ImageButton)findViewById(R.id.back);
        dersismi=(TextView) findViewById(R.id.name);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        emojIcon = new EmojIconActions(this, rootView, inputChat, emojiButton);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });


        db = FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            // referansa ulaşıp ilgili sohbetleri getirebilmemiz için gerekli yapı
            subject = bundle.getString("subject");
            username=bundle.getString("username");
            dbRef = db.getReference("ChatSubjects/"+subject+"/mesaj");
            if(subject.length()>22)
            {
                dersismi.setText(subject.substring(0,21)+"...");
            }else{
                dersismi.setText(subject);
            }
        }
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        customAdapter = new CustomAdapter(getApplicationContext(),chatLists,username);
        listView.setAdapter(customAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatLists.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);
                    chatLists.add(message);
                    //Log.d("VALUE",ds.getValue(Message.class).getMesajText());
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputChat.getText().length()>=1){

                    long msTime = System.currentTimeMillis();
                    Date curDateTime = new Date(msTime);
                  //  curDateTime.setHours(curDateTime.getHours() + 12);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(curDateTime);
                    cal2.add(Calendar.HOUR, +12);
                    curDateTime = cal2.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                    String dateTime = formatter.format(curDateTime);
                    Message message = new Message(inputChat.getText().toString(),username,dateTime);
                    dbRef.push().setValue(message);
                    inputChat.setText("");

                }else{

                    Toast.makeText(getApplicationContext(),"Gönderilecek mesaj boş olamaz!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


}
