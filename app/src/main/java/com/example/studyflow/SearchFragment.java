package com.example.studyflow;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchFragment  extends Fragment {
    private EditText etSearchInput;
    private RecyclerView rvLectures, rvTasks;
    private TextView tvHeaderLectures, tvHeaderTasks;
    private DB db;
    private LectureAdapter lectureAdapter;
    private TasksAdapter tasksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DB(getContext());
        etSearchInput = view.findViewById(R.id.et_search_input);
        rvLectures = view.findViewById(R.id.rv_search_lectures);
        rvTasks = view.findViewById(R.id.rv_search_tasks);
        tvHeaderLectures = view.findViewById(R.id.tv_header_lectures);
        tvHeaderTasks = view.findViewById(R.id.tv_header_tasks);

        rvLectures.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString().trim());
            }

            public void afterTextChanged(CharSequence s) {}
        });
    }
    private void performSearch(String query) {
        if (query.isEmpty()) {
            rvLectures.setAdapter(null);
            rvTasks.setAdapter(null);
            tvHeaderLectures.setVisibility(View.GONE);
            tvHeaderTasks.setVisibility(View.GONE);
            return;
        }
        List<LectureItem> foundLectures = db.searchLectures(query);
        if (!foundLectures.isEmpty()) {
            lectureAdapter = new LectureAdapter(foundLectures, true);            rvLectures.setAdapter(lectureAdapter);
            tvHeaderLectures.setVisibility(View.VISIBLE);
        } else {
            rvLectures.setAdapter(null);
            tvHeaderLectures.setVisibility(View.GONE);
        }

        List<Task> foundTasks = db.searchTasks(query);
        if (!foundTasks.isEmpty()) {
            tasksAdapter = new TasksAdapter(foundTasks, getContext());
            rvTasks.setAdapter(tasksAdapter);
            tvHeaderTasks.setVisibility(View.VISIBLE);
        } else {
            rvTasks.setAdapter(null);
            tvHeaderTasks.setVisibility(View.GONE);
        }
    }

    }

