package com.umutsoysal.ajandam.Ogrenci;


import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.LoginPage;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.ViewPagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    public static String username = "";
    private Boolean exit = false;
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    AlarmKur alarmKur;
    Devamsizlik devamsizlik;
    Duyurular duyurular;
    DersProgrami dersProgrami;
    SohbetOdasi sohbetOdasi;
    Profile profile;
    Sqllite db;
    TextView activityName;
    EditText searchView;
    ImageView profile_image;
    ArrayList<HashMap<String, String>> user_info;
    Dialog dialog;
    public static String file_path;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        View view =getSupportActionBar().getCustomView();
        activityName=(TextView)view.findViewById(R.id.activityName);
        searchView=(EditText)view.findViewById(R.id.search_bar);
        profile_image=(ImageView)view.findViewById(R.id.profile_image);

        db=new Sqllite(MainActivity.this);
        user_info=db.getUSERINFO();

        if(user_info.size()>0)
        {
            if(user_info.get(0).get("profile")!=null&&user_info.get(0).get("profile").length()>5)
            {
                file_path=user_info.get(0).get("profile");
                Picasso.with(MainActivity.this).load(user_info.get(0).get("profile")).resize(400,400).into(profile_image);
            }
            else {
                Picasso.with(MainActivity.this).load(R.drawable.avatar).resize(400,400).into(profile_image);
            }
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        db = new Sqllite(MainActivity.this);


        profile_image.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view)
            {
                    more_view_menu();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.action_dersler:
                                viewPager.setCurrentItem(0);
                                activityName.setText("Ders Programı");

                                break;
                            case R.id.action_devamsizlik:
                                viewPager.setCurrentItem(1);
                                activityName.setText("Devamsızlık Bilgileri");

                                break;

                            case R.id.action_sohbet:
                                viewPager.setCurrentItem(3);
                                activityName.setText("Sohbetler");

                                break;
                            case R.id.action_profile:
                                viewPager.setCurrentItem(4);
                                activityName.setText("Profil");

                                break;

                            case R.id.action_duyuru:
                                viewPager.setCurrentItem(2);
                                activityName.setText("Ders Duyuruları");

                                break;

                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (prevMenuItem != null)
                {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                if (position == 0)
                {
                    activityName.setText("Ders Programı");
                    activityName.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                }
                else if (position == 1)
                {
                    activityName.setText("Devamsızlık Bilgileri");
                    activityName.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                }else if (position == 3)
                {
                    activityName.setText("Sohbetler");
                    activityName.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                }
                else if (position == 4)
                {
                    activityName.setText("Profilim");
                    activityName.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                } else if (position == 2)
                {
                    activityName.setText("Ders Duyuruları");
                    activityName.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });


        setupViewPager(viewPager);
    }


    @Override
    public void onResume()
    {
        super.onResume();
        db=new Sqllite(MainActivity.this);
        user_info=db.getUSERINFO();

        if(user_info.size()>0)
        {
            if(user_info.get(0).get("profile")!=null&&user_info.get(0).get("profile").length()>5)
            {
                file_path=user_info.get(0).get("profile");
                Picasso.with(MainActivity.this).load(user_info.get(0).get("profile")).resize(400,400).into(profile_image);
            }
            else {
                Picasso.with(MainActivity.this).load(R.drawable.avatar).resize(400,400).into(profile_image);
            }
        }

    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        dersProgrami = new DersProgrami();
        devamsizlik = new Devamsizlik();
        profile = new Profile();
        sohbetOdasi=new SohbetOdasi();
        duyurular = new Duyurular();
        adapter.addFragment(dersProgrami);
        adapter.addFragment(devamsizlik);
        adapter.addFragment(duyurular);
        adapter.addFragment(sohbetOdasi);
        adapter.addFragment(profile);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed()
    {
        RelativeLayout drawer = (RelativeLayout) findViewById(R.id.drawer_layout);
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);}
        // finish activity
        else {
            Snackbar.make(drawer, " Press again for exit!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

           /* btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            btAdapter = btManager.getAdapter();

            burdayim=false;
            scanHandler.post(scanRunnable);

            //ble beacon scanner start
            BTAdapter = BluetoothAdapter.getDefaultAdapter();
            // Phone does not support Bluetooth so let the user know and exit.
            if (BTAdapter == null)
            {
                new AlertDialog.Builder(this)
                        .setTitle("Not compatible")
                        .setMessage("Your phone does not support Bluetooth")
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                System.exit(0);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            if (!BTAdapter.isEnabled())
            {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, REQUEST_BLUETOOTH);
            }

            Log.d("DEVICELIST", "Super called for DeviceListFragment onCreate\n");
            Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

            List<String> s = new ArrayList<String>();
            for (BluetoothDevice bt : pairedDevices)
                s.add(bt.getName());

            ///end
            return true;
        }*/

            return  true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void more_view_menu() {

        dialog = new Dialog(MainActivity.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_more_menu);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.gri));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.LEFT);
        dialog.setCanceledOnTouchOutside(true);

        final TextView username=(TextView)dialog.findViewById(R.id.username);
        final TextView no=(TextView)dialog.findViewById(R.id.numuro);
        final RelativeLayout profile=(RelativeLayout) dialog.findViewById(R.id.profile);
        final RelativeLayout dersListesi=(RelativeLayout) dialog.findViewById(R.id.dersListesi);
        final RelativeLayout dersAlarm=(RelativeLayout) dialog.findViewById(R.id.dersTakip);
        final RelativeLayout info=(RelativeLayout) dialog.findViewById(R.id.info);
        final RelativeLayout yardim=(RelativeLayout) dialog.findViewById(R.id.help);
        final RelativeLayout enDis=(RelativeLayout) dialog.findViewById(R.id.discerceve);
        final RelativeLayout out=(RelativeLayout) dialog.findViewById(R.id.out);
        final ImageView close=(ImageView)dialog.findViewById(R.id.close);
        final CircleImageView profil=(CircleImageView)dialog.findViewById(R.id.profile_image);

        if(file_path!=null&&file_path.length()>5)
        {
            Picasso.with(MainActivity.this).load(file_path).resize(400,400).into(profil);
        }

        username.setText(DersProgrami.username);
        no.setText(DersProgrami.okulnumber);

        out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Sqllite db = new Sqllite(getApplicationContext());
                db.resetAkademisyen();
                db.resetUSER();
                Intent i = new Intent(MainActivity.this, LoginPage.class);
                startActivity(i);
            }
        });

        enDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(4);
                dialog.dismiss();
            }
        });


        dersListesi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                viewPager.setCurrentItem(0);
                dialog.dismiss();
            }
        });

        dersAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,AlarmKur.class));
            }
        });
        dialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        dialog.show();

    }
}
