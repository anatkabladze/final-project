package com.example.studyflow;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class AddEditLectureFragment extends Fragment {
    EditText etSubject, etStart, etEnd, etRoom, etTeacher;
    private com.google.android.material.textfield.TextInputEditText  etNotifHours, etNotifMinutes;
    Button btnSave;
    AutoCompleteTextView etDay;
    ArrayAdapter<String> dayAdapter;
    TextView tvTitlelecture;
    // ედიტისტვის
    private int lectureId = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.add_edit_lecture_fragment_layout, container,false);
        etSubject = v.findViewById(R.id.et_subject);
        etStart   = v.findViewById(R.id.et_start);
        etEnd     = v.findViewById(R.id.et_end);
        etRoom    = v.findViewById(R.id.et_room);
        etTeacher = v.findViewById(R.id.et_teacher);
        etDay = v.findViewById(R.id.et_day);
        btnSave   = v.findViewById(R.id.btn_save);

        etNotifHours = v.findViewById(R.id.et_notif_hours);
        etNotifMinutes = v.findViewById(R.id.et_notif_minutes);
        tvTitlelecture = v.findViewById(R.id.tv_title_lecture );
        String[] days = {"ორშაბათი","სამშაბათი","ოთხშაბათი","ხუთშაბათი","პარასკევი","შაბათი","კვირა"};
       dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, days);
        etDay.setAdapter(dayAdapter);

        if (getArguments() != null) {
            lectureId = getArguments().getInt("lectureId", -1);

            if (lectureId != -1) {
                tvTitlelecture.setText("ლექციის რედაქტირება"); //  tu editia
                etSubject.setText(getArguments().getString("subject"));
                etStart.setText(getArguments().getString("start"));
                etEnd.setText(getArguments().getString("end"));
                etRoom.setText(getArguments().getString("room"));
                etTeacher.setText(getArguments().getString("teacher"));

                long notifMillis = getArguments().getLong("notificationTime", 0);
                if (notifMillis > 0) {

                    long hours = (notifMillis % (24 * 3600000L)) / 3600000L;
                    long minutes = (notifMillis % 3600000L) / 60000L;


                    if (hours > 0) etNotifHours.setText(String.valueOf(hours));
                    if (minutes > 0) etNotifMinutes.setText(String.valueOf(minutes));
                }
                int day = getArguments().getInt("day", 1);
                if (day >= 1 && day <= days.length) {
                    etDay.setText(days[day - 1], false);
                }
            }
            else {
                tvTitlelecture.setText("ლექციის დამატება");
            }
        }


        v.findViewById(R.id.btn_back).setOnClickListener(view -> {
            getParentFragmentManager().popBackStack();
        });




        btnSave.setOnClickListener(v1 -> saveLecture());



        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(etEnd);
            }
        });


        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(etStart);
            }
        });
        return v;

    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    String h = String.format(Locale.getDefault(), "%02d", hourOfDay);
                    String m = String.format(Locale.getDefault(), "%02d", minute1);
                    editText.setText(h + ":" + m);
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private int getIntFromEt(com.google.android.material.textfield.TextInputEditText et) {
        String val = et.getText().toString().trim();
        return val.isEmpty() ? 0 : Integer.parseInt(val);
    }

    private void saveLecture() {

        String subject = etSubject.getText().toString().trim();
        String start = etStart.getText().toString().trim();
        String end = etEnd.getText().toString().trim();
        String day = etDay.getText().toString().trim();


        if (subject.isEmpty()) {
            etSubject.setError("საგნის სახელი აუცილებელია");
            etSubject.requestFocus();
            return;
        }

        if (start.isEmpty()) {
            etStart.setError("დაწყების დრო აუცილებელია");
            return;
        }

        if (end.isEmpty()) {
            etEnd.setError("დასრულების დრო აუცილებელია");
            return;
        }

        if (day.isEmpty()) {
            etDay.setError("აირჩიეთ კვირის დღე");
            return;
        }


        DB db = new DB(requireContext());
        int dayOfWeekForDB = dayAdapter.getPosition(etDay.getText().toString()) + 1;



        int hours = getIntFromEt(etNotifHours);
        int minutes = getIntFromEt(etNotifMinutes);
        long notificationOffset =  (hours * 3600000L) + (minutes * 60000L);

        LectureItem lecture = new LectureItem(
                lectureId,
                etSubject.getText().toString(),
                etStart.getText().toString(),
                etEnd.getText().toString(),
                etRoom.getText().toString(),
                etTeacher.getText().toString(),
                dayOfWeekForDB,
                notificationOffset
        );
        if (lectureId == -1) {
            db.insertLecture(lecture);
        } else {

            db.updateLecture(lecture);
        }


        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).reloadSchedule();
        }
        getParentFragmentManager().popBackStack();

    }
}
