# 项目路线图

## 1. 项目名称
智能学习管理程序课程审核版（`courseapp`）

## 2. 一句话目标
为《Android开发技术》课程审核与答辩提供一个易讲清、易演示、符合 Java 原生开发口径的智能学习管理程序。

## 3. 当前唯一主线
- 当前唯一开发主线：`courseapp`
- 历史遗留：`app / core / feature / worker`
- 交付目录：`submit_package/`

## 4. 阶段列表

### Phase 0：规范收敛
- 目标：建立唯一执行入口、阶段口径、change 工作区和 AGENTS 协作规则
- 输入：现有 `courseapp` 源码与 07-13 文档
- 交付物：`docs/README.md`、`docs/roadmap.md`、`docs/stages/current.md`、`docs/changes/*`、`AGENTS.md`
- Gate：AI 能明确当前主线、当前阶段、当前活跃 change，并按统一规则推进

### Phase 1：主线稳定
- 目标：在 `courseapp` 主线上保持可编译、可解释、可答辩
- 输入：规范底盘已建立
- 交付物：稳定的 `courseapp`、补充说明文档、必要验证记录
- Gate：主线可构建，可说明关键技术点，可解释主要函数

### Phase 2：增量演进
- 目标：在 change 机制下补中小功能或优化体验
- 输入：已稳定主线
- 交付物：业务 change、验证证据、更新后的提交包
- Gate：每次改动可追踪、可验证、可回退

### Phase 3：交付与归档
- 目标：收口文档、产物和历史变更
- 输入：业务和验证完成
- 交付物：更新后的 `submit_package/`、归档的 changes
- Gate：交付目录清晰、主线状态明确、历史记录可追溯

## 5. 当前阶段
- 当前阶段：**Phase 1：主线稳定**

## 6. 阶段切换规则
- 只有在当前阶段 Gate 达成后，才能切换到下一阶段
- 阶段切换时要更新：
  - `docs/README.md`
  - `docs/stages/current.md`
  - `docs/changes/active.md`

## 7. 当前不做什么
- 不把旧 Kotlin/Compose 多模块重新拉回主线
- 不把 `submit_package/` 当作开发目录
- 不在缺 change 的情况下直接推进中等以上任务
