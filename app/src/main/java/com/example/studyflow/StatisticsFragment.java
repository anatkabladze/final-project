package com.example.studyflow;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter; // შეცვლილია აქ

import java.util.ArrayList;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private TextView tvTotal, tvCompleted, tvRemaining, tvCompletedPct, tvRemainingPct;
    private TextView tvOnTime, tvOverdue, tvOnTimePct, tvOverduePct;
    private PieChart pieProgress, pieOverdue;
    private BarChart barPriority;
    private DB db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DB(getContext());




        tvTotal = view.findViewById(R.id.tv_total_count);
        tvCompleted = view.findViewById(R.id.tv_completed_count);
        tvRemaining = view.findViewById(R.id.tv_remaining_count);
        tvCompletedPct = view.findViewById(R.id.tv_completed_percent);
        tvRemainingPct = view.findViewById(R.id.tv_remaining_percent);

        tvOnTime = view.findViewById(R.id.tv_on_time_count);
        tvOverdue = view.findViewById(R.id.tv_overdue_count);
        tvOnTimePct = view.findViewById(R.id.tv_on_time_percent);
        tvOverduePct = view.findViewById(R.id.tv_overdue_percent);

        pieProgress = view.findViewById(R.id.pieChartProgress);
        pieOverdue = view.findViewById(R.id.pieChartOverdue);
        barPriority = view.findViewById(R.id.barChartPriority);
        displayStatistics();

    }

    private void displayStatistics() {
        int total = db.getTotalTasksCount();
        int completed = db.getCompletedTasksCount();
        int remaining = total - completed;
        int overdue = db.getOverdueTasksCount();
        int onTime = db.getOnTimePendingTasksCount();

        tvTotal.setText(String.valueOf(total));
        tvCompleted.setText(String.valueOf(completed));
        tvRemaining.setText(String.valueOf(remaining));
        tvOnTime.setText(String.valueOf(onTime));
        tvOverdue.setText(String.valueOf(overdue));

        if (total > 0) {
            tvCompletedPct.setText(String.format(Locale.getDefault(), "%.1f%%", (completed * 100f / total)));
            tvRemainingPct.setText(String.format(Locale.getDefault(), "%.1f%%", (remaining * 100f / total)));
        } else {
            tvCompletedPct.setText("0%");
            tvRemainingPct.setText("0%");
        }

        if (remaining > 0) {
            tvOnTimePct.setText(String.format(Locale.getDefault(), "%.1f%%", (onTime * 100f / remaining)));
            tvOverduePct.setText(String.format(Locale.getDefault(), "%.1f%%", (overdue * 100f / remaining)));
        } else {
            tvOnTimePct.setText("0%");
            tvOverduePct.setText("0%");
        }

        setupPieChart(pieProgress, completed, remaining, "პროგრესი", new int[]{Color.parseColor("#4CAF50"), Color.parseColor("#BDBDBD")});
        setupPieChart(pieOverdue, onTime, overdue, "სტატუსი", new int[]{Color.parseColor("#2196F3"), Color.parseColor("#F44336")});
        setupPriorityBarChart();
    }

    private void setupPieChart(PieChart pie, int val1, int val2, String label, int[] colors) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(val1, ""));
        entries.add(new PieEntry(val2, ""));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(3f);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        pie.setData(data);;

        pie.setCenterText(label);
        pie.setCenterTextSize(16f);
        pie.setHoleRadius(50f);
        pie.setTransparentCircleRadius(75f);
        pie.getDescription().setEnabled(false);
        pie.getLegend().setEnabled(false);

        pie.animateY(1000);
        pie.invalidate();
    }

    private void setupPriorityBarChart() {
        int high = db.getTasksCountByPriority(1);
        int medium = db.getTasksCountByPriority(2);
        int low = db.getTasksCountByPriority(3);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, high));
        entries.add(new BarEntry(1f, medium));
        entries.add(new BarEntry(2f, low));

        BarDataSet dataSet = new BarDataSet(entries, "პრიორიტეტი");
        dataSet.setColors(new int[]{
                Color.parseColor("#EF5350"),
                Color.parseColor("#FFCA28"),
                Color.parseColor("#66BB6A")
        });
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barPriority.setData(data);

        final String[] labels = new String[]{"High", "Medium", "Low"};
        XAxis xAxis = barPriority.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < labels.length) {
                    return labels[(int) value];
                }
                return "";
            }
        });

        barPriority.getAxisLeft().setGranularity(1f);
        barPriority.getAxisRight().setEnabled(false);
        barPriority.getDescription().setEnabled(false);
        barPriority.animateY(1000);
        barPriority.invalidate();
    }
}