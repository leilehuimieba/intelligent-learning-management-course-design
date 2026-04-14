package com.team.focusstudy.courseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.focusstudy.courseapp.data.AppDatabaseHelper;
import com.team.focusstudy.courseapp.databinding.ActivityHomeBinding;
import com.team.focusstudy.courseapp.model.StatsSummary;
import com.team.focusstudy.courseapp.util.ExportUtils;
import com.team.focusstudy.courseapp.util.PreferenceManager;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private AppDatabaseHelper databaseHelper;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(this);
        if (!preferenceManager.isOnboardingDone()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new AppDatabaseHelper(this);

        binding.btnTask.setOnClickListener(v -> startActivity(new Intent(this, TaskActivity.class)));
        binding.btnFocus.setOnClickListener(v -> startActivity(new Intent(this, FocusActivity.class)));
        binding.btnStats.setOnClickListener(v -> startActivity(new Intent(this, StatsActivity.class)));
        binding.btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        binding.btnExport.setOnClickListener(v -> exportCsv());
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatsSummary summary = databaseHelper.querySummary();
        binding.tvHomeSummary.setText("欢迎你，" + preferenceManager.getNickname() + "。每日目标：" + preferenceManager.getDailyTarget() + " 分钟，默认专注：" + preferenceManager.getDefaultFocus() + " 分钟");
        binding.tvTodayFocus.setText("累计专注：" + summary.getTotalFocusMinutes() + " 分钟");
        binding.tvTaskCount.setText("任务数量：" + summary.getTotalTaskCount() + "，已完成：" + summary.getCompletedTaskCount());
    }

    private void exportCsv() {
        try {
            File csvFile = ExportUtils.exportSummaryCsv(this, databaseHelper, preferenceManager);
            Intent shareIntent = ExportUtils.createShareIntent(this, csvFile);
            startActivity(Intent.createChooser(shareIntent, "分享 CSV 周报"));
        } catch (IOException e) {
            Toast.makeText(this, "导出失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
