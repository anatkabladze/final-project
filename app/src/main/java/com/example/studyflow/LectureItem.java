package com.example.studyflow;

public class LectureItem {

    private int id;
    private String subject;
    private String startTime;
    private String endTime;
    private String room;
    private String teacher;
    private int dayOfWeek;
    private long dateMillis;
    private int notifyEnabled;
    private long notificationTime;

    public LectureItem(int id, String subject, String startTime, String endTime,
                       String room, String teacher, int dayOfWeek,
                       long dateMillis, int notifyEnabled,long notificationTime) {
        this.id = id;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.dateMillis = dateMillis;
        this.notifyEnabled = notifyEnabled;
        this.notificationTime = notificationTime;
    }
    public LectureItem(int id, String subject, String startTime, String endTime,
                       String room, String teacher, int dayOfWeek, long notificationTime) {
        this.id = id;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.notificationTime = notificationTime;
    }
    public LectureItem(int id, String subject, String startTime, String endTime,
                       String room, String teacher, int dayOfWeek) {
        this.id = id;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.notificationTime = 0;
    }


    // Getters
    public int getId() { return id; }
    public String getSubject() { return subject; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getRoom() { return room; }
    public String getTeacher() { return teacher; }
    public int getDayOfWeek() { return dayOfWeek; }
    public long getDateMillis() { return dateMillis; }
    public int getNotifyEnabled() { return notifyEnabled; }


    public void setId(int id) { this.id = id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setRoom(String room) { this.room = room; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setDateMillis(long dateMillis) { this.dateMillis = dateMillis; }
    public void setNotifyEnabled(int notifyEnabled) { this.notifyEnabled = notifyEnabled; }
    public long getNotificationTime() { return notificationTime; }
    public void setNotificationTime(long notificationTime) { this.notificationTime = notificationTime; }

}
