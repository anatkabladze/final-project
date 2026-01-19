package com.example.studyflow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private OnTaskStatusChangedListener statusListener;


    public interface OnTaskStatusChangedListener {
        void onTaskStatusChanged();
    }
    public void setOnTaskStatusChangedListener(OnTaskStatusChangedListener listener) {
        this.statusListener = listener;
    }

    public TasksAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        DB db = new DB(context);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.tvLecture.setText(task.getLectureName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
        holder.tvDeadline.setText(sdf.format(new Date(task.getDeadline())));

        if (task.getPriority() == 1) {
            holder.tvPriority.setText("!!!");
            holder.tvPriority.setTextColor(Color.RED);
        } else if (task.getPriority() == 2) {
            holder.tvPriority.setText("!!");
            holder.tvPriority.setTextColor(Color.parseColor("#FFA500"));
        } else {
            holder.tvPriority.setText("!");
            holder.tvPriority.setTextColor(Color.GREEN);
        }

        long now = System.currentTimeMillis();
        long timeLeft = task.getDeadline() - now;
        long twoDays = 2 * 24 * 60 * 60 * 1000L;


        if (timeLeft < 0) {
           holder.tvDeadline.setTextColor(Color.RED);
        } else if (timeLeft < twoDays) {
            holder.tvDeadline.setTextColor(Color.parseColor("#FFA500"));
        } else {
            holder.tvDeadline.setTextColor(Color.GREEN);
        }

        holder.checkbox.setChecked(task.getIsCompleted() == 1);




        holder.checkbox.setOnClickListener(v -> {
            int newStatus = holder.checkbox.isChecked() ? 1 : 0;
            task.setIsCompleted(newStatus);

            db.updateTaskStatus(task.getId(), newStatus);



            if (statusListener != null) {
                statusListener.onTaskStatusChanged();
            }
        });


        holder.itemView.setOnClickListener(v -> {
            if (context instanceof MainActivity) {

                ((MainActivity) context).openTaskDetails(task.getId());
            }
        });


    }



    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView tvTitle, tvDescription, tvLecture, tvDeadline, tvPriority;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox_task);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvDescription = itemView.findViewById(R.id.tv_task_description);
            tvLecture = itemView.findViewById(R.id.tv_task_lecture);
            tvDeadline = itemView.findViewById(R.id.tv_task_deadline);
            tvPriority = itemView.findViewById(R.id.tv_task_priority);
        }
    }
}