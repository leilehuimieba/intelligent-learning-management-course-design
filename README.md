# 智能学习管理程序

一个用于课程设计展示与答辩的 Android 学习管理应用项目仓库。

当前默认开发主线为 `courseapp`，目标是保持项目 **可构建、可演示、可答辩、可解释**。

## 项目简介

“智能学习管理程序”围绕学生日常学习过程中的几个核心场景展开：

- 首次使用引导
- 学习任务管理
- 专注计时
- 学习统计分析
- 参数设置
- CSV 数据导出

当前项目已建立文档化推进方式，后续中等以上任务默认通过 `docs/changes/` 中的 change 工作区推进。

## 当前主线说明

本仓库中包含两类内容：

### 1. 当前唯一执行主线
- `courseapp`

这是当前默认推进、默认构建核对、默认答辩说明所对应的主线。

### 2. 历史遗留模块（只读）
- `app`
- `core`
- `feature`
- `worker`

这些目录保留用于历史实现参考，不作为当前默认推进对象。

### 3. 交付产物目录
- `submit_package/`

该目录用于保存课程设计提交材料，不作为默认开发目录。

## 当前已实现能力

基于当前 `courseapp` 主线，项目已具备以下基础能力：

- 首次启动进入引导页，保存昵称、每日目标和默认专注时长
- 首页进入任务管理、专注计时、统计分析、设置等页面
- 设置页修改基础配置
- 导出 CSV 周报并通过系统分享

应用当前对外名称为：**智能学习管理程序**。

## 技术方案

当前主线保持轻量实现，便于课程设计讲解与答辩：

- 语言：Java
- UI：Activity + XML
- 数据存储：SQLite + SharedPreferences
- 构建工具：Gradle

## 仓库结构

```text
.
├─ courseapp/                 当前唯一开发主线
├─ docs/                      项目文档、阶段定义、change 工作区
├─ submit_package/            课程提交材料
├─ app/ core/ feature/ worker 历史遗留模块（只读）
└─ gradle/                    Gradle Wrapper 与版本配置
```

## 文档入口

继续阅读项目时，建议按以下顺序：

1. `docs/README.md`
2. `docs/stages/current.md`
3. `docs/changes/active.md`
4. 当前活跃 change 下的 `proposal.md / design.md / tasks.md / status.md / verify.md`

如果你是答辩准备或项目说明用途，也可以直接看：

- `docs/references/courseapp/07-courseapp-答辩说明.md`
- `docs/references/courseapp/10-courseapp-技术实现讲解.md`
- `docs/references/courseapp/11-courseapp-关键函数清单.md`
- `docs/references/courseapp/15-courseapp-手工演示清单.md`
- `docs/references/courseapp/16-courseapp-老师追问一页速答.md`

## 本地运行

### 环境建议

- Android Studio
- Android SDK
- JDK 17（与当前 Android Gradle Plugin / Gradle 配置保持兼容）

### 构建命令

在仓库根目录执行：

```bash
./gradlew :courseapp:assembleDebug
```

Windows 下可执行：

```bash
gradlew.bat :courseapp:assembleDebug
```

## 当前阶段

当前阶段为：**Phase 1：主线稳定**

当前阶段重点不是继续无序加功能，而是：

- 保持 `courseapp` 主线稳定
- 保持页面流程与答辩口径一致
- 通过最小 change 推进项目
- 为课程演示、答辩和提交做准备

## 仓库使用说明

- 若你只是查看课程设计成果，请优先阅读本页与 `docs/`
- 若你要继续推进项目，请不要默认改动历史遗留模块
- 若你要做中等以上改动，请先进入 `docs/changes/` 建立或续接 change

## 说明

本仓库当前更偏向于课程设计项目展示、说明与可答辩交付，而不是面向生产环境的大型商业应用工程。

