package com.irfan.draft1.Schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.irfan.draft1.Alarms.PassAlarmActivityListener;
import com.irfan.draft1.Maps.MapFullscreen;
import com.irfan.draft1.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by User on 5/26/2017.
 */

public class TimetableTabFragment extends Fragment implements AdapterView.OnItemSelectedListener {//TimetableFragmentAdapter.onPassAlarm {




    private TimetableFragmentAdapter scheduleAdapter;
    private List<Schedule> schedule = new ArrayList<>();
    private Spinner busStopTitle;
    private RecyclerView recyclerView;
    private TabLayout timetableSelector;
    private int timetableIndex = 0;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_schedule_fragment, container, false);


        //TIMETABLE SELECTOR
        timetableSelector = view.findViewById(R.id.timetable_selector);
        setTimetablePanel();
        for (int i = 0; i < timetableSelector.getTabCount(); i++) {
            TabLayout.Tab tab = timetableSelector.getTabAt(i);
            Objects.requireNonNull(tab).setCustomView(R.layout.badged_tab);
        }


        //SPINNER
        busStopTitle = view.findViewById(R.id.locationTitle);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.bus_stops, R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        busStopTitle.setAdapter(spinnerAdapter);
        busStopTitle.setOnItemSelectedListener(this);

//        selectDay = (Spinner) view.findViewById(R.id.selectDay);
//        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(getActivity(),
//                R.array.select_day, R.layout.simple_spinner_item);
//        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
//        selectDay.setAdapter(spinnerAdapter2);
        // selectDay.setOnItemSelectedListener(this);



        //TIMETABLE
        scheduleAdapter = new TimetableFragmentAdapter(schedule, getActivity());
        recyclerView = view.findViewById(R.id.schedule_list);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //setSchedulesWeekday(busStopTitle.getItemAtPosition(0).toString());

        FloatingActionButton openMap = view.findViewById(R.id.open_maps);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapFullscreen.class);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(scheduleAdapter);
        return view;
    }

    public void setTimetablePanel() {
        timetableSelector.addTab(timetableSelector.newTab().setText("Monday - Friday"));
        timetableSelector.addTab(timetableSelector.newTab().setText("Saturday"));
        timetableSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTimetable(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setTimetable(tab.getPosition());
            }
        });
    }

    public void setTimetable(int index) {
        switch (index) {
            case 0:             //Monday-Friday
                timetableIndex = 0;
                setSchedulesWeekday(busStopTitle.getSelectedItem().toString());
                break;
            case 1:             //Saturday
                timetableIndex = 1;
                setSchedulesWeekend(busStopTitle.getSelectedItem().toString());
                break;
        }
    }


    public void setSchedulesWeekend(String location) {
        downloadSchedule("WeekendBusSchedule", location);
    }

    public void setSchedulesWeekday(final String location) {
        downloadSchedule("WeekdayBusSchedule", location);
    }

    private void downloadSchedule(String collectionName, String location) {
        schedule.clear();
        db.collection(collectionName).document(location).collection("Timetable").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        Schedule receivedSchedule = documentSnapshot.toObject(Schedule.class);
                        schedule.add(receivedSchedule);

                    }
                } else {
                    Snackbar.make(Objects.requireNonNull(getView()), "Error getting the schedule information", Snackbar.LENGTH_SHORT);
                }

                scheduleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String location = adapterView.getItemAtPosition(position).toString();
        if (timetableIndex == 0) {
            setSchedulesWeekday(location);
            recyclerView.setAdapter(scheduleAdapter);
        } else {
            setSchedulesWeekend(location);
            recyclerView.setAdapter(scheduleAdapter);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

//        HashMap<String, Date[]> busSchedule = new HashMap<>();
//        busSchedule.put("Campus", ScheduleWeekdays(0, 0));
//        busSchedule.put("Villa", ScheduleWeekdays(7 * MINUTE, 7 * MINUTE));
//        busSchedule.put("Nam Leong", ScheduleWeekdays(10 * MINUTE, 10 * MINUTE));
//        busSchedule.put("Perkasa", ScheduleWeekdays(5 * MINUTE, 12 * MINUTE));
//        busSchedule.put("Water I", ScheduleWeekdays(7 * MINUTE, 14 * MINUTE));
//        busSchedule.put("Water II", ScheduleWeekdays(9 * MINUTE, 16 * MINUTE));
//
//
//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//
//        boolean monday = false;
//        boolean tuesday = false;
//        boolean wednesday = false;
//        boolean thursday = false;
//        boolean saturday = false;
//        boolean friday = false;
//        boolean sunday = false;
//
//        switch (day) {
//            case Calendar.SUNDAY:
//                sunday = true;
//                break;
//            case Calendar.MONDAY:
//                monday = true;
//                break;
//            case Calendar.TUESDAY:
//                tuesday = true;
//                break;
//            case Calendar.WEDNESDAY:
//                wednesday = true;
//                break;
//            case Calendar.THURSDAY:
//                thursday = true;
//                break;
//            case Calendar.FRIDAY:
//                friday = true;
//                break;
//            case Calendar.SATURDAY:
//                saturday = true;
//                break;
//        }
//        if (!schedule.isEmpty()) {
//            schedule.clear();
//        }
//
//        for (int i = 0; i < busSchedule.get(location).length; i++) {
//
//            try {
//        //        Notification notification = new Notification(time.get(i), 3, "", monday, tuesday, wednesday, thursday, friday, saturday, sunday);
//        //        entry = new Schedule(time.get(i), location, time.get(i).intValue(), false, notification);
//                schedule.set(i, new Schedule(busSchedule.get(location)[i],location,(int)busSchedule.get(location)[i].getTime(),false));
//
//
//            } catch (IndexOutOfBoundsException e) {
//
//          //      time.add(i, (busSchedule.get(location)[i].getTime()));
//          //      Notification notification = new Notification(time.get(i), 3, "", monday, tuesday, wednesday, thursday, friday, saturday, sunday);
//          //      entry = new Schedule(time.get(i), location, time.get(i).intValue(), false, notification);
//
//                Schedule ok = new Schedule(busSchedule.get(location)[i],location,(int)busSchedule.get(location)[i].getTime(),false);
//                schedule.add(ok);
//
//
//            }
//
/*
    private Date[] ScheduleWeekend(long delay) {
        Calendar[] saturday = new Calendar[11];
        for (int i = 0; i < saturday.length; i++) {
            saturday[i] = Calendar.getInstance();
            saturday[i].set(Calendar.HOUR_OF_DAY, 7);
            saturday[i].set(Calendar.MINUTE, 30);
            saturday[i].setTimeInMillis(saturday[i].getTimeInMillis() + delay+ MINUTE * 60 * i);
        }
        Date[] times = new Date[11];
        for (int o = 0; o <11; o++){
            times[o] = saturday[o].getTime();
        }
        return times;
    }*/

    /*private Date[] ScheduleWeekdays(long morning, long afternoon) {
        Calendar[] pickupTimes = new Calendar[47];

        for (int i = 0; i < pickupTimes.length; i++) {
            pickupTimes[i] = Calendar.getInstance();
        }

        for (int i = 0; i < 11; i++) {
            pickupTimes[i].set(Calendar.HOUR_OF_DAY, 7);
            pickupTimes[i].set(Calendar.MINUTE, 0);
            pickupTimes[i].set(Calendar.SECOND, 0);
            pickupTimes[i].setTimeInMillis(pickupTimes[i].getTimeInMillis() + morning + MINUTE * 15 * i);
        }
        for (int i = 11; i < 28; i++) {
            pickupTimes[i].set(Calendar.HOUR_OF_DAY, 7);
            pickupTimes[i].set(Calendar.MINUTE, 0);
            pickupTimes[i].set(Calendar.SECOND, 0);
            pickupTimes[i].setTimeInMillis(pickupTimes[i].getTimeInMillis() + afternoon + MINUTE * 15 * i);
        }
        for (int i = 28; i < 43; i++) {
            pickupTimes[i].set(Calendar.HOUR_OF_DAY, 14);
            pickupTimes[i].set(Calendar.MINUTE, 0);
            pickupTimes[i].set(Calendar.SECOND, 0);
            pickupTimes[i].setTimeInMillis(pickupTimes[i].getTimeInMillis() + afternoon + MINUTE * 20 * (i - 28));
        }
        for (int i = 43; i < 46; i++) {
            pickupTimes[i].set(Calendar.HOUR_OF_DAY, 19);
            pickupTimes[i].set(Calendar.MINUTE, 0);
            pickupTimes[i].set(Calendar.SECOND, 0);
            pickupTimes[i].setTimeInMillis(pickupTimes[i].getTimeInMillis() + afternoon + MINUTE * 30 * (i - 43));
        }
        pickupTimes[46].set(Calendar.HOUR_OF_DAY, 21);
        pickupTimes[46].set(Calendar.MINUTE, 0);
        pickupTimes[46].set(Calendar.SECOND, 0);
        pickupTimes[46].setTimeInMillis(pickupTimes[46].getTimeInMillis() + afternoon);

        Date[] times = new Date[47];
        for (int o = 0; o <47; o++){
            times[o] = pickupTimes[o].getTime();
        }


        return times;
    }
*/


