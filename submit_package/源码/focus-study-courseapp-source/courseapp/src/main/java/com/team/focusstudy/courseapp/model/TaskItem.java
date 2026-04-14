package com.team.focusstudy.courseapp.model;

public class TaskItem {
    private long id;
    private String title;
    private String category;
    private int estimatedMinutes;
    private String status;
    private long createdAt;

    public TaskItem() {
    }

    public TaskItem(long id, String title, String category, int estimatedMinutes, String status, long createdAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.estimatedMinutes = estimatedMinutes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
