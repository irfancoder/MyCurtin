package com.irfan.draft1.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.News.NewsLearnMore;
import com.irfan.draft1.R;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Created by irfan on 11/01/2018.
 */

public class EventsNotification extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();

            //you can get your text message here.
            String title = data.get("title");
            String body = data.get("body");
            String url = data.get("URL");
            String logo = data.get("logo");


            sendNotification(removeNulls(title), removeNulls(body),removeNulls(url),removeNulls(logo));
            Log.d("NOTE", "Notification through data payload");
            Log.d("NOTE", "Title: "+title);
            Log.d("NOTE", "Body: "+body);
            Log.d("NOTE", "URL: "+url);
            Log.d("NOTE", "Logo: "+logo);
        }
//        if (remoteMessage.getNotification() != null) {
//            String title = remoteMessage.getNotification().getTitle(); //get title
//            String message = remoteMessage.getNotification().getBody(); //get message
//            String click_action = remoteMessage.getNotification().getClickAction(); //get click_action
//
//            Log.d(TAG, "Message Notification Title: " + title);
//            Log.d(TAG, "Message Notification Body: " + message);
//            Log.d(TAG, "Message Notification click_action: " + click_action);
//            sendNotification(title, message);
//            Log.d("NOTE", "Notification through notification");
//            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */

    public void sendRegistrationToServer(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (!user.isAnonymous()) {
                db.collection("UserDatabase").document(user.getUid()).update("userToken", token).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        }

    }

    public void sendNotification(String title, String body,String url,String logo) {





        Intent intent = new Intent(this, NewsLearnMore.class);
        intent.putExtra("URL", url);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);


        //NotificationManagerCompat notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        String channelId = getString(R.string.default_notification_channel_id);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent.getActivity(this, 0 /* Request code */, intent,PendingIntent.FLAG_ONE_SHOT);

//        int randomID = 1 + (int) (Math.random() * 100000);


//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }



//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(verseurl);
//        bigText.setBigContentTitle("Today's Bible Verse");
//        bigText.setSummaryText("Text in detail");
        FutureTarget<Bitmap> largeIcon = Glide.with(this).asBitmap().load(logo).submit();

        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

            builder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.mycurtinicon)
                    .setLargeIcon(getCircleBitmap(largeIcon.get()))//BitmapFactory.decodeResource(getResources(), R.drawable.mycurtinlogo))
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            ;
            notificationManagerCompat.notify(0 /* ID of notification */, builder.build());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        //
    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private String removeNulls(String input)
    {
        if (input==null)
        {
            return "";
        }
        return input;
    }
}
