package com.umutsoysal.ajandam.notifications;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.umutsoysal.ajandam.Database.Sqllite;
import com.umutsoysal.ajandam.NotificationReceiver.NotificationEventReceiver;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.Splashscreen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NotificationIntentService extends IntentService
{

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    Sqllite db;
    static Context conText = null;
    String[] lesson;
    String[] clock;
    String[] day;
    String[] location;
    String dayOfTheWeek;
    String simdikiSaat,simdikiSaat2;
    private Calendar calendar;
    ArrayList<HashMap<String, String>> alarmlar;

    public NotificationIntentService()
    {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context)
    {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        conText = context;
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context)
    {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try
        {
            String action = intent.getAction();
            if (ACTION_START.equals(action))
            {
                processStartNotification();
            }
        }
        finally
        {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent)
    {
        // Log something?
    }

    private void processStartNotification()
    {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

       // headsUpNotification("Kontrol Sistemlerine Giriş", "305", "19:45");

        db = new Sqllite(conText);
        alarmlar = db.alarmGET();
        int byt = alarmlar.size();

        calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek)
        {
            dayOfTheWeek = "Pazartesi";
        }
        else if (Calendar.TUESDAY == dayOfWeek)
        {
            dayOfTheWeek = "Salı";
        }
        else if (Calendar.WEDNESDAY == dayOfWeek)
        {
            dayOfTheWeek = "Çarşamba";
        }
        else if (Calendar.THURSDAY == dayOfWeek)
        {
            dayOfTheWeek = "Perşembe";
        }
        else if (Calendar.FRIDAY == dayOfWeek)
        {
            dayOfTheWeek = "Cuma";
        }
        else if (Calendar.SATURDAY == dayOfWeek)
        {
            dayOfTheWeek = "Cumartesi";
        }
        else if (Calendar.SUNDAY == dayOfWeek)
        {
            dayOfTheWeek = "Pazar";
        }


        Date now = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.HOUR, +2);
        now = cal.getTime();
        // Different formatters for 12 and 24 hour timestamps
        SimpleDateFormat formatter24 = new SimpleDateFormat("HH");

        simdikiSaat = formatter24.format(now);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(now);
        cal2.add(Calendar.HOUR, -1);
        now = cal2.getTime();
        // Different formatters for 12 and 24 hour timestamps
        simdikiSaat2 = formatter24.format(now);


        if (alarmlar.size() > 0)
        {

            lesson = new String[byt];
            clock = new String[byt];
            day = new String[byt];
            location = new String[byt];

            for (int i = 0; i < alarmlar.size(); i++)
            {
                lesson[i] = alarmlar.get(i).get("ders");
                day[i] = alarmlar.get(i).get("gun");
                location[i] = alarmlar.get(i).get("sinif");
                clock[i] = alarmlar.get(i).get("saat");
                String arry[] = clock[i].split(":");
                int saat = Integer.parseInt(arry[0]);

                if (alarmlar.get(i).get("gun").equals(dayOfTheWeek) && saat == Integer.parseInt(simdikiSaat))
                {
                    headsUpNotification(lesson[i], location[i], clock[i]);
                }else if (alarmlar.get(i).get("gun").equals(dayOfTheWeek) && saat == Integer.parseInt(simdikiSaat2))
                {
                    headsUpNotification(lesson[i], location[i], clock[i]);
                }
            }


        }


    }

    private void headsUpNotification(String dersAdi, String mekan, String saat) {

        String CHANNEL_ID = "ajandam";
        String CHANNEL_NAME = "ajandam bildirimleri";
        NotificationChannel channel=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.deleteNotificationChannel(CHANNEL_ID);
            channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Ajandam Channel");
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }


        int NOTIFICATION_ID = 1;
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.bildirimlogo)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle("HAYDİ DERSE!")
                        .setContentText(dersAdi + " dersin başlamak üzere hemen hazırlan...")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Merhaba, bugün " + dersAdi + " dersin saat " + saat + " 'de " + mekan + " sınıfında başlayacaktır. Dersine zamanında gitmeyi unutma!"))
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(this, Splashscreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        Intent buttonIntent = new Intent(getBaseContext(), NotificationEventReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());



    }
}
