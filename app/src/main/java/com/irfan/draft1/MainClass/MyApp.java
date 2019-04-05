package com.irfan.draft1.MainClass;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.irfan.draft1.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by irfan on 26/07/2017.
 */

public class MyApp extends Application
{
    @Override
    public void onCreate() {

        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/DIN Alternate Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
