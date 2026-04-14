package com.team.focusstudy.courseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.focusstudy.courseapp.databinding.ActivityOnboardingBinding;
import com.team.focusstudy.courseapp.util.PreferenceManager;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        binding.etNickname.setText(preferenceManager.getNickname());
        binding.etDailyTarget.setText(String.valueOf(preferenceManager.getDailyTarget()));
        binding.etDefaultFocus.setText(String.valueOf(preferenceManager.getDefaultFocus()));

        binding.btnEnterApp.setOnClickListener(v -> saveAndGoHome());
    }

    private void saveAndGoHome() {
        String nickname = binding.etNickname.getText().toString().trim();
        String dailyText = binding.etDailyTarget.getText().toString().trim();
        String focusText = binding.etDefaultFocus.getText().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(dailyText) || TextUtils.isEmpty(focusText)) {
            Toast.makeText(this, "请完整填写目标参数", Toast.LENGTH_SHORT).show();
            return;
        }

        int dailyTarget;
        int defaultFocus;
        try {
            dailyTarget = Integer.parseInt(dailyText);
            defaultFocus = Integer.parseInt(focusText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入合法数字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dailyTarget < 30 || dailyTarget > 720) {
            Toast.makeText(this, "每日目标需在 30-720 分钟之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (defaultFocus < 5 || defaultFocus > 180) {
            Toast.makeText(this, "默认专注时长需在 5-180 分钟之间", Toast.LENGTH_SHORT).show();
            return;
        }

        preferenceManager.saveProfile(nickname, dailyTarget, defaultFocus);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
