# 智能学习管理程序答辩高频问答

## 1. 项目用了什么技术？

答：

我主要用了 Java、Activity、XML、ViewBinding、SQLiteOpenHelper、SharedPreferences、RecyclerView、CountDownTimer 和 FileProvider。

## 2. 为什么这样设计？

答：

因为课程更强调 Java 原生开发，所以我选择了传统 Android 技术路线，页面结构和数据流程都比较直观，方便演示和答辩。

## 3. 为什么用 SharedPreferences？

答：

因为昵称、是否首次引导、默认专注时长这些属于轻量级配置，用 SharedPreferences 更简单。

## 4. 为什么用 SQLite？

答：

因为任务和专注记录属于结构化业务数据，需要做增删改查和统计，SQLite 更合适。

## 5. 为什么任务列表用 RecyclerView？

答：

因为任务是动态列表数据，RecyclerView 适合复用列表项视图，结构也更规范。

## 6. 专注计时怎么实现？

答：

我用了 `CountDownTimer`。开始时读取用户输入并校验，然后启动倒计时，每秒刷新界面，结束后保存到数据库。

## 7. 统计页怎么得到数据？

答：

统计页不是自己做计算，而是通过数据库层的 `querySummary()` 用 SQL 聚合查询得到结果。

## 8. 导出功能怎么实现？

答：

先把统计信息写成 CSV 文件，再通过 `FileProvider` 和 `Intent.ACTION_SEND` 分享出去。

## 9. 关键函数有哪些？

答：

我觉得最关键的是：

1. `saveAndGoHome()`
2. `saveTask()`
3. `loadTasks()`
4. `startFocus()`
5. `startTimer()`
6. `saveSession()`
7. `querySummary()`
8. `exportSummaryCsv()`

## 10. 你的项目难点是什么？

答：

我觉得难点主要有两个，一个是专注倒计时和数据保存的衔接，另一个是 CSV 文件导出和安全分享。

## 11. 你怎么做输入校验？

答：

我把输入校验放在各个 Activity 里，比如任务时长、专注分钟数、每日目标都有范围校验，非法输入会直接提示用户。

## 12. 你为什么把首页数据刷新放在 onResume？

答：

因为用户从其他页面返回首页时，任务或专注数据可能已经变了，放在 `onResume()` 里可以保证每次看到的都是最新结果。
