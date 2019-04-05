package com.irfan.draft1.Notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.ScheduleTab;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by irfan on 20/11/2017.
 */
public class AlarmReceiver extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_kring_layout);

        SeekBar stopAlarm = findViewById(R.id.stopAlarm);
        stopAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (i>95)
                {
                    mMediaPlayer.stop();
                    finish();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 95) {
                    seekBar.setThumb(ContextCompat.getDrawable(AlarmReceiver.this,R.drawable.slide_initial_alarm));
                }
            }
        });



//        playSound(this, getAlarmUri());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
//    private Uri getAlarmUri() {


//        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        if (alert == null) {
//            alert = RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            if (alert == null) {
//                alert = RingtoneManager
//                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                if (alert == null) {
//                    alert = RingtoneManager
//                            .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                }
//            }
//        }
//        return readAlarmTone();
//    }

//    public Uri readAlarmTone() {
//        SharedPreferences prefs = getSharedPreferences(ScheduleTab.USER_RINGTONE, MODE_PRIVATE);
//        String alarmString = prefs.getString(ScheduleTab.USER_RINGTONE, null);
//        Uri alarmUri = Uri.parse(alarmString);

//                return alarmUri;

///    }
}