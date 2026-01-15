package com.example.studyflow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
    private CheckBox cbStatus;

    private Calendar selectedDeadline = Calendar.getInstance();
    private boolean isDateSet = false;
    private boolean isTimeSet = false;
    private int taskId = -1;
    private boolean isReadOnly = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        etTitle = view.findViewById(R.id.et_task_title);
        etDescription = view.findViewById(R.id.et_task_description);
        etLecture = view.findViewById(R.id.et_task_lecture);
        btnPickDate = view.findViewById(R.id.btn_pick_date);
        btnPickTime = view.findViewById(R.id.btn_pick_time);
        tvSelectedDeadline = view.findViewById(R.id.tv_selected_deadline);
        rgPriority = view.findViewById(R.id.rg_priority);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);
        cbStatus = view.findViewById(R.id.cb_status);

        btnPickDate.setOnClickListener(v -> showDatePicker());
        btnPickTime.setOnClickListener(v -> showTimePicker());

        if (getArguments() != null) {
            taskId = getArguments().getInt("taskId", -1);
            isReadOnly = getArguments().getBoolean("isReadOnly", false);

            if (taskId != -1) {
                loadTaskData(taskId);
                if (isReadOnly) {
                    setupReadOnlyMode();
                } else {
                    setupEditMode();
                }
            } else {
                setupAddMode();
            }
        } else {
            setupAddMode();
        }

        return view;
    }

    private void setupAddMode() {
        toggleInputs(true);
        cbStatus.setVisibility(View.GONE);
        btnSave.setText("შენახვა");
        btnSave.setOnClickListener(v -> saveTask());
        btnCancel.setText("გაუქმება");
        btnCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setupEditMode() {
        toggleInputs(true);
        btnSave.setText("განახლება");
        cbStatus.setVisibility(View.VISIBLE);
        btnSave.setOnClickListener(v -> saveTask());
        btnCancel.setText("წაშლა");
        btnCancel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
        btnCancel.setOnClickListener(v -> deleteTask());
    }

    private void setupReadOnlyMode() {
        toggleInputs(false);
        btnSave.setText("რედაქტირება");
        btnSave.setOnClickListener(v -> enableEditing());
        cbStatus.setVisibility(View.VISIBLE);
        btnCancel.setText("წაშლა");
        btnCancel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
        btnCancel.setOnClickListener(v -> deleteTask());
    }

    private void enableEditing() {
        toggleInputs(true);
        btnSave.setText("განახლება");
        btnSave.setOnClickListener(v -> saveTask());

        btnCancel.setText("გაუქმება");
        btnCancel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray)));
        btnCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void toggleInputs(boolean status) {
        etTitle.setEnabled(status);
        etDescription.setEnabled(status);
        etLecture.setEnabled(status);



        cbStatus.setEnabled(status);
        btnPickDate.setVisibility(status ? View.VISIBLE : View.GONE);
        btnPickTime.setVisibility(status ? View.VISIBLE : View.GONE);

        rgPriority.setEnabled(status);
        for (int i = 0; i < rgPriority.getChildCount(); i++) {
            rgPriority.getChildAt(i).setEnabled(status);
        }
    }

    private void deleteTask() {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("წაშლა")
                .setMessage("ნამდვილად გსურთ წაშლა?")
                .setPositiveButton("დიახ", (dialog, which) -> {
                    DB db = new DB(requireContext());
                    db.deleteTask(taskId);
                    Toast.makeText(getContext(), "წაიშალა", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                })
                .setNegativeButton("არა", null)
                .show();
    }

    private void loadTaskData(int id) {
        DB db = new DB(requireContext());
        Task task = db.getTaskById(id);
        if (task != null) {
            etTitle.setText(task.getTitle());
            etDescription.setText(task.getDescription());
            etLecture.setText(task.getLectureName());

            cbStatus.setChecked(task.getIsCompleted() == 1);

            selectedDeadline.setTimeInMillis(task.getDeadline());
            isDateSet = true;
            isTimeSet = true;
            updateDeadlineText();

            if (task.getPriority() == 1) rgPriority.check(R.id.rb_priority_high);
            else if (task.getPriority() == 3) rgPriority.check(R.id.rb_priority_low);
            else rgPriority.check(R.id.rb_priority_medium);
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDeadline.set(Calendar.YEAR, year);
                    selectedDeadline.set(Calendar.MONTH, month);
                    selectedDeadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    isDateSet = true;
                    updateDeadlineText();
                },
                selectedDeadline.get(Calendar.YEAR),
                selectedDeadline.get(Calendar.MONTH),
                selectedDeadline.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> {
                    selectedDeadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDeadline.set(Calendar.MINUTE, minute);
                    isTimeSet = true;
                    updateDeadlineText();
                },
                selectedDeadline.get(Calendar.HOUR_OF_DAY),
                selectedDeadline.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void updateDeadlineText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        if (isDateSet && isTimeSet) {
            tvSelectedDeadline.setText(sdf.format(selectedDeadline.getTime()));
            tvSelectedDeadline.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if (isDateSet) {
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            tvSelectedDeadline.setText(df.format(selectedDeadline.getTime()) + " - აირჩიეთ დრო");
        }
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("შეავსეთ დასახელება");
            return;
        }
        if (!isDateSet || !isTimeSet) {
            Toast.makeText(getContext(), "აირჩიეთ დედლაინი", Toast.LENGTH_SHORT).show();
            return;
        }

        int priority = 2;
        int selectedId = rgPriority.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_priority_high) priority = 1;
        else if (selectedId == R.id.rb_priority_low) priority = 3;

        DB db = new DB(getContext());
        boolean success;
        int isCompleted = cbStatus.isChecked() ? 1 : 0;

        if (taskId == -1) {
            Task task = new Task(title, etDescription.getText().toString(), etLecture.getText().toString(),
                    selectedDeadline.getTimeInMillis(), priority, isCompleted);
            success = db.insertTask(task) > 0;
        } else {
            Task task = new Task(taskId, title, etDescription.getText().toString(), etLecture.getText().toString(),
                    selectedDeadline.getTimeInMillis(), priority, isCompleted, System.currentTimeMillis(), null, -1);
            success = db.updateTask(task) > 0;
        }

        if (success) {
            Toast.makeText(getContext(), "შენახულია", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "შეცდომა!", Toast.LENGTH_SHORT).show();
        }
    }
}