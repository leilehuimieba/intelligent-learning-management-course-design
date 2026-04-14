# CourseApp 关键函数清单

## 1. 首次引导与设置

### `PreferenceManager.isOnboardingDone()`

作用：

判断用户是否已经完成首次引导。

### `PreferenceManager.saveProfile(String nickname, int dailyTarget, int defaultFocus)`

作用：

保存昵称、每日目标和默认专注时长。

### `OnboardingActivity.saveAndGoHome()`

作用：

1. 读取用户输入
2. 做非空和范围校验
3. 保存配置
4. 跳转到首页

## 2. 任务管理

### `TaskActivity.saveTask()`

作用：

1. 读取任务输入
2. 校验任务标题和预计时长
3. 调用数据库插入任务
4. 刷新任务列表

### `TaskActivity.loadTasks()`

作用：

从数据库读取任务列表，并显示到 RecyclerView 上。

### `TaskActivity.markTaskDone(TaskItem taskItem)`

作用：

把指定任务状态改成 `DONE`，然后刷新页面。

### `AppDatabaseHelper.insertTask(...)`

作用：

把任务写入 `study_task` 表。

### `AppDatabaseHelper.queryTasks()`

作用：

从数据库中查询任务列表。

### `AppDatabaseHelper.markTaskDone(long taskId)`

作用：

更新任务状态为已完成。

## 3. 专注计时

### `FocusActivity.startFocus()`

作用：

1. 检查当前是否已有正在进行的专注
2. 读取输入分钟数
3. 校验范围
4. 启动倒计时

### `FocusActivity.startTimer()`

作用：

1. 创建 `CountDownTimer`
2. 每秒刷新时间显示
3. 到时间后自动保存记录

### `FocusActivity.endFocus()`

作用：

手动结束专注，并保存实际专注时长。

### `FocusActivity.saveSession(int actualMinutes)`

作用：

把当前专注记录写入数据库。

### `AppDatabaseHelper.insertSession(...)`

作用：

把一条专注记录插入 `focus_session` 表。

## 4. 统计分析

### `AppDatabaseHelper.querySummary()`

作用：

通过 SQL 聚合查询统计：

1. 总任务数
2. 已完成任务数
3. 专注次数
4. 累计专注时长

### `StatsActivity.onResume()`

作用：

每次进入统计页时都重新读取数据库中的最新统计结果。

## 5. CSV 导出

### `HomeActivity.exportCsv()`

作用：

点击首页按钮后导出 CSV 文件并调起分享。

### `ExportUtils.exportSummaryCsv(...)`

作用：

生成 CSV 文本并写入本地文件。

### `ExportUtils.createShareIntent(...)`

作用：

通过 `FileProvider` 把文件转成可分享的 `Uri`，再用系统分享面板发送。
