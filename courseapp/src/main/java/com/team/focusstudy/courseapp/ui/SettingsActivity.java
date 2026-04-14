package com.team.focusstudy.courseapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.team.focusstudy.courseapp.databinding.ActivitySettingsBinding;
import com.team.focusstudy.courseapp.util.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        binding.etSettingsNickname.setText(preferenceManager.getNickname());
        binding.etSettingsDailyTarget.setText(String.valueOf(preferenceManager.getDailyTarget()));
        binding.etSettingsFocus.setText(String.valueOf(preferenceManager.getDefaultFocus()));

        binding.btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        String nickname = binding.etSettingsNickname.getText().toString().trim();
        String dailyText = binding.etSettingsDailyTarget.getText().toString().trim();
        String focusText = binding.etSettingsFocus.getText().toString().trim();

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(dailyText) || TextUtils.isEmpty(focusText)) {
            Toast.makeText(this, "请完整填写所有字段", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
        finish();
    }
}
