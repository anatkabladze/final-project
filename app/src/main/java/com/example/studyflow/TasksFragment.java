package com.example.studyflow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TasksAdapter adapter;
    private List<Task> taskList;
    private FloatingActionButton fabAddTask;
    private Button btnPending, btnCompleted;
    private Spinner spinnerSort;

    private int currentStatus = 0;
    private String currentSortOrder = "deadline ASC";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        fabAddTask = view.findViewById(R.id.fab_add_task);
        btnPending = view.findViewById(R.id.btn_pending);
        btnCompleted = view.findViewById(R.id.btn_completed);
        spinnerSort = view.findViewById(R.id.spinner_sort);

        taskList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TasksAdapter(taskList, getContext());
        recyclerView.setAdapter(adapter);

        setupSortSpinner();

        btnPending.setOnClickListener(v -> {
            currentStatus = 0;
            loadTasks();
        });

        btnCompleted.setOnClickListener(v -> {
            currentStatus = 1;
            loadTasks();
        });

        adapter.setOnTaskStatusChangedListener(() -> {
            loadTasks();
        });

        fabAddTask.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddEditTaskFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadTasks();
        return view;
    }

    private void setupSortSpinner() {
        String[] sortOptions = {"დედლაინის მიხედვით", "პრიორიტეტის მიხედვით"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentSortOrder = "deadline ASC";
                } else {
                    currentSortOrder = "priority ASC";
                }
                loadTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadTasks() {
        DB db = new DB(getContext());
        taskList.clear();

        taskList.addAll(db.getTasksByStatus(currentStatus, currentSortOrder));
        adapter.notifyDataSetChanged();
        updateButtonStyles();
    }

    private void updateButtonStyles() {
        if (currentStatus == 0) {
            btnPending.setBackgroundColor(Color.parseColor("#6200EE"));
            btnPending.setTextColor(Color.WHITE);
            btnCompleted.setBackgroundColor(Color.LTGRAY);
            btnCompleted.setTextColor(Color.BLACK);
        } else {
            btnPending.setBackgroundColor(Color.LTGRAY);
            btnPending.setTextColor(Color.BLACK);
            btnCompleted.setBackgroundColor(Color.parseColor("#6200EE"));
            btnCompleted.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
}