package com.example.studyflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {
    private List<LectureItem> lectureList;
    private boolean isSearchMode = false;
    public LectureAdapter(List<LectureItem> lectureList) {
        this.lectureList = lectureList;
    }


    public LectureAdapter(List<LectureItem> lectureList, boolean isSearchMode) {
        this.lectureList = lectureList;
        this.isSearchMode = isSearchMode;
    }
    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lecture_layout, parent, false);
        return new LectureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        LectureItem lecture = lectureList.get(position);

        holder.tv_time.setText(lecture.getStartTime() + " - " + lecture.getEndTime());
        holder.tv_subject.setText(lecture.getSubject());
        holder.tv_room.setText(lecture.getRoom());
        holder.tv_lecturer.setText(lecture.getTeacher());



        if (isSearchMode) {
            holder.tv_day_name.setVisibility(View.VISIBLE);
            holder.tv_day_name.setText(getDayName(lecture.getDayOfWeek()));
        } else {
            holder.tv_day_name.setVisibility(View.GONE);
        }


        holder.iv_edit.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("lectureId", lecture.getId());
                    bundle.putString("subject", lecture.getSubject());
                    bundle.putString("start", lecture.getStartTime());
                    bundle.putString("end", lecture.getEndTime());
                    bundle.putString("room", lecture.getRoom());
                    bundle.putString("teacher", lecture.getTeacher());
                    bundle.putInt("day", lecture.getDayOfWeek());
            bundle.putLong("notificationTime", lecture.getNotificationTime());

                    AddEditLectureFragment fragment = new AddEditLectureFragment();
                    fragment.setArguments(bundle);

                    ((MainActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                      });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                            .setTitle("წაშლა")
                            .setMessage("დარწმუნებული ხართ რომ გინდათ წაშლა?")
                            .setPositiveButton("დიახ", (dialog, which) -> {
                                DB db = new DB(v.getContext());
                                db.deleteLecture(lecture.getId());
                                ((MainActivity) v.getContext()).reloadSchedule();
                            })
                            .setNegativeButton("არა", null)
                            .show();
                    return true;
                }
            });
        }


    private String getDayName(int day) {
        switch (day) {
            case 1: return "ორშაბათი";
            case 2: return "სამშაბათი";
            case 3: return "ოთხშაბათი";
            case 4: return "ხუთშაბათი";
            case 5: return "პარასკევი";
            case 6: return "შაბათი";
            case 7: return "კვირა";
            default: return "";
        }
    }

    public int getItemCount() {
        return lectureList.size();
    }

    static class LectureViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time, tv_subject, tv_room, tv_lecturer, tv_day_name;
        ImageView iv_edit;
        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_room = itemView.findViewById(R.id.tv_room);
            tv_lecturer = itemView.findViewById(R.id.tv_lecturer);
            iv_edit = itemView.findViewById(R.id.btn_edit);
            tv_day_name = itemView.findViewById(R.id.tv_day_name);
        }
    }
}
