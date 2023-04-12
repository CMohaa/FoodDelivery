package com.rova.g3an.Services;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.rova.g3an.R;
import com.rova.g3an.models.Notification;


import java.util.List;

/**
 * Created by Mohamed El Sayed
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private List<Notification> notificationList;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String click_Action = remoteMessage.getNotification().getClickAction();
        String dataFrom = remoteMessage.getData().get("from_id");
        String dataFromName = remoteMessage.getData().get("from_name");
        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        // ========================== Update NotificationFragment ==========================
        /*
        notificationList = UserWiazrd.getInstance().getTempUser().getNotificationList();
        Notification notification = new Notification();
        notification.setType(type);
        notification.setForward(forward);
        notification.setPost(post);
        notification.setFrom(dataFrom);
        notification.setMessage(message);
        notificationList.add(notification);
        UserWiazrd.getInstance().getTempUser().setNotificationList(notificationList);
        */
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_user)
                .setAutoCancel(true)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                .setSound(alarmSound)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent intent = new Intent(click_Action);
        intent.putExtra("user_id",dataFrom);
        intent.putExtra("user_name",dataFromName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        int mNotificationID = (int)System.currentTimeMillis();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(mNotificationID,mBuilder.build());

    }
}