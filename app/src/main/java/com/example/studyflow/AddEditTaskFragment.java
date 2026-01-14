package com.example.studyflow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskFragment extends Fragment {

    private TextInputEditText etTitle, etDescription, etLecture;
    private Button btnPickDate, btnPickTime, btnSave, btnCancel;
    private TextView tvSelectedDeadline;
    private RadioGroup rgPriority;

    private Calendar selectedDeadline = Calendar.getInstance();
    private boolean isDateSet = false;
    private boolean isTimeSet = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        // ვიუების ინიციალიზაცია
        etTitle = view.findViewById(R.id.et_task_title);
        etDescription = view.findViewById(R.id.et_task_description);
        etLecture = view.findViewById(R.id.et_task_lecture);
        btnPickDate = view.findViewById(R.id.btn_pick_date);
        btnPickTime = view.findViewById(R.id.btn_pick_time);
        tvSelectedDeadline = view.findViewById(R.id.tv_selected_deadline);
        rgPriority = view.findViewById(R.id.rg_priority);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

        // თარიღის არჩევა
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // დროის არჩევა
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // შენახვა
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        // გაუქმება
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDeadline.set(Calendar.YEAR, year);
                    selectedDeadline.set(Calendar.MONTH, month);
                    selectedDeadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    isDateSet = true;
                    updateDeadlineText();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        if (!isDateSet) {
            Toast.makeText(getContext(), "ჯერ აირჩიეთ თარიღი", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar now = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedDeadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDeadline.set(Calendar.MINUTE, minute);
                    selectedDeadline.set(Calendar.SECOND, 0);
                    isTimeSet = true;
                    updateDeadlineText();
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void updateDeadlineText() {
        if (isDateSet && isTimeSet) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            tvSelectedDeadline.setText(sdf.format(selectedDeadline.getTime()));
            tvSelectedDeadline.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (isDateSet) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            tvSelectedDeadline.setText(sdf.format(selectedDeadline.getTime()) + " - აირჩიეთ დრო");
            tvSelectedDeadline.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    private void saveTask() {
        // ვალიდაცია
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("შეავსეთ დასახელება");
            etTitle.requestFocus();
            return;
        }

        if (!isDateSet || !isTimeSet) {
            Toast.makeText(getContext(), "აირჩიეთ დედლაინი", Toast.LENGTH_SHORT).show();
            return;
        }

        // მონაცემების აღება
        String description = etDescription.getText().toString().trim();
        String lectureName = etLecture.getText().toString().trim();
        long deadline = selectedDeadline.getTimeInMillis();

        // პრიორიტეტის განსაზღვრა
        int priority = 2; // default საშუალო
        int selectedId = rgPriority.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_priority_high) {
            priority = 1;
        } else if (selectedId == R.id.rb_priority_low) {
            priority = 3;
        }

        // ახალი თასქის შექმნა შენი constructor-ით
        Task task = new Task(title, description, lectureName, deadline, priority, -1);

        // ბაზაში შენახვა
        DB db = new DB(getContext());
        long result = db.insertTask(task);

        if (result > 0) {
            Toast.makeText(getContext(), "დავალება შენახულია", Toast.LENGTH_SHORT).show();

            // TasksFragment-ზე დაბრუნება
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "შეცდომა შენახვისას", Toast.LENGTH_SHORT).show();
        }
    }
}