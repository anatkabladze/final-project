package com.example.studyflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private RecyclerView rv_days;
    private DayAdapter dayAdapter;
    private List<DayItem> dayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView tv_month_year = view.findViewById(R.id.tv_month_year);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("LLLL, yyyy", new Locale("ka"));
        tv_month_year.setText(sdf.format(calendar.getTime()));

        rv_days = view.findViewById(R.id.rv_days);
        dayList = new ArrayList<>();

        String[] weekdays = {"ორშ","სამშ","ოთხ","ხუთ","პარ","შაბ","კვი"};

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 1=კვი ... 7=შაბ
        int startIndex = (dayOfWeek + 5) % 7;

        Calendar tempCal = (Calendar) calendar.clone();
        for (int i = 0; i < 7; i++) {
            int index = (startIndex + i) % 7;
            int dayNumber = tempCal.get(Calendar.DAY_OF_MONTH);
            dayList.add(new DayItem(weekdays[index], dayNumber));
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        dayAdapter = new DayAdapter(dayList);
        rv_days.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_days.setAdapter(dayAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv_days);


        RecyclerView rv_lectures = view.findViewById(R.id.rv_lectures);


        List<LectureItem> lectureList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        lectureList.add(new LectureItem(
                1,
                "მათემატიკა",
                "10:00",
                "11:30",
                "Auditorium 201",
                "მიხეილ გურული",
                1,
                cal.getTimeInMillis(),
                0
        ));

        lectureList.add(new LectureItem(
                2,
                "ფიზიკა",
                "12:00",
                "13:30",
                "Auditorium 305",
                "ელენე კვერნაძე",
                1,
                cal.getTimeInMillis(),
                0
        ));

        lectureList.add(new LectureItem(
                3,
                "ქიმია",
                "14:00",
                "15:30",
                "Lab 2",
                "ნიკა ბარბაქაძე",
                2,
                cal.getTimeInMillis(),
                0
        ));

        LectureAdapter lectureAdapter = new LectureAdapter(lectureList);
        rv_lectures.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_lectures.setAdapter(lectureAdapter);
    }
}
