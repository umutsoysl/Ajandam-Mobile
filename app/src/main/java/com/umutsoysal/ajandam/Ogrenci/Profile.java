package com.umutsoysal.ajandam.Ogrenci;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.umutsoysal.ajandam.Adapter.DevamsizlikAdapter;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.conversation.activity.ChatActivity;
import com.umutsoysal.ajandam.conversation.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class Profile extends Fragment
{

    RelativeLayout alarmlar,sifreDegistir,cikis;
    TextView name,no,departman;
    ImageView profile_image;
    private final static int REQUEST_SELECT_IMAGE = 100;
    public static String yol="",password;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static FirebaseDatabase db;
    FirebaseStorage storage;
    Sqllite sql;
    StorageReference storageReference;
    ArrayList<HashMap<String, String>> user_info;
    CircleImageView profil_resmi;
    public Profile()
    {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View item = inflater.inflate(R.layout.fragment_activity_profile, container, false);
        alarmlar=(RelativeLayout)item.findViewById(R.id.dersAlarm);
        sifreDegistir=(RelativeLayout)item.findViewById(R.id.sifreYenile);
        cikis=(RelativeLayout)item.findViewById(R.id.oturumuKapat);
        name=(TextView)item.findViewById(R.id.name);
        no=(TextView)item.findViewById(R.id.no);
        departman=(TextView)item.findViewById(R.id.bolum);
        profile_image=(ImageView)item.findViewById(R.id.select_image);
        profil_resmi=(CircleImageView)item.findViewById(R.id.profile_image);

        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        sql=new Sqllite(getActivity());

        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                requestPermission();
            }
        });



        alarmlar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(),AlarmKur.class));
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        });

        sifreDegistir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(),StudentPassChange.class));
                getActivity(). overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        });

        cikis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

               Sqllite db = new Sqllite(getActivity());
                db.resetAkademisyen();
                db.resetUSER();
                Intent i = new Intent(getActivity(), LoginPage.class);
                startActivity(i);
            }
        });

        return item;
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
        PackageManager packageManager = getActivity().getPackageManager();

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
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_SELECT_IMAGE) {
                Uri imageUri = getPickImageResultUri(data);
                startCropActivity(imageUri);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                yol = result.getUri().toString();
                if (result.getUri() != null) {

                    uploadImage(Uri.parse(yol));
                } else {
                    Toast.makeText(getActivity(), "Fotoğraf seçilemedi!", Toast.LENGTH_LONG).show();
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
                .start(getActivity());
            uploadImage(imgUri);
    }

    private void uploadImage(Uri filePath) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("profile/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            sql.resetUSER();
                            sql.userEkle(name.getText().toString(),password.toString(),downloadUrl.toString());
                            Picasso.with(getActivity()).load(downloadUrl).into(profil_resmi);

                            yol="";
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onResume()
    {
        super.onResume();
        sql=new Sqllite(getActivity());
        user_info=sql.getUSERINFO();

        if(user_info.size()>0)
        {
            name.setText(user_info.get(0).get("username").toString());
            password=user_info.get(0).get("password").toString();
            no.setText(DersProgrami.okulnumber);
            if(user_info.get(0).get("profile")!=null&&user_info.get(0).get("profile").length()>5){
                Picasso.with(getActivity()).load(user_info.get(0).get("profile")).into(profil_resmi);
            }else {
                Picasso.with(getActivity()).load(R.drawable.avatar).into(profil_resmi);
            }
        }

    }

}