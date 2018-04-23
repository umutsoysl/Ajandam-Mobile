package com.umutsoysal.ajandam.conversation.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.Ogrenci.DersProgrami;
import com.umutsoysal.ajandam.Ogrenci.MainActivity;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.adapter.CustomAdapter;
import com.umutsoysal.ajandam.conversation.model.Message;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback
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
    ImageView emojiButton,camera;
    View rootView;
    EmojIconActions emojIcon;
    private final static int REQUEST_SELECT_IMAGE = 100;
    public static String yol="";
    private Uri filePath;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<HashMap<String, String>> user_info;
    public static String pp_username,pp_number,pp_departman,pp_photo;
    Sqllite sqllite;
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
        camera=(ImageView)findViewById(R.id.camera);
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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        sqllite=new Sqllite(ChatActivity.this);
        user_info=sqllite.getUSERINFO();

        if(user_info.size()>0)
        {
            pp_username=user_info.get(0).get("username").toString();
            pp_number= DersProgrami.okulnumber;
            pp_photo=user_info.get(0).get("profile");
        }

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

        customAdapter = new CustomAdapter(ChatActivity.this,chatLists,username);
        listView.setAdapter(customAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  v.startAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.image_click));
                requestPermission();
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
                    Message message = new Message(inputChat.getText().toString(),username,pp_photo,dateTime,yol);
                    dbRef.push().setValue(message);
                    inputChat.setText("");

                }else{

                    Toast.makeText(getApplicationContext(),"Gönderilecek mesaj boş olamaz!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);

        } else {
            startActivityForResult(resimSecimiIntent(), REQUEST_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(resimSecimiIntent(), REQUEST_SELECT_IMAGE);
        }
    }

    public Intent resimSecimiIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGE) {
                Uri imageUri = getPickImageResultUri(data);
                startCropActivity(imageUri);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Calendar now = Calendar.getInstance();
                 yol = result.getUri().toString();
                int saat = now.get(Calendar.HOUR_OF_DAY);
                int dakika = now.get(Calendar.MINUTE);
                final Context context = null;
                if (result.getUri() != null) {


                    uploadImage(Uri.parse(yol));


                } else {
                    Toast.makeText(ChatActivity.this, "Fotoğraf seçilemedi!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void startCropActivity(Uri imgUri) {
        CropImage.activity(imgUri)
                .setAutoZoomEnabled(true)
                .setAspectRatio(400, 400)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            long msTime = System.currentTimeMillis();
                            Date curDateTime = new Date(msTime);
                            //  curDateTime.setHours(curDateTime.getHours() + 12);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(curDateTime);
                            cal2.add(Calendar.HOUR, +12);
                            curDateTime = cal2.getTime();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                            String dateTime = formatter.format(curDateTime);
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Message message = new Message(inputChat.getText().toString(),username,pp_photo,dateTime,downloadUrl.toString());
                            dbRef.push().setValue(message);
                            yol="";
                            inputChat.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ChatActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sqllite=new Sqllite(ChatActivity.this);
        user_info=sqllite.getUSERINFO();

        if(user_info.size()>0)
        {
            pp_username=user_info.get(0).get("username").toString();
            pp_number= DersProgrami.okulnumber;
            pp_photo=user_info.get(0).get("profile");
        }

    }
}
