package com.example.studyflow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
 LinearLayout btn_schedule, btn_search, btn_add, btn_tasks,btn_stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_schedule = findViewById(R.id.btn_schedule);
        btn_search   = findViewById(R.id.btn_search);
        btn_add      = findViewById(R.id.btn_add);
        btn_tasks    = findViewById(R.id.btn_tasks);
        btn_stats = findViewById(R.id.btn_stats);



        replaceFragment(new LecturesFragment());

        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LecturesFragment());
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               replaceFragment(new SearchFragment());
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
               replaceFragment(new TasksFragment());
            }
        });


        btn_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new StatisticsFragment());
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 102);
            }
        }



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

        dialogView.findViewById(R.id.card_add_task).setOnClickListener(new View.OnClickListener() {
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AddEditTaskFragment())
                .addToBackStack(null)
                .commit();
    }
    public void openTaskDetails(int taskId) {
        AddEditTaskFragment fragment = new AddEditTaskFragment();
        Bundle args = new Bundle();
        args.putInt("taskId", taskId);
        args.putBoolean("isReadOnly", true);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    public void openEditTask(int taskId) {
        AddEditTaskFragment fragment = new AddEditTaskFragment();
        Bundle args = new Bundle();
        args.putInt("taskId", taskId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void openAddLecture() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AddEditLectureFragment())
                .addToBackStack(null)
                .commit();
    }
    public void reloadSchedule() {
        replaceFragment(new LecturesFragment());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "შეტყობინებები ჩაირთო", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "შეტყობინებების უფლება უარყოფილია", Toast.LENGTH_SHORT).show();
            }
        }
    }



}