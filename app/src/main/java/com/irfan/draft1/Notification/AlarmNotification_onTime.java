package com.irfan.draft1.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.WindowManager;

import com.irfan.draft1.R;

import java.text.SimpleDateFormat;

/**
 * Created by irfan on 11/06/2017.
 */

public class AlarmNotification_onTime extends BroadcastReceiver {

    int randomID = 1 + (int)(Math.random() * 100000);

    @Override
    public void onReceive(Context context, Intent intent) {

        long time2 = intent.getLongExtra("timeRepeatWeek",2);
        String message = intent.getStringExtra("message");
        int notificationID = intent.getIntExtra("notificationID",0);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mma");

        String timeRepeatWeek = sdf.format(time2);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,notificationID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (message.equals(""))
        {
            message = "Don't miss your "+timeRepeatWeek+" bus!";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.mycurtinicon)
                .setContentTitle("MyCurtin")
                .setContentText(message)
        .setSound(soundUri)
        ;



        Notification notification = builder.build();
        //NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat.from(context).notify(randomID,notification);
    }


}
