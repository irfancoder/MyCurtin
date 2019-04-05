package com.irfan.draft1.Schedule;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.irfan.draft1.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by irfan on 03/08/2017.
 */

public class WeekendAdapter extends RecyclerView.Adapter<WeekendAdapter.WeekendHolder> {

    private static final int VIEW_TYPE_TOP = 0;
    private static final int VIEW_TYPE_MIDDLE = 1;
    private static final int VIEW_TYPE_BOTTOM = 2;

    private List<Schedule> scheduleList;
    private Context context;
    private LayoutInflater inflater;

    public WeekendAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public WeekendAdapter.WeekendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_weekend_interface, parent, false);
        return new WeekendHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekendAdapter.WeekendHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        SimpleDateFormat sdfAMPM = new SimpleDateFormat("hh:mm");
        holder.mItemTitle.setText(sdfAMPM.format(schedule.getTime()));
        holder.mItemSubtitle.setText(schedule.getLocation());
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TOP:
                // The top of the line has to be rounded

                holder.mItemLine.setBackground(ContextCompat.getDrawable(context,R.drawable.line_bg_top));
                break;
            case VIEW_TYPE_MIDDLE:
                // Only the color could be enough
                // but a drawable can be used to make the cap rounded also here
                holder.mItemLine.setBackground(ContextCompat.getDrawable(context,R.drawable.line_bg_middle));
                break;
            case VIEW_TYPE_BOTTOM:
                holder.mItemLine.getLayoutParams().height = 100;
                holder.mItemLine.setBackground(ContextCompat.getDrawable(context,R.drawable.line_bg_bottom));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == scheduleList.size() - 1) {
            return VIEW_TYPE_BOTTOM;
        } else {
            return VIEW_TYPE_MIDDLE;
        }

    }


    public class WeekendHolder extends RecyclerView.ViewHolder {

        TextView mItemTitle;
        TextView mItemSubtitle;
        FrameLayout mItemLine;

        public WeekendHolder(View itemView) {
            super(itemView);

            mItemTitle = itemView.findViewById(R.id.item_title);
            mItemSubtitle = itemView.findViewById(R.id.item_subtitle);
            mItemLine = itemView.findViewById(R.id.item_line);
        }
    }
}
