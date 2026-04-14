package com.team.focusstudy.courseapp.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.focusstudy.courseapp.data.AppDatabaseHelper;
import com.team.focusstudy.courseapp.databinding.ActivityFocusBinding;
import com.team.focusstudy.courseapp.util.PreferenceManager;

import java.util.Locale;

public class FocusActivity extends AppCompatActivity {

    private ActivityFocusBinding binding;
    private AppDatabaseHelper databaseHelper;
    private PreferenceManager preferenceManager;
    private CountDownTimer countDownTimer;
    private int plannedMinutes;
    private long startAt;
    private long remainingMillis;
    private boolean running;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFocusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new AppDatabaseHelper(this);
        preferenceManager = new PreferenceManager(this);
        plannedMinutes = preferenceManager.getDefaultFocus();
        remainingMillis = plannedMinutes * 60L * 1000L;
        binding.etFocusMinutes.setText(String.valueOf(plannedMinutes));
        renderTime(remainingMillis);

        binding.btnStartFocus.setOnClickListener(v -> startFocus());
        binding.btnEndFocus.setOnClickListener(v -> endFocus());
    }

    private void startFocus() {
        if (running) {
            Toast.makeText(this, "当前已有进行中的专注", Toast.LENGTH_SHORT).show();
            return;
        }

        String minuteText = binding.etFocusMinutes.getText().toString().trim();
        if (TextUtils.isEmpty(minuteText)) {
            minuteText = "25";
        }

        try {
            plannedMinutes = Integer.parseInt(minuteText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入合法分钟数", Toast.LENGTH_SHORT).show();
            return;
        }

        if (plannedMinutes < 5 || plannedMinutes > 180) {
            Toast.makeText(this, "专注时长需在 5-180 分钟之间", Toast.LENGTH_SHORT).show();
            return;
        }

        startAt = System.currentTimeMillis();
        remainingMillis = plannedMinutes * 60L * 1000L;
        running = true;
        startTimer();
        Toast.makeText(this, "专注已开始", Toast.LENGTH_SHORT).show();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(remainingMillis, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                renderTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingMillis = 0L;
                renderTime(0L);
                saveSession(plannedMinutes);
                running = false;
                Toast.makeText(FocusActivity.this, "专注完成，已保存记录", Toast.LENGTH_SHORT).show();
            }
        };
        countDownTimer.start();
    }

    private void endFocus() {
        if (!running) {
            Toast.makeText(this, "当前没有进行中的专注", Toast.LENGTH_SHORT).show();
            return;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        long elapsedMillis = System.currentTimeMillis() - startAt;
        int actualMinutes = (int) Math.max(1L, elapsedMillis / 60000L);
        saveSession(actualMinutes);
        running = false;
        remainingMillis = preferenceManager.getDefaultFocus() * 60L * 1000L;
        renderTime(remainingMillis);
        Toast.makeText(this, "专注已结束，记录已保存", Toast.LENGTH_SHORT).show();
    }

    private void saveSession(int actualMinutes) {
        long endAt = System.currentTimeMillis();
        databaseHelper.insertSession(plannedMinutes, actualMinutes, startAt, endAt);
    }

    private void renderTime(long millis) {
        long totalSeconds = millis / 1000L;
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        binding.tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
