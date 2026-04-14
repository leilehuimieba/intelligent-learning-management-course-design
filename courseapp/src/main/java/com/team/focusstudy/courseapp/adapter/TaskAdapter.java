package com.team.focusstudy.courseapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team.focusstudy.courseapp.R;
import com.team.focusstudy.courseapp.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskDoneClickListener {
        void onTaskDone(TaskItem taskItem);
    }

    private final List<TaskItem> items = new ArrayList<>();
    private final OnTaskDoneClickListener onTaskDoneClickListener;

    public TaskAdapter(OnTaskDoneClickListener onTaskDoneClickListener) {
        this.onTaskDoneClickListener = onTaskDoneClickListener;
    }

    public void submitList(List<TaskItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvMeta.setText("分类：" + safeText(item.getCategory()) + "  预计：" + item.getEstimatedMinutes() + " 分钟  状态：" + item.getStatus());
        holder.btnDone.setEnabled(!"DONE".equals(item.getStatus()));
        holder.btnDone.setOnClickListener(v -> onTaskDoneClickListener.onTaskDone(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String safeText(String text) {
        return text == null || text.trim().isEmpty() ? "未分类" : text;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvMeta;
        Button btnDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvItemTitle);
            tvMeta = itemView.findViewById(R.id.tvItemMeta);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
