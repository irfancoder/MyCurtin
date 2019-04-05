package com.irfan.draft1.MainClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.irfan.draft1.Notification.EventsNotification;
import com.irfan.draft1.R;

/**
 * Created by irfan on 04/11/2017.
 */

public class SplashActivity extends AppCompatActivity {

    public static final String PREFERENCES_FILE = "Iman comel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
    }




//    public static boolean readSharedSetting(Context ctx, String settingName, boolean defaultValue) {
//        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
//        return sharedPref.getBoolean(settingName, defaultValue);
//    }
}
