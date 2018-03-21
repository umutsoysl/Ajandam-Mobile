package com.umutsoysal.ajandam.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.umutsoysal.ajandam.NotificationReceiver.NotificationEventReceiver;
import com.umutsoysal.ajandam.R;
import com.umutsoysal.ajandam.Splashscreen;

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        NotificationManager nm = (NotificationManager) getSystemService( NOTIFICATION_SERVICE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("A J A N D A M")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setContentText("Bu gun 17.00 da dersin var bebegim!")
                .setSmallIcon(R.drawable.logo);


        Intent mainIntent = new Intent(this, Splashscreen.class);
        PendingIntent pendingIntent = android.app.PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
