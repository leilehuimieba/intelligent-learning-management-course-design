package com.team.focusstudy.courseapp.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.focusstudy.courseapp.data.AppDatabaseHelper;
import com.team.focusstudy.courseapp.databinding.ActivityStatsBinding;
import com.team.focusstudy.courseapp.model.StatsSummary;

public class StatsActivity extends AppCompatActivity {

    private ActivityStatsBinding binding;
    private AppDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new AppDatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatsSummary summary = databaseHelper.querySummary();
        binding.tvStatSummary.setText("总专注时长：" + summary.getTotalFocusMinutes() + " 分钟");
        binding.tvStatTask.setText("已完成任务：" + summary.getCompletedTaskCount() + " / " + summary.getTotalTaskCount());
        binding.tvStatSession.setText("专注会话次数：" + summary.getSessionCount());
    }
}
