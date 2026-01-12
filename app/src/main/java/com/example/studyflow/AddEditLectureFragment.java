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



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class AddEditLectureFragment extends Fragment {
    EditText etSubject, etStart, etEnd, etRoom, etTeacher;

    Button btnSave;
    AutoCompleteTextView etDay;
    ArrayAdapter<String> dayAdapter;
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

        String[] days = {"ორშაბათი","სამშაბათი","ოთხშაბათი","ხუთშაბათი","პარასკევი","შაბათი","კვირა"};
       dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, days);
        etDay.setAdapter(dayAdapter);

        if (getArguments() != null) {
            lectureId = getArguments().getInt("lectureId", -1);

            if (lectureId != -1) { //  tu editia
                etSubject.setText(getArguments().getString("subject"));
                etStart.setText(getArguments().getString("start"));
                etEnd.setText(getArguments().getString("end"));
                etRoom.setText(getArguments().getString("room"));
                etTeacher.setText(getArguments().getString("teacher"));


                int day = getArguments().getInt("day", 1);
                if (day >= 1 && day <= days.length) {
                    etDay.setText(days[day - 1], false);
                }
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

    private void saveLecture() {
        DB db = new DB(requireContext(), "studyflow.db", null, 1);

        int dayOfWeekForDB = dayAdapter.getPosition(etDay.getText().toString()) + 1;


        LectureItem lecture = new LectureItem(
                lectureId,
                etSubject.getText().toString(),
                etStart.getText().toString(),
                etEnd.getText().toString(),
                etRoom.getText().toString(),
                etTeacher.getText().toString(),
                dayOfWeekForDB
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
