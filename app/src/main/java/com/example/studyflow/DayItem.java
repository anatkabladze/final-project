package com.example.studyflow;

public class DayItem {
    private String dayName;
    private int dayNumber;

    public DayItem(String dayName, int dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
    }

    public String getDayName() {
        return dayName;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}