package com.irfan.draft1.Schedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.irfan.draft1.Alarms.AlarmFragment;
import com.irfan.draft1.Alarms.PassAlarmActivityListener;
import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.Maps.MapFullscreen;
import com.irfan.draft1.News.NewsTabAdapter;
import com.irfan.draft1.R;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by irfan on 06/11/2017.
 */

public class ScheduleTab extends Fragment {
    //public static final String USER_RINGTONE = "ringtone";
   // private Animation animation;
   // private ViewPager viewPager;
   // private TabLayout tabLayout;
  //  public TabLayout.Tab tab;
  //  private View badge;
  //  private AlarmFragment alarmFragment = new AlarmFragment();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_fragment, container, false);

//        viewPager = view.findViewById(R.id.scheduleViewpager);
//        setNewsTabAdapter(viewPager);
//        tabLayout = view.findViewById(R.id.scheduleTabs);
//        tabLayout.setupWithViewPager(viewPager);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tab = tabLayout.getTabAt(i);
//            Objects.requireNonNull(tab).setCustomView(R.layout.badged_tab);
//            TextView tabTitle= tab.getCustomView().findViewById(android.R.id.text1);
//            tabTitle.setTextColor(ContextCompat.getColorStateList(getActivity(),R.color.tab_text_color_indicator));
//        }
//        badge = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.badgeCotainer);
//
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorBlack));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition()==1)
//                {
//                    animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_bottom);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            badge.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    badge.setAnimation(animation);
//
//
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        ImageView changeAlarmtone = view.findViewById(R.id.change_alarmtone);
//        changeAlarmtone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (Settings.System.canWrite(getActivity())) {
//
//                        openRingtonePicker();
//                    }
//                    else {
//                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//
//                    }
//                }else
//                    {
//                        openRingtonePicker();
//                    }
//
//
//
//            }
//        });

        FloatingActionButton openMap = view.findViewById(R.id.open_maps);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapFullscreen.class);
                startActivity(intent);

            }
        });
        return view;
    }


//    private void openRingtonePicker()
//    {
//        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION | RingtoneManager.TYPE_RINGTONE);
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, readAlarmTone());
//
//        startActivityForResult(intent, 0);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//            RingtoneManager.setActualDefaultRingtoneUri(getActivity(), RingtoneManager.TYPE_RINGTONE, uri);
//            if (uri != null) {
//                String ringTonePath = uri.toString();
//
//               saveAlarmTone(Objects.requireNonNull(getActivity()),USER_RINGTONE,ringTonePath);
//
//}
 //       }
//    }

//    private void setNewsTabAdapter(ViewPager viewPager) {
//        NewsTabAdapter adapter = new NewsTabAdapter(getChildFragmentManager());
//
//        adapter.addFragments(new TimetableTabFragment(), getString(R.string.schedule_tab_1));
//        adapter.addFragments(alarmFragment, getString(R.string.schedule_tab_2));
//        // adapter.addFragments(new NewsFavorites(), getString(R.string.schedule_tab_3));
//        viewPager.setAdapter(adapter);
//    }

//    public void setPagerFragment(int a)
//    {
//        Objects.requireNonNull(tabLayout.getTabAt(a)).select();
//        viewPager.setCurrentItem(a);
//    }

//    public void setParentArguments(final Schedule args) {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    if (args != null) {
//                        alarmFragment.setUIArguments(args, getActivity());
//                        badge.setVisibility(View.VISIBLE);
//                        badge.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
//                    }
//                }
//            });
//        }
//    }

//    public static void saveAlarmTone(Context ctx, String settingName, String settingValue) {
//        SharedPreferences sharedPref = ctx.getSharedPreferences(USER_RINGTONE, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(settingName, settingValue);
//        editor.apply();
//    }
//    public Uri readAlarmTone() {
//        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(ScheduleTab.USER_RINGTONE, MODE_PRIVATE);
//        String alarmString = prefs.getString(ScheduleTab.USER_RINGTONE, null);
//        if (alarmString!=null) {
//            return Uri.parse(alarmString);
//        }
//        return null;
//
//
//    }
}