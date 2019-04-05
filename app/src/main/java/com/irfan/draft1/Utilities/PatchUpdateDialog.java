package com.irfan.draft1.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.irfan.draft1.R;

/**
 * Created by irfan on 10/04/2018.
 */

public class PatchUpdateDialog extends Dialog implements View.OnClickListener {

    public static final String PREFS_NAME = "patch_nevershowagain";
    private CheckBox doNotShowAgain;
    private Button okButton;

    public PatchUpdateDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.patch_dialog);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        doNotShowAgain = findViewById(R.id.never_show_checkbox);
        okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok_button) {
            boolean isNeverShowingAgain = doNotShowAgain.isChecked();
            SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("skipMessage", isNeverShowingAgain);
            editor.commit();

            dismiss();
        }
    }


}
