# 智能学习管理程序

> 一个面向课程设计展示、演示与答辩的 Android 学习管理应用项目。

## 项目概述

本项目用于完成“智能学习管理程序”课程设计，围绕学生日常学习过程中的几个核心场景，提供轻量、可解释、可演示的移动端实现。

当前仓库默认以 `courseapp` 作为唯一执行主线，目标是保持项目：

- 可构建
- 可演示
- 可答辩
- 可解释

## 功能特性

当前 `courseapp` 主线已具备以下基础功能：

- **首次使用引导**：保存昵称、每日目标、默认专注时长
- **任务管理**：进入任务页，查看和维护学习任务
- **专注计时**：支持学习专注流程展示
- **统计分析**：查看学习统计结果
- **参数设置**：修改基础配置项
- **CSV 导出**：导出周报数据并通过系统分享

## 项目截图

当前仓库尚未提交实际运行截图。为保证仓库首页结构完整，已预留截图展示区。

后续建议补充以下页面截图：

- 首页
- 首次使用引导页
- 任务管理页
- 专注计时页
- 统计分析页
- 参数设置页

建议将截图统一放在：

- `docs/assets/screenshots/`

补充截图后，可直接在此处替换为 Markdown 图片展示，例如：

```md
![首页截图](docs/assets/screenshots/home.png)
![任务页截图](docs/assets/screenshots/task.png)
```

## 技术栈

当前主线采用轻量实现，便于课程设计讲解与后续答辩：

- **语言**：Java
- **界面**：Activity + XML
- **本地存储**：SQLite + SharedPreferences
- **构建工具**：Gradle
- **平台**：Android

## 项目结构

```text
.
├─ courseapp/                 当前唯一开发主线
├─ docs/                      项目文档、阶段定义、change 工作区
├─ submit_package/            课程提交材料
├─ app/                       历史遗留模块（只读）
├─ core/                      历史遗留模块（只读）
├─ feature/                   历史遗留模块（只读）
├─ worker/                    历史遗留模块（只读）
└─ gradle/                    Gradle Wrapper 与版本配置
```

### 当前主线说明

- **当前唯一执行主线**：`courseapp`
- **历史遗留模块**：`app / core / feature / worker`
- **交付目录**：`submit_package/`

历史遗留模块保留用于参考，不作为当前默认推进对象；`submit_package/` 用于提交材料整理，不作为默认开发目录。

## 快速开始

### 环境要求

- Android Studio
- Android SDK
- JDK 17

### 克隆项目

```bash
git clone https://github.com/leilehuimieba/intelligent-learning-management-course-design.git
cd intelligent-learning-management-course-design
```

### 构建调试包

在仓库根目录执行：

```bash
./gradlew :courseapp:assembleDebug
```

Windows 下可执行：

```bash
gradlew.bat :courseapp:assembleDebug
```

### 运行主线模块

在 Android Studio 中打开项目后，优先选择 `courseapp` 模块进行构建、运行与检查。

## 文档导航

若你要继续阅读或推进项目，建议按以下顺序查看：

1. `docs/README.md`
2. `docs/stages/current.md`
3. `docs/changes/active.md`
4. 当前活跃 change 下的：
   - `proposal.md`
   - `design.md`
   - `tasks.md`
   - `status.md`
   - `verify.md`

若你是为了课程设计说明、老师检查或答辩准备，可优先查看：

- `docs/references/courseapp/07-courseapp-答辩说明.md`
- `docs/references/courseapp/10-courseapp-技术实现讲解.md`
- `docs/references/courseapp/11-courseapp-关键函数清单.md`
- `docs/references/courseapp/15-courseapp-手工演示清单.md`
- `docs/references/courseapp/16-courseapp-老师追问一页速答.md`

## 当前阶段

当前项目阶段为：**Phase 1：主线稳定**

当前阶段重点：

- 保持 `courseapp` 主线稳定
- 保持页面流程与答辩口径一致
- 通过最小 change 推进项目
- 为课程演示、答辩和提交做准备

## 开发约定

- 默认使用简体中文进行项目协作说明
- 中等以上任务优先进入 `docs/changes/` 工作区
- 完成实现后应同步更新 `tasks.md / status.md / verify.md`
- 未补验证记录前，不直接宣称任务完成

## 适用场景

本仓库当前更适合以下用途：

- 课程设计项目展示
- 课堂检查与演示
- 答辩准备
- 项目结构说明
- 轻量 Android 学习管理应用参考

## 说明

本项目当前定位是课程设计工程与答辩展示仓库，不以生产环境大型商业应用标准为目标。
