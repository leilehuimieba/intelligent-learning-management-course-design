package com.team.focusstudy.courseapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.team.focusstudy.courseapp.adapter.TaskAdapter;
import com.team.focusstudy.courseapp.data.AppDatabaseHelper;
import com.team.focusstudy.courseapp.databinding.ActivityTaskBinding;
import com.team.focusstudy.courseapp.model.TaskItem;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;
    private AppDatabaseHelper databaseHelper;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new AppDatabaseHelper(this);
        adapter = new TaskAdapter(this::markTaskDone);

        binding.rvTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTasks.setAdapter(adapter);

        binding.btnSaveTask.setOnClickListener(v -> saveTask());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void saveTask() {
        String title = binding.etTaskTitle.getText().toString().trim();
        String category = binding.etTaskCategory.getText().toString().trim();
        String minutesText = binding.etTaskMinutes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入任务标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(minutesText)) {
            Toast.makeText(this, "请输入预计时长", Toast.LENGTH_SHORT).show();
            return;
        }

        int minutes;
        try {
            minutes = Integer.parseInt(minutesText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入合法数字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (minutes < 5 || minutes > 600) {
            Toast.makeText(this, "预计时长需在 5-600 分钟之间", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.insertTask(title, category, minutes);
        binding.etTaskTitle.setText("");
        binding.etTaskCategory.setText("");
        binding.etTaskMinutes.setText("");
        Toast.makeText(this, "任务创建成功", Toast.LENGTH_SHORT).show();
        loadTasks();
    }

    private void loadTasks() {
        List<TaskItem> items = databaseHelper.queryTasks();
        adapter.submitList(items);
        binding.tvTaskEmpty.setVisibility(items.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void markTaskDone(TaskItem taskItem) {
        databaseHelper.markTaskDone(taskItem.getId());
        Toast.makeText(this, "任务已标记完成", Toast.LENGTH_SHORT).show();
        loadTasks();
    }
}
