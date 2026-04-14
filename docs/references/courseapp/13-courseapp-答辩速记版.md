# 智能学习管理程序答辩速记版

## 1. 一句话介绍

这是一个用 `Java + Activity + XML + SQLite` 实现的智能学习管理程序。

## 2. 功能

1. 首次引导
2. 任务管理
3. 专注计时
4. 统计分析
5. 设置参数
6. CSV 导出

## 3. 核心技术

1. `SharedPreferences` 保存配置
2. `SQLiteOpenHelper` 管理数据库
3. `RecyclerView` 展示任务列表
4. `CountDownTimer` 实现倒计时
5. `FileProvider` 实现文件分享

## 4. 关键函数

1. `saveAndGoHome()`
2. `saveTask()`
3. `loadTasks()`
4. `startFocus()`
5. `startTimer()`
6. `saveSession()`
7. `querySummary()`
8. `exportSummaryCsv()`

## 5. 最常用回答模板

这个功能我主要用了 `某个技术`，关键逻辑在 `某个类/函数`。  
实现流程是：先 `读取或校验`，再 `处理数据`，最后 `更新界面或保存数据库`。
