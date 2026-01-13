package com.example.studyflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter adapter;
    private List<Task> taskList;
    private int statusFilter; // 0 = გასაკეთებელი, 1 = დასრულებული

    public static TaskListFragment newInstance(int statusFilter) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putInt("status", statusFilter);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        if (getArguments() != null) {
            statusFilter = getArguments().getInt("status", 0);
        }

        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskList = new ArrayList<>();
        adapter = new TasksAdapter(taskList, getContext());
        recyclerView.setAdapter(adapter);

        loadTasks();

        return view;
    }

    private void loadTasks() {
        DB db = new DB(getContext());
        taskList.clear();
        taskList.addAll(db.getTasksByStatus(statusFilter));
        adapter.notifyDataSetChanged();
    }

    public void refreshTasks() {
        loadTasks();
    }
}