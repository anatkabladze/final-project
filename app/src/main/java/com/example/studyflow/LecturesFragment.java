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
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LecturesFragment extends Fragment {

    private RecyclerView rv_days;
    private DayAdapter dayAdapter;
    private List<DayItem> dayList;
    private DB db;
    private LectureAdapter lectureAdapter;
    private RecyclerView rv_lectures;
    private int selectedDay = 0;
    private TextView tv_free_day;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lecture_fragment_layout, container, false);
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
        rv_lectures = view.findViewById(R.id.rv_lectures);
        rv_lectures.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_free_day = view.findViewById(R.id.tv_free_day);

        db = new DB(getContext());


        String[] weekdays = {"ორშ","სამშ","ოთხ","ხუთ","პარ","შაბ","კვი"};

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int startIndex = (dayOfWeek + 5) % 7;

        Calendar tempCal = (Calendar) calendar.clone();
        int todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int diffToMonday = (Calendar.MONDAY - todayDayOfWeek + 7) % 7;

        //ორშაბათიდან
        tempCal.add(Calendar.DAY_OF_MONTH, diffToMonday);


        for (int i = 0; i < 7; i++) {
            int dayNumber = tempCal.get(Calendar.DAY_OF_MONTH);
            dayList.add(new DayItem(weekdays[i], dayNumber));
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
//ინტერფეისის ობიექტი
        DayAdapter.OnDayClickListener listener = new DayAdapter.OnDayClickListener() {
            @Override
            public void onDayClick(int dayIndex) {
                selectedDay = dayIndex;
                loadLecturesForDay(dayIndex);
            }
        };
        dayAdapter = new DayAdapter(dayList, listener);


        rv_days.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_days.setAdapter(dayAdapter);


        int todayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7; // 0=ორშ, 1=სამ...
        selectedDay = todayIndex;
        dayAdapter.setSelectedDay(todayIndex);


            dayAdapter.setSelectedDay(todayIndex);
            rv_days.scrollToPosition(todayIndex);




        loadLecturesForDay(todayIndex);

    }
    private void loadLecturesForDay(int dayIndex) {
        int dayOfWeekForDB = dayIndex + 1;
        List<LectureItem> lectures = db.getLecturesByDay(dayOfWeekForDB);
        if (lectures == null || lectures.isEmpty()) {

            rv_lectures.setVisibility(View.GONE);
            tv_free_day.setVisibility(View.VISIBLE);
        } else {

            tv_free_day.setVisibility(View.GONE);
            rv_lectures.setVisibility(View.VISIBLE);

            lectureAdapter = new LectureAdapter(lectures);
            rv_lectures.setAdapter(lectureAdapter);
        }



    }
}
