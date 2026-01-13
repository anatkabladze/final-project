package com.example.studyflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TasksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fabAddTask;
    private Spinner spinnerSort;
    private TasksPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        fabAddTask = view.findViewById(R.id.fab_add_task);
        spinnerSort = view.findViewById(R.id.spinner_sort);

        pagerAdapter = new TasksPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("გასაკეთებელი");
            } else {
                tab.setText("დასრულებული");
            }
        }).attach();

        setupSortSpinner();

        fabAddTask.setOnClickListener(v -> {
            // TODO: გახსნა AddEditTaskFragment
        });

        return view;
    }

    private void setupSortSpinner() {
        String[] sortOptions = {"დედლაინის მიხედვით", "პრიორიტეტის მიხედვით"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: სორტირება - ეს შემდეგ ნაბიჯზე გავაკეთებთ
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}