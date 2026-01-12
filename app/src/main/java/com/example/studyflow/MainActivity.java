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
            showAddDialog();
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

    private void showAddDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        dialogView.findViewById(R.id.tv_add_lecture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               // უნდა შევცვალო დიზაინის გმო
              openAddLecture();
            }
        });

        dialogView.findViewById(R.id.tv_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // აქაც იგივე
               openAddTask();
            }
        });

        dialog.show();
    }

    private void openAddTask() {
    }

    private void openAddLecture() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AddEditLectureFragment())
                .addToBackStack(null)
                .commit();
    }
    public void reloadSchedule() {
        replaceFragment(new ScheduleFragment());
    }



}