package com.irfan.draft1.Profile;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.irfan.draft1.R;

/**
 * Created by irfan on 12/01/2018.
 */

public class AboutUsDialog extends Dialog {
    public AboutUsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_aboutus);



    }
}
