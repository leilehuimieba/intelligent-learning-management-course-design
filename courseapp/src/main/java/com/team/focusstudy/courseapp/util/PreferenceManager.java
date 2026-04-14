package com.team.focusstudy.courseapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "course_focus_prefs";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_DAILY_TARGET = "daily_target";
    private static final String KEY_DEFAULT_FOCUS = "default_focus";

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isOnboardingDone() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_DONE, false);
    }

    public void saveProfile(String nickname, int dailyTarget, int defaultFocus) {
        sharedPreferences.edit()
                .putBoolean(KEY_ONBOARDING_DONE, true)
                .putString(KEY_NICKNAME, nickname)
                .putInt(KEY_DAILY_TARGET, dailyTarget)
                .putInt(KEY_DEFAULT_FOCUS, defaultFocus)
                .apply();
    }

    public String getNickname() {
        return sharedPreferences.getString(KEY_NICKNAME, "学习者");
    }

    public int getDailyTarget() {
        return sharedPreferences.getInt(KEY_DAILY_TARGET, 120);
    }

    public int getDefaultFocus() {
        return sharedPreferences.getInt(KEY_DEFAULT_FOCUS, 25);
    }
}
