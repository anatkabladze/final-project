package com.example.studyflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<DayItem> dayList;

    public DayAdapter(List<DayItem> dayList) {
        this.dayList = dayList;
    }


    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_layout, parent, false);
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayItem day = dayList.get(position);
        holder.tv_day_name.setText(day.getDayName());
        holder.tv_day_number.setText(String.valueOf(day.getDayNumber()));
    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }


    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day_name;
        TextView tv_day_number;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day_name = itemView.findViewById(R.id.tv_day_name);
            tv_day_number = itemView.findViewById(R.id.tv_day_number);
        }
    }
}
