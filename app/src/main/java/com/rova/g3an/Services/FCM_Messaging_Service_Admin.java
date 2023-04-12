package com.rova.g3an.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rova.g3an.R;

public class FCM_Messaging_Service_Admin extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
            if (remoteMessage.getFrom().equals("/topics/" + "promotional_messages")) {
                String click_Action = remoteMessage.getNotification().getClickAction();
                String dataFrom = remoteMessage.getData().get("from_id");
                String dataFromName = remoteMessage.getData().get("from_name");
                String messageTitle = remoteMessage.getNotification().getTitle();
                String messageBody = remoteMessage.getNotification().getBody();
                    displayNotifcation(messageTitle, messageBody,
                            click_Action, dataFrom, dataFromName);
            }
    }

    private void displayNotifcation(String title, String contentText , String click_Action , String dataFrom , String dataFromName
          ){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_cart)
                .setAutoCancel(true)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                .setSound(alarmSound)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(click_Action);
        intent.putExtra("user_id",dataFrom);
        intent.putExtra("user_name",dataFromName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        int mNotificationID = (int)System.currentTimeMillis();
        notificationManager.notify(mNotificationID, mBuilder.build());
    }
}