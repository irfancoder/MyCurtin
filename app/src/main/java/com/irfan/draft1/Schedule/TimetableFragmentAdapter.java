package com.irfan.draft1.Schedule;

/**
 * Created by irfan on 11/06/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static android.view.HapticFeedbackConstants.LONG_PRESS;

/**
 * Created by User on 6/1/2017.
 */

public class TimetableFragmentAdapter extends RecyclerView.Adapter<TimetableFragmentAdapter.ScheduleHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private List<Schedule> scheduleList;

    private Schedule alarm;
    private Context context;

    @SuppressLint("SimpleDateFormat") private SimpleDateFormat sdfAMPM = new SimpleDateFormat("hh:mm a");
    @SuppressLint("SimpleDateFormat") private SimpleDateFormat sdfNone = new SimpleDateFormat("hh:mm");
    @SuppressLint("SimpleDateFormat") private SimpleDateFormat sdfScroll = new SimpleDateFormat("h a");
    private Calendar getPeriod = Calendar.getInstance();


    TimetableFragmentAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
        setHasStableIds(true);

    }


    @NonNull
    @Override
    public TimetableFragmentAdapter.ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_list_interface, parent, false);

        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleHolder holder, final int position) {

        scrollingErrorFix(scheduleList, holder, position);


    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void scrollingErrorFix(List<Schedule> lel, ScheduleHolder holder, int position) {

        alarm = lel.get(position);

        getPeriod.setTime(alarm.getTime());

        int am = getPeriod.get(Calendar.AM_PM);
        final String timeDisplayNone = sdfNone.format(getPeriod.getTimeInMillis());
        if (!isEmpty(lel.toString())) {
            holder.time.setText(timeDisplayNone);
            if (am == Calendar.AM) {
                holder.ampm.setText("AM");
                holder.ampm.setVisibility(View.VISIBLE);
                holder.ampm.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            } else {
                holder.ampm.setText("PM");
                holder.ampm.setVisibility(View.VISIBLE);
                holder.ampm.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            }


        }
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return sdfScroll.format(scheduleList.get(position).getTime());
    }


    //   public void alarmSet(Long time, int notificationID, final ScheduleHolder holder, int position) {


//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmNotification_onTime.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//
//        holder.ampm.setTextColor(ContextCompat.getColor(context, R.color.cardview_light_background));
//        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//        holder.time.setTextColor(ContextCompat.getColor(context, R.color.cardview_light_background));


//        Snackbar.make(holder.itemView, "Alarm is set for " + sdfAMPM.format(alarm.getTime()), Snackbar.LENGTH_LONG)
//                .setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alarmRemove(holder.getAdapterPosition(), holder);
//                    }
//                })
//                .show();
//    }

//    public void alarmRemove(int notificationID, ScheduleHolder holder) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmNotification_onTime.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationID, intent, 0);
//        alarmManager.cancel(pendingIntent);
//
//
//        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.cardview_light_background));
//        holder.time.setTextColor(ContextCompat.getColor(context, R.color.colorTextAlarmSelected));
//        holder.ampm.setTextColor(ContextCompat.getColor(context, R.color.colorAMPMDeselected));
//    }


    public class ScheduleHolder extends RecyclerView.ViewHolder {
        public TextView time, ampm;
        RelativeLayout cardView;


        ScheduleHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            time = itemView.findViewById(R.id.time);
            ampm = itemView.findViewById(R.id.ampm);
//            cardView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);

        }


//        @Override
//        public void onClick(View view) {
//            final int position = getAdapterPosition();
//            final Schedule alarm = scheduleList.get(position);
//
//            //  addAlarm(alarm);
//
//        }

//        @Override
//        public boolean onLongClick(View view) {
//            view.performHapticFeedback(LONG_PRESS);
//            final Schedule alarmOption = scheduleList.get(getAdapterPosition());
//            PopupMenu popupMenu = new PopupMenu(context, time);
//            popupMenu.getMenuInflater().inflate(R.menu.schedule_menu, popupMenu.getMenu());
//            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.add_alarm_option:
//                            alarmOption.setState(true);
//                            final AlarmDialog dialog = new AlarmDialog();
//
//                            dialog.showDialog(context, (MainActivity) context, alarm);
//
////                            saveFunction.addPassAlarm(alarmOption);
//                            return true;
//
//                        case R.id.share_alarm_option:
//                            final String timeDisplayAMPM = sdfAMPM.format(alarmOption.getTime());
//                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Taking the " + timeDisplayAMPM + " bus from " + alarmOption.getLocation() + ".\n Shared from MyCurtin");
//                            sendIntent.setType("text/plain");
//                            context.startActivity(sendIntent);
//                            return true;
//                    }
//                    return false;
//                }
//            });
//            popupMenu.show();
//
//            return true;
//        }
    }


    public void addAlarm(final Schedule alarm) {
        final AlarmDialog dialog = new AlarmDialog();

        dialog.showDialog(context, (MainActivity) context, alarm);

    }

//    public interface onPassAlarm {
//        void addPassAlarm(Schedule alarmEntry);
//    }

}




