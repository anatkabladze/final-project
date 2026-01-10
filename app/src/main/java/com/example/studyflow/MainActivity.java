package com.example.studyflow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
 LinearLayout btn_schedule, btn_search, btn_add, btn_tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_schedule = findViewById(R.id.btn_schedule);
        btn_search   = findViewById(R.id.btn_search);
        btn_add      = findViewById(R.id.btn_add);
        btn_tasks    = findViewById(R.id.btn_tasks);



        replaceFragment(new ScheduleFragment());

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ScheduleFragment());
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //           replaceFragment(new SearchFragment());
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      //          replaceFragment(new AddFragment());
            }
        });

        btn_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //           replaceFragment(new TasksFragment());
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}