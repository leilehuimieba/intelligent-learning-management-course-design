package com.team.focusstudy.courseapp.model;

public class StatsSummary {
    private int totalFocusMinutes;
    private int completedTaskCount;
    private int sessionCount;
    private int totalTaskCount;

    public int getTotalFocusMinutes() {
        return totalFocusMinutes;
    }

    public void setTotalFocusMinutes(int totalFocusMinutes) {
        this.totalFocusMinutes = totalFocusMinutes;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public int getTotalTaskCount() {
        return totalTaskCount;
    }

    public void setTotalTaskCount(int totalTaskCount) {
        this.totalTaskCount = totalTaskCount;
    }
}
