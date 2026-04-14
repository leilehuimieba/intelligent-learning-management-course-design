# CourseApp 技术实现讲解

## 1. 项目定位

`courseapp` 是本项目的课程答辩版本，技术路线是：

1. Java
2. Activity
3. XML 布局
4. SQLite
5. SharedPreferences

这个版本重点不是做复杂架构，而是做一个老师容易检查、学生也容易讲清楚的原生 Android 智能学习管理程序。

## 2. 页面结构

项目主要页面如下：

1. `OnboardingActivity`
2. `HomeActivity`
3. `TaskActivity`
4. `FocusActivity`
5. `StatsActivity`
6. `SettingsActivity`

页面之间通过 `Intent` 跳转，符合传统 Android 开发方式。

## 3. 数据存储设计

### 3.1 SharedPreferences

用于保存轻量级配置数据：

1. 是否完成首次引导
2. 用户昵称
3. 每日目标分钟数
4. 默认专注时长

原因是这类数据结构简单，不适合单独建表。

### 3.2 SQLite

用于保存核心业务数据：

1. 任务数据表 `study_task`
2. 专注记录表 `focus_session`

数据库实现放在 `AppDatabaseHelper` 中，使用 `SQLiteOpenHelper` 完成建表、插入、查询和更新。

## 4. 主要功能实现

### 4.1 首次引导

首页启动时先判断 `SharedPreferences` 中是否已经完成引导。  
如果没有完成，就跳转到 `OnboardingActivity`。

### 4.2 任务管理

任务页支持：

1. 输入任务标题
2. 输入任务分类
3. 输入预计时长
4. 保存到数据库
5. 在 RecyclerView 中展示
6. 点击按钮标记完成

### 4.3 专注计时

专注页使用 `CountDownTimer` 实现倒计时：

1. 输入专注分钟数
2. 开始倒计时
3. 每秒刷新界面
4. 手动结束或自动结束后写入数据库

### 4.4 统计分析

统计页不自己做复杂计算，而是直接调用数据库汇总方法 `querySummary()`，读取：

1. 累计专注时长
2. 已完成任务数
3. 总任务数
4. 专注会话次数

### 4.5 CSV 导出

导出功能通过 `ExportUtils` 实现：

1. 先生成 CSV 文本
2. 再写入到本地文件
3. 最后通过 `FileProvider + Intent.ACTION_SEND` 分享

## 5. 关键技术点

### 5.1 ViewBinding

每个页面都使用 ViewBinding 获取控件，不再手写大量 `findViewById`，代码更清晰。

### 5.2 RecyclerView

任务列表使用 RecyclerView 展示，配合 `TaskAdapter` 实现数据绑定。

### 5.3 CountDownTimer

专注页使用原生倒计时工具完成每秒刷新，逻辑简单，答辩也容易解释。

### 5.4 SQL 聚合

统计页的数据不是手算的，而是通过 SQL 的 `COUNT` 和 `SUM` 汇总得到，体现了数据库查询能力。

## 6. 可以主动强调的优点

1. 技术路线符合 Java 原生 Android 课程考核口径。
2. 页面结构清晰，Activity 分工明确。
3. 使用了本地数据库，实现了真实的增删改查和统计。
4. 支持 CSV 导出，完整性更强。
