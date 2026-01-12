package com.example.studyflow;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<DayItem> dayList;
    private OnDayClickListener listener;
    private int selectedPosition = -1;

    public DayAdapter(List<DayItem> dayList, OnDayClickListener listener) {

        this.dayList = dayList;
        this.listener = listener;

    }

    public void setSelectedDay(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;


        if (previousPosition != -1) {
            notifyItemChanged(previousPosition);
        }
        notifyItemChanged(selectedPosition);
    }


    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_layout, parent, false);
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DayItem day = dayList.get(position);
        holder.tv_day_name.setText(day.getDayName());
        holder.tv_day_number.setText(String.valueOf(day.getDayNumber()));


        if (position == selectedPosition) {

            holder.card_day.setStrokeWidth(4);
            holder.card_day.setStrokeColor(0xFF1976D2);
            holder.card_day.setCardElevation(8);
            holder.tv_day_number.setBackgroundResource(R.drawable.day_number_selected);
            holder.tv_day_number.setTextColor(0xFFFFFFFF);
            holder.tv_day_name.setTextColor(0xFF1976D2);
        } else {

            holder.card_day.setStrokeWidth(0);
            holder.card_day.setCardElevation(2);
            holder.tv_day_number.setBackgroundResource(R.drawable.day_number_background);
            holder.tv_day_number.setTextColor(0xFF212121);
            holder.tv_day_name.setTextColor(0xFF757575);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    setSelectedDay(position);
                    listener.onDayClick(position); // position = 0-6
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }


    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tv_day_name;
        TextView tv_day_number;
        MaterialCardView card_day;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_day_name = itemView.findViewById(R.id.tv_day_name);
            tv_day_number = itemView.findViewById(R.id.tv_day_number);
            card_day = itemView.findViewById(R.id.card_day);
        }
    }

    public interface OnDayClickListener {
        void onDayClick(int dayIndex);
    }
}


