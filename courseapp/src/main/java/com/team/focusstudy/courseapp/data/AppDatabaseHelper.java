package com.team.focusstudy.courseapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.team.focusstudy.courseapp.model.StatsSummary;
import com.team.focusstudy.courseapp.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "focus_study_course.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASK = "study_task";
    public static final String TABLE_SESSION = "focus_session";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASK + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "category TEXT," +
                "estimated_minutes INTEGER NOT NULL," +
                "status TEXT NOT NULL," +
                "created_at INTEGER NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE " + TABLE_SESSION + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "planned_minutes INTEGER NOT NULL," +
                "actual_minutes INTEGER NOT NULL," +
                "started_at INTEGER NOT NULL," +
                "ended_at INTEGER NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    public long insertTask(String title, String category, int estimatedMinutes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("category", category);
        values.put("estimated_minutes", estimatedMinutes);
        values.put("status", "TODO");
        values.put("created_at", System.currentTimeMillis());
        return db.insert(TABLE_TASK, null, values);
    }

    public List<TaskItem> queryTasks() {
        List<TaskItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASK, null, null, null, null, null, "id DESC");
        try {
            while (cursor.moveToNext()) {
                TaskItem item = new TaskItem();
                item.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                item.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                item.setEstimatedMinutes(cursor.getInt(cursor.getColumnIndexOrThrow("estimated_minutes")));
                item.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                item.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow("created_at")));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public void markTaskDone(long taskId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "DONE");
        db.update(TABLE_TASK, values, "id = ?", new String[]{String.valueOf(taskId)});
    }

    public void insertSession(int plannedMinutes, int actualMinutes, long startedAt, long endedAt) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("planned_minutes", plannedMinutes);
        values.put("actual_minutes", actualMinutes);
        values.put("started_at", startedAt);
        values.put("ended_at", endedAt);
        db.insert(TABLE_SESSION, null, values);
    }

    public StatsSummary querySummary() {
        StatsSummary summary = new StatsSummary();
        SQLiteDatabase db = getReadableDatabase();

        Cursor taskCursor = db.rawQuery(
                "SELECT COUNT(*) AS total_count, " +
                        "SUM(CASE WHEN status='DONE' THEN 1 ELSE 0 END) AS done_count " +
                        "FROM " + TABLE_TASK,
                null
        );
        try {
            if (taskCursor.moveToFirst()) {
                summary.setTotalTaskCount(taskCursor.getInt(taskCursor.getColumnIndexOrThrow("total_count")));
                summary.setCompletedTaskCount(taskCursor.getInt(taskCursor.getColumnIndexOrThrow("done_count")));
            }
        } finally {
            taskCursor.close();
        }

        Cursor focusCursor = db.rawQuery(
                "SELECT COUNT(*) AS session_count, COALESCE(SUM(actual_minutes), 0) AS total_minutes FROM " + TABLE_SESSION,
                null
        );
        try {
            if (focusCursor.moveToFirst()) {
                summary.setSessionCount(focusCursor.getInt(focusCursor.getColumnIndexOrThrow("session_count")));
                summary.setTotalFocusMinutes(focusCursor.getInt(focusCursor.getColumnIndexOrThrow("total_minutes")));
            }
        } finally {
            focusCursor.close();
        }

        return summary;
    }
}
