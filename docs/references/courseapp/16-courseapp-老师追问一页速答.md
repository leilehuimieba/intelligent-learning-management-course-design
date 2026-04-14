# 智能学习管理程序老师追问一页速答

## 1. 项目一句话

这是一个用 **Java + Activity + XML + SQLite** 做的智能学习管理程序，重点是满足课程考核要求，功能链路完整，而且比较容易讲清楚。

## 2. 老师最可能追问的 12 个问题

### 1）你这个项目用了什么技术？

答：

我主要用了 Java、Activity、XML、ViewBinding、SharedPreferences、SQLiteOpenHelper、RecyclerView、CountDownTimer 和 FileProvider。

---

### 2）为什么不用更复杂的框架？

答：

因为这次课程考核更看重原生 Android 开发基础，所以我故意选了传统技术路线。这样页面跳转、数据存储和功能实现都更直观，老师也更容易检查。

---

### 3）为什么用 SharedPreferences？

答：

因为昵称、每日目标、默认专注时长这些都属于轻量配置数据，没有必要单独建表，用 SharedPreferences 保存更简单。

---

### 4）为什么用 SQLite？

答：

因为任务和专注记录是结构化业务数据，后面要做新增、查询、更新和统计，所以 SQLite 更合适。

---

### 5）你的页面流程是什么？

答：

启动后先判断有没有完成首次引导，没有就进入引导页；保存基础信息后进入首页；首页再跳转到任务、专注、统计和设置页面，最后还能从首页导出 CSV。

---

### 6）首次引导是怎么实现的？

答：

首页启动时先调用 `PreferenceManager.isOnboardingDone()` 判断是否完成引导，如果没有完成，就跳到 `OnboardingActivity`；引导页保存成功后再进入首页。

---

### 7）设置页和引导页有什么区别？

答：

引导页是第一次进入应用时录入基础参数，设置页是后续修改这些参数。两者最终都保存到同一个偏好存储类里，所以逻辑是统一的。

---

### 8）任务管理是怎么做的？

答：

任务页先读取输入内容并做合法性校验，然后调用数据库方法把任务写入 `study_task` 表，再通过 RecyclerView 把任务列表展示出来。

---

### 9）专注计时是怎么做的？

答：

我用的是原生 `CountDownTimer`。开始专注时先读取分钟数并校验，倒计时过程中每秒刷新界面，结束后把计划时长和实际时长写入数据库。

---

### 10）统计页的数据是怎么算出来的？

答：

统计页不是在页面里自己手算，而是直接调用数据库层的 `querySummary()`，通过 SQL 聚合统一查询总任务数、已完成任务数、专注次数和累计专注时长。

---

### 11）CSV 导出怎么实现的？

答：

先把首页的汇总数据拼成 CSV 文本，再写成文件，最后通过 `FileProvider` 生成可分享的 `Uri`，用系统分享面板导出。

---

### 12）你项目里最关键的几个函数是什么？

答：

我认为最关键的是：

1. `OnboardingActivity.saveAndGoHome()`
2. `TaskActivity.saveTask()`
3. `TaskActivity.loadTasks()`
4. `FocusActivity.startFocus()`
5. `FocusActivity.startTimer()`
6. `FocusActivity.saveSession()`
7. `AppDatabaseHelper.querySummary()`
8. `ExportUtils.exportSummaryCsv()`

## 3. 如果老师继续追问，你可以这样接

### 老师问：你的难点是什么？

答：

我觉得主要有两个，一个是专注倒计时和保存记录之间的衔接，另一个是 CSV 文件生成后怎么安全分享出去。

### 老师问：你怎么做输入校验？

答：

我把校验放在各个 Activity 里，比如昵称非空、分钟数必须在合理范围内，非法输入就直接 Toast 提示。

### 老师问：为什么首页和统计页都用 onResume 刷新？

答：

因为用户从其他页面返回时，任务数和专注数据可能已经变化了，放在 `onResume()` 里能保证页面每次回来都显示最新结果。

## 4. 最后 20 秒总结模板

你可以直接背这段：

这个项目是一个面向课程考核的原生 Android 智能学习管理程序，我主要用了 Java、Activity、XML、SharedPreferences 和 SQLite。  
它实现了首次引导、任务管理、专注计时、统计分析、设置和 CSV 导出。  
项目重点不是复杂架构，而是把页面流程、数据存储和关键函数讲清楚，满足课程对原生开发和数据库应用的要求。
