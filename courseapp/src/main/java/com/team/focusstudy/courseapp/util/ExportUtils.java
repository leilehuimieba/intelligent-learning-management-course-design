package com.team.focusstudy.courseapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.team.focusstudy.courseapp.data.AppDatabaseHelper;
import com.team.focusstudy.courseapp.model.StatsSummary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExportUtils {

    private ExportUtils() {
    }

    public static File exportSummaryCsv(Context context, AppDatabaseHelper databaseHelper, PreferenceManager preferenceManager) throws IOException {
        File exportDir = new File(context.getExternalFilesDir(null), "exports");
        if (!exportDir.exists() && !exportDir.mkdirs()) {
            throw new IOException("导出目录创建失败");
        }

        String fileName = "focus-study-report-" + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(new Date()) + ".csv";
        File file = new File(exportDir, fileName);

        StatsSummary summary = databaseHelper.querySummary();
        StringBuilder builder = new StringBuilder();
        builder.append("昵称,每日目标,默认专注,任务总数,已完成任务,专注次数,累计专注时长\n");
        builder.append(preferenceManager.getNickname()).append(",")
                .append(preferenceManager.getDailyTarget()).append(",")
                .append(preferenceManager.getDefaultFocus()).append(",")
                .append(summary.getTotalTaskCount()).append(",")
                .append(summary.getCompletedTaskCount()).append(",")
                .append(summary.getSessionCount()).append(",")
                .append(summary.getTotalFocusMinutes()).append("\n");

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(builder.toString().getBytes(StandardCharsets.UTF_8));
        }

        return file;
    }

    public static Intent createShareIntent(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }
}
