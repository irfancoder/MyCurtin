package com.irfan.draft1.Alarms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.irfan.draft1.MainClass.MainActivity;
import com.irfan.draft1.R;
import com.irfan.draft1.Schedule.AlarmDialog;
import com.irfan.draft1.Schedule.Notification;
import com.irfan.draft1.Schedule.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by irfan on 04/07/2017.
 */

public class AlarmFragmentAdapter extends RecyclerView.Adapter<AlarmFragmentAdapter.AlarmHolder> {

    private Context context;
    private List<Schedule> alarmList = new ArrayList<>();
    private LayoutInflater inflater;
    private SimpleDateFormat sdfAMPM = new SimpleDateFormat("hh:mm");

    private boolean state;
    private AlarmEnability enableFunction;
    private Calendar getAMPM = Calendar.getInstance();
    private AlarmFragment alarmFragment;



    AlarmFragmentAdapter(Context context, List<Schedule> alarm, AlarmEnability enableFunction, AlarmFragment alarmFragment) {
        this.context = context;
        this.alarmList = alarm;
        this.alarmFragment = alarmFragment;
        this.enableFunction = enableFunction;


    }

    @NonNull
    @Override
    public AlarmFragmentAdapter.AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list_interface, parent, false);
        return new AlarmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmFragmentAdapter.AlarmHolder holder, int position) {
        Schedule alarm = alarmList.get(position);
        getAMPM.setTime(alarm.getTime());
        int am = getAMPM.get(Calendar.AM_PM);
        holder.alarmEntry.setText(sdfAMPM.format(getAMPM.getTimeInMillis()));
        holder.alarmLocation.setText(alarm.getLocation());

        if (alarm.getState())
        {
            holder.aSwitch.setChecked(true);
        }
        else
        {
            holder.aSwitch.setChecked(false);
        }

        if (am == Calendar.AM) {
            holder.alarmAMPM.setText("AM");
        } else {
            holder.alarmAMPM.setText("PM");

        }

  //      repeatDayIndicator(holder,alarm.getNotification());


    }

    private void repeatDayIndicator(AlarmHolder holder, Notification alarm)
    {
        if (alarm.isMonday())
        {
            holder.monday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.monday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isTuesday())
        {
            holder.tuesday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.tuesday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isWednesday())
        {
            holder.wednesday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.wednesday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isThursday())
        {
            holder.thursday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.thursday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isFriday())
        {
            holder.friday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.friday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isSaturday())
        {
            holder.saturday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.saturday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
        if (alarm.isSunday())
        {
            holder.sunday.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }else
        {
            holder.sunday.setTextColor(ContextCompat.getColor(context,R.color.colorDefaultText));
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }


    public class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        private TextView alarmEntry;
        private TextView alarmAMPM;
        private TextView alarmLocation;
        private Switch aSwitch;
        private View shade;
        private TextView monday,tuesday,wednesday,thursday,friday,saturday,sunday;
        private ImageView removeOption;



        AlarmHolder(View itemView) {
            super(itemView);

            alarmEntry = itemView.findViewById(R.id.alarm_entry);
            alarmAMPM = itemView.findViewById(R.id.alarm_ampm);
            alarmLocation = itemView.findViewById(R.id.alarm_location);
            aSwitch = itemView.findViewById(R.id.bell);
            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);
            saturday = itemView.findViewById(R.id.saturday);
            sunday = itemView.findViewById(R.id.sunday);
            shade = itemView.findViewById(R.id.shade);

            aSwitch.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);



        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.alarm_card:

                    final AlarmDialog dialog = new AlarmDialog();
                    if (!alarmList.isEmpty()) {
                        dialog.showDialog(context, (MainActivity) context, alarmList.get(getAdapterPosition()));
                    }

                    break;


            }
        }

        @Override
        public boolean onLongClick(View view) {
//
            final int lastPosition = getAdapterPosition();
            final Schedule toRemove = alarmList.get(getAdapterPosition());
            toRemove.setState(false);
            enableFunction.onAlarmSwitch(alarmList.get(getAdapterPosition()));
            alarmDelete(getAdapterPosition());
            Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarmList.add(lastPosition, toRemove);
                    notifyItemInserted(lastPosition);
                    alarmFragment.alarmIsEmpty(alarmList);
                }
            }).setActionTextColor(ContextCompat.getColor(context,R.color.colorPrimary)).show();
            enableFunction.onAlarmCount(getItemCount());

            return false;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int check = compoundButton.getId();
            if (check == R.id.bell && b) {
                alarmList.get(getAdapterPosition()).setState(true);
               enableFunction.onAlarmSwitch(alarmList.get(getAdapterPosition()));


            }
            if (check == R.id.bell && !b) {
                alarmList.get(getAdapterPosition()).setState(false);
                enableFunction.onAlarmSwitch(alarmList.get(getAdapterPosition()));

            }
            changeEnabilityUI(b);


        }
        private void changeEnabilityUI(boolean state)
        {
            if (!state) {
                shade.setVisibility(View.VISIBLE);
                alarmAMPM.setTextColor(ContextCompat.getColor(context, R.color.colorAMPMDeselected));
                alarmLocation.setTextColor(ContextCompat.getColor(context, R.color.colorAMPMDeselected));
                alarmEntry.setTextColor(ContextCompat.getColor(context, R.color.colorAMPMDeselected));

            }else
            {
                shade.setVisibility(View.INVISIBLE);
                alarmAMPM.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
                alarmLocation.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
                alarmEntry.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

            }
        }

    }


    private void alarmDelete(int position) {
        alarmList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, alarmList.size());
        alarmFragment.alarmIsEmpty(alarmList);

    }



   public interface AlarmEnability
    {
        void onAlarmSwitch(Schedule alarm);
        void onAlarmCount(int count);
    }


}
