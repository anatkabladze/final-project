package com.example.studyflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {

    private List<LectureItem> lectureList;

    public LectureAdapter(List<LectureItem> lectureList) {
        this.lectureList = lectureList;
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


        holder.tv_time.setText(
                lecture.getStartTime() + " - " + lecture.getEndTime()
        );


        holder.tv_subject.setText(lecture.getSubject());


        holder.tv_room.setText(lecture.getRoom());

        holder.tv_lecturer.setText(lecture.getTeacher());
    }

    @Override
    public int getItemCount() {
        return lectureList.size();
    }

    static class LectureViewHolder extends RecyclerView.ViewHolder {

        TextView tv_time, tv_subject, tv_room, tv_lecturer;

        public LectureViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_room = itemView.findViewById(R.id.tv_room);
            tv_lecturer = itemView.findViewById(R.id.tv_lecturer);
        }
    }
}
