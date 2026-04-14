# 智能学习管理程序源码核对记录

## 1. 核对结论

截至 2026-04-14，`courseapp` 当前源码与现有答辩文档整体口径**基本一致**，未发现需要大范围回改文档或源码的结构性冲突。

当前可以稳定讲清的主线是：

1. 首次启动先判断是否完成引导
2. 首次用户进入引导页填写基础参数
3. 进入首页后可跳转任务、专注、统计、设置
4. 首页可导出 CSV 并通过系统分享

## 2. 页面流程与实际实现

### 2.1 启动与引导

- 启动入口是 `HomeActivity`
- `HomeActivity.onCreate()` 中先通过 `PreferenceManager.isOnboardingDone()` 判断是否完成引导
- 如果未完成，则跳转到 `OnboardingActivity`
- `OnboardingActivity.saveAndGoHome()` 负责读取昵称、每日目标、默认专注时长，做合法性校验后保存到 `SharedPreferences`

### 2.2 首页

- 首页仍然是主菜单页
- `HomeActivity.onCreate()` 中为任务、专注、统计、设置、导出按钮分别绑定点击事件
- `HomeActivity.onResume()` 中刷新首页摘要，包括昵称、每日目标、默认专注、累计专注和任务统计

### 2.3 任务管理

- `TaskActivity.saveTask()` 负责读取任务标题、分类、预计时长并做输入校验
- 合法数据通过 `AppDatabaseHelper.insertTask(...)` 写入 SQLite
- `TaskActivity.loadTasks()` 负责从数据库查询列表，并刷新 RecyclerView
- `TaskActivity.markTaskDone(...)` 负责把任务状态改成 `DONE`

### 2.4 专注计时

- `FocusActivity.startFocus()` 负责读取专注分钟数、检查是否重复启动、做范围校验
- `FocusActivity.startTimer()` 使用 `CountDownTimer` 每秒刷新界面
- `FocusActivity.endFocus()` 支持手动结束专注
- `FocusActivity.saveSession(...)` 负责保存一次专注记录到 `focus_session` 表

### 2.5 统计分析

- `StatsActivity.onResume()` 每次回到统计页时重新读取数据库汇总结果
- 汇总逻辑由 `AppDatabaseHelper.querySummary()` 提供
- 统计结果包括总任务数、已完成任务数、专注会话次数、累计专注时长

### 2.6 设置

- `SettingsActivity` 用于修改昵称、每日目标和默认专注时长
- `SettingsActivity.saveSettings()` 负责读取输入、做范围校验并重新保存到 `SharedPreferences`

### 2.7 CSV 导出

- 首页导出入口在 `HomeActivity.exportCsv()`
- `ExportUtils.exportSummaryCsv(...)` 负责生成 CSV 文本并写入本地文件
- `ExportUtils.createShareIntent(...)` 负责通过 `FileProvider` 生成 `Uri` 并调起系统分享

## 3. 与答辩文档的一致性判断

当前以下说法可以继续保留：

1. 技术路线为 Java + Activity + XML + SQLite + SharedPreferences
2. 首次引导使用 `SharedPreferences` 保存轻量配置
3. 任务与专注记录使用 SQLite 保存
4. 统计页通过数据库汇总方法统一取数
5. CSV 导出通过 `FileProvider + Intent.ACTION_SEND` 分享

当前未发现以下类型的问题：

1. 文档中写了某个页面，但源码里没有
2. 文档中写了 CSV 导出，但源码里没有导出入口
3. 文档中写了设置页，但源码里没有保存逻辑

## 4. 答辩时可以直接这样说

### 4.1 老师问：首次启动为什么会先进引导页？

可以回答：

首页启动时先读取 `SharedPreferences`，判断用户是否完成首次引导；如果没有完成，就从 `HomeActivity` 跳转到 `OnboardingActivity`。

### 4.2 老师问：设置页和引导页为什么都能改基础参数？

可以回答：

引导页负责第一次录入，设置页负责后续修改，两者最终都调用同一个偏好存储类保存数据，这样逻辑统一，也容易讲清楚。

### 4.3 老师问：任务和统计是怎么关联起来的？

可以回答：

任务先写入 SQLite，统计页不自己手工累加，而是通过数据库汇总函数统一查询总任务数和已完成任务数。

### 4.4 老师问：专注计时是怎么实现的？

可以回答：

我用的是 Android 原生的 `CountDownTimer`，开始时记录计划时长和开始时间，每秒刷新界面，手动结束或自动结束后都把记录写入数据库。

### 4.5 老师问：CSV 导出具体做了什么？

可以回答：

先把首页汇总数据拼成 CSV 文本，再写成文件，最后通过 `FileProvider` 转成可分享的 `Uri`，调用系统分享面板导出。

## 5. 当前仍待补的验证

1. 还没有做完整的页面级手工演示记录
2. 还没有把“老师高频追问的速答版”收成最终一页
