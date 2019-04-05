package com.irfan.draft1.Schedule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.irfan.draft1.Alarms.AlarmFragment;
import com.irfan.draft1.Alarms.PassAlarmActivityListener;
import com.irfan.draft1.R;

import java.text.SimpleDateFormat;

/**
 * Created by irfan on 04/11/2017.
 */

public class AlarmDialog implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private TextView notificationText, settingsTime, settingsLocation;

    private TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private Schedule alarmReceived;
    private String notificationTemplate = "Get notified ";
    private ImageView dismiss;

    private SeekBar seekBar;
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    private Long notifyTime;
    private int notifyProgress;
    private Button applySettings;
    private PassAlarmActivityListener callback;
    private Dialog dialog;
    private Context context;
    private Notification changeNotification;

    public void showDialog(Context context, Activity activity, Schedule alarm) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.alarm_setting_activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;

        alarmReceived = alarm;
        this.context = context;
//        changeNotification = alarmReceived.getNotification();

        declareAllVariables();

        settingsTime.setText(sdf.format(alarmReceived.getTime()));
        settingsLocation.setText(alarmReceived.getLocation());
//        changeNotification = alarmReceived.getNotification();

        seekBar.setProgress(changeNotification.getProgress());



        try {
            callback = (PassAlarmActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onPassAlarm");
        }


//        applySettings = (Button) dialog.findViewById(R.id.apply_settings);
//        dismiss = (ImageView) dialog.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        applySettings.setOnClickListener(this);
        dialog.show();
    }

    public void notificationTrigger(long minutes, int progress) {

        notifyTime = alarmReceived.getTime().getTime() - minutes;
        changeNotification.setProgress(progress);

    }

    private void declareAllVariables()
    {
//        notificationText = (TextView) dialog.findViewById(R.id.notification_text);
//        settingsTime = (TextView) dialog.findViewById(R.id.settings_time);
//        settingsLocation = (TextView) dialog.findViewById(R.id.settings_location);
        monday = dialog.findViewById(R.id.monday);
        tuesday = dialog.findViewById(R.id.tuesday);
        wednesday = dialog.findViewById(R.id.wednesday);
        thursday = dialog.findViewById(R.id.thursday);
        friday = dialog.findViewById(R.id.friday);
        saturday = dialog.findViewById(R.id.saturday);
        sunday = dialog.findViewById(R.id.sunday);
//        seekBar = (SeekBar) dialog.findViewById(R.id.seekBar);


        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
//
//        toggleDaytoSet(alarmReceived.getNotification().isMonday(),monday);
//        toggleDaytoSet(alarmReceived.getNotification().isTuesday(),tuesday);
//        toggleDaytoSet(alarmReceived.getNotification().isWednesday(),wednesday);
//        toggleDaytoSet(alarmReceived.getNotification().isThursday(),thursday);
//        toggleDaytoSet(alarmReceived.getNotification().isFriday(),friday);
//        toggleDaytoSet(alarmReceived.getNotification().isSaturday(),saturday);
//        toggleDaytoSet(alarmReceived.getNotification().isSunday(),sunday);
//        setNotificationText(alarmReceived.getNotification().getProgress());
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
      setNotificationText(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void setNotificationText(int i)
    {
        if (i == 0) {
            notificationText.setText(notificationTemplate + "30 min before bus arrives");
            notificationTrigger(1800000, i);
        }
        if (i == 1) {
            notificationText.setText(notificationTemplate + "15 min before bus arrives");
            notificationTrigger(900000, i);
        }
        if (i == 2) {
            notificationText.setText(notificationTemplate + "10 min before bus arrives");
            notificationTrigger(600000, i);
        }
        if (i == 3) {
            notificationText.setText(notificationTemplate + "5 min before bus arrives");
            notificationTrigger(300000, i);
        }
        if (i == 4) {
            notificationText.setText(notificationTemplate + "when the bus arrives");
            notificationTrigger(0, i);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private void setApplySettings() {
        if (notifyTime != null) {
            changeNotification.setNotificationTime(notifyTime);

        }
//        alarmReceived.setNotification(changeNotification);
        callback.passAlarmActivity(alarmReceived);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(alarmFragment.PARCEL_ID, alarmReceived);
//        alarmFragment.setUIArguments(bundle);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.apply_settings:
//                setApplySettings();
//                dialog.dismiss();
//                break;
//            case R.id.dismiss:
//                dialog.dismiss();
//                break;
//            case R.id.monday:
//                if (alarmReceived.getNotification().isMonday())
//                {
//                    changeNotification.setMonday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isMonday(),monday);
//                }
//                else
//                {
//                    changeNotification.setMonday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isMonday(),monday);
//                }                break;
//            case R.id.tuesday:
//                if (alarmReceived.getNotification().isTuesday())
//                {
//                    changeNotification.setTuesday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isTuesday(),tuesday);
//                }
//                else
//                {
//                    changeNotification.setTuesday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isTuesday(),tuesday);
//                }                break;
//            case R.id.wednesday:
//                if (alarmReceived.getNotification().isWednesday())
//                {
//                    changeNotification.setWednesday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isWednesday(),wednesday);
//                }
//                else
//                {
//                    changeNotification.setWednesday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isWednesday(),wednesday);
//                }                break;
//            case R.id.thursday:
//                if (alarmReceived.getNotification().isThursday())
//                {
//                    changeNotification.setThursday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isThursday(),thursday);
//                }
//                else
//                {
//                    changeNotification.setThursday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isThursday(),thursday);
//                }                break;
//            case R.id.friday:
//                if (alarmReceived.getNotification().isFriday())
//                {
//                    changeNotification.setFriday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isFriday(),friday);
//                }
//                else
//                {
//                    changeNotification.setFriday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isFriday(),friday);
//                }                break;
//            case R.id.saturday:
//                if (alarmReceived.getNotification().isSaturday())
//                {
//                    changeNotification.setSaturday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isSaturday(),saturday);
//                }
//                else
//                {
//                    changeNotification.setSaturday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isSaturday(),saturday);
//                }
//                break;
//            case R.id.sunday:
//
//
//                if (alarmReceived.getNotification().isSunday())
//                {
//                    changeNotification.setSunday(false);
//                    toggleDaytoSet(alarmReceived.getNotification().isSunday(),sunday);
//                }
//                else
//                {
//                    changeNotification.setSunday(true);
//                    toggleDaytoSet(alarmReceived.getNotification().isSunday(),sunday);
//                }
//
//                break;

        }
    }


    public void toggleDaytoSet(boolean state,TextView day ) {
        if (state) {
            day.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));


        } else {
            day.setTextColor(ContextCompat.getColor(context, R.color.colorDefaultText));
        }


    }

}
