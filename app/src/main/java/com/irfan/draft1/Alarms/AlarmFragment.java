package com.irfan.draft1.Alarms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.Notification.AlarmNotification_onTime;
import com.irfan.draft1.Notification.AlarmReceiver;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.Schedule;
import com.irfan.draft1.Schedule.ScheduleTab;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by irfan on 03/07/2017.
 */

public class AlarmFragment extends Fragment{
    public final static String PARCEL_ID = "irfanishandsome";
    private String alarmFileName = "alarmFile.cb";

    private Context context;
    private RecyclerView recyclerView;
    private int index;
    private int alarmCount;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE MMMM dd, yyyy hh:mm a");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
    private AlarmFragmentAdapter adapter;
    private List<Schedule> alarmList = new ArrayList<>();
    private ItemTouchHelper.Callback itemTouchHelperCallback;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private PassAlarmActivityListener callback;

    private AlarmManager alarmManager;
    private LinearLayout alarm_emptystate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        recyclerView = view.findViewById(R.id.alarm_list);
        alarm_emptystate = view.findViewById(R.id.alarm_emptystate);
        alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);

        //alarm_emptystate.setOnClickListener(this);


        try {
            readAlarmFile();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        alarmIsEmpty(alarmList);

        //adapter = new AlarmFragmentAdapter(getActivity(), alarmList, this,this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        saveAlarmFile();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    private void readAlarmFile() throws ClassNotFoundException {


        try {
            FileInputStream fis = Objects.requireNonNull(getContext()).openFileInput(alarmFileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            alarmList = (List<Schedule>) is.readObject();
            is.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void alarmIsEmpty(List<Schedule> listParameter) {

        if (listParameter.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            alarm_emptystate.setVisibility(View.VISIBLE);


        } else {
            recyclerView.setVisibility(View.VISIBLE);
            alarm_emptystate.setVisibility(View.GONE);

        }

    }

    private void saveAlarmFile() {
        try {
            FileOutputStream open = getActivity().openFileOutput(alarmFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(open);
            os.writeObject(alarmList);
            os.close();


        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setUIArguments(final Schedule args, final Context context) {
        if(context!=null){
            ((MainActivity)context).runOnUiThread(new Runnable() {
                public void run() {
                    if (args != null) {

                        boolean check = false;
                        for (int i = 0; i < alarmList.size(); i++) {
                            if (args == alarmList.get(i)) {
                                alarmList.set(i, args);
                                check = true;
                            }

                        }
                        if (!check) {
                            alarmList.add(args);
                        }

                        index = alarmList.size() - 1;
                        alarmCount = alarmList.size();
                        adapter.notifyDataSetChanged();
                        alarmIsEmpty(alarmList);
                        alarmEnable(args);
                    }
                }
            });
        }


    }

//    public void setDragDrop() {
//        itemTouchHelperCallback = new ItemTouchHelper.Callback() {
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//
//                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
//            }
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                Collections.swap(alarmList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//                return true;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        };
//
//    }

    public void alarmEnable(Schedule alarmEnable) {
        alarmEnable.setState(true);

//        setRepeatDay(alarmEnable);

    }

    public void alarmDisable(Schedule alarmDisable) {
        alarmDisable.setState(false);
        Intent intent = new Intent(getActivity(), AlarmNotification_onTime.class);
        int notificationID = alarmDisable.getId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), notificationID, intent, 0);
        alarmManager.cancel(pendingIntent);


    }

//
//    @Override
//    public void onAlarmSwitch(Schedule alarm) {
//
//
//        if (alarm.getState()) {
//            alarmEnable(alarm);
//        }
//        if (!alarm.getState()) {
//            alarmDisable(alarm);
//        }
//
//    }
//
//
//
//    @Override
//    public void onAlarmCount(int count) {
//        alarmCount = count;
//    }

    /*public void setRepeatDay(Schedule alarmEnable) {
        if (alarmEnable.getNotification().isSunday()) {
            forday(alarmEnable, 1);
        }
        if (alarmEnable.getNotification().isMonday()) {
            forday(alarmEnable, 2);
        }
        if (alarmEnable.getNotification().isTuesday()) {
            forday(alarmEnable, 3);
        }
        if (alarmEnable.getNotification().isWednesday()) {
            forday(alarmEnable, 4);
        }
        if (alarmEnable.getNotification().isThursday()) {
            forday(alarmEnable, 5);
        }
        if (alarmEnable.getNotification().isFriday()) {
            forday(alarmEnable, 6);
        }
        if (alarmEnable.getNotification().isSaturday()) {
            forday(alarmEnable, 7);
        }

    }
*/
    public void forday(Schedule alarmEnable, int week) {
        Calendar repeatWeek = Calendar.getInstance();
 //       long timeToAlarm = alarmEnable.getNotification().getNotificationTime();
 //       repeatWeek.setTimeInMillis(timeToAlarm);
        repeatWeek.set(Calendar.DAY_OF_WEEK, week);
        repeatWeek.set(Calendar.SECOND,0);
        int notificationID = alarmEnable.getId();


        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("notificationID", alarmEnable.getId());




        if (repeatWeek.before(Calendar.getInstance())) {

            do {
                repeatWeek.setTimeInMillis(repeatWeek.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7);
            } while (repeatWeek.before(Calendar.getInstance()));
            intent.putExtra("timeRepeatWeek", repeatWeek.getTimeInMillis());
//            intent.putExtra("message", alarmEnable.getNotification().getMessage());
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), notificationID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,repeatWeek.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);
            Toast.makeText(getContext(), sdfDate.format(repeatWeek.getTimeInMillis()), Toast.LENGTH_LONG).show();
          //  Toast.makeText(getContext(), sdfDate.format(repeatWeek.getTimeInMillis()), Toast.LENGTH_LONG).show();


        } else {
            intent.putExtra("timeRepeatWeek", repeatWeek.getTimeInMillis());
//            intent.putExtra("message", alarmEnable.getNotification().getMessage());
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), notificationID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    repeatWeek.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            Toast.makeText(getContext(), "Alarm set!", Toast.LENGTH_LONG).show();
             //Toast.makeText(getContext(),sdfDate.format(repeatWeek.getTimeInMillis()),Toast.LENGTH_LONG).show();


        }
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId())
//        {
//            case R.id.alarm_emptystate:
//                int newFrag = 0; //the number of the new Fragment to show
//                ScheduleTab parent = (ScheduleTab) getParentFragment();
//                parent.setPagerFragment(newFrag);
//                break;
//        }
//    }


}
