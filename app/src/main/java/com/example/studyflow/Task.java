package com.example.studyflow;


public class Task {

    private int id;
    private String title;
    private String description;
    private String lectureName;
    private long deadline;
    private int priority;
    private int isCompleted;
    private long createdAt;
    private Long completedAt;
    private long notificationTime;

    public Task(int id, String title, String description, String lectureName,
                long deadline, int priority, int isCompleted,
                long createdAt, Long completedAt, long notificationTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lectureName = lectureName;
        this.deadline = deadline;
        this.priority = priority;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.notificationTime = notificationTime;
    }

    public Task(String title, String description, String lectureName,
                long deadline, int priority, int notificationTime) {
        this.title = title;
        this.description = description;
        this.lectureName = lectureName;
        this.deadline = deadline;
        this.priority = priority;
        this.isCompleted = 0;
        this.createdAt = System.currentTimeMillis();
        this.completedAt = null;
        this.notificationTime = notificationTime;
    }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLectureName() { return lectureName; }
    public long getDeadline() { return deadline; }
    public int getPriority() { return priority; }
    public int getIsCompleted() { return isCompleted; }
    public long getCreatedAt() { return createdAt; }
    public Long getCompletedAt() { return completedAt; }
    public long getNotificationTime() { return notificationTime; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setLectureName(String lectureName) { this.lectureName = lectureName; }
    public void setDeadline(long deadline) { this.deadline = deadline; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setIsCompleted(int isCompleted) { this.isCompleted = isCompleted; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public void setCompletedAt(Long completedAt) { this.completedAt = completedAt; }
    public void setNotificationTime(long notificationTime) { this.notificationTime = notificationTime; }
}