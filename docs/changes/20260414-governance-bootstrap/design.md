# Design - governance-bootstrap

## 设计目标
建立一套轻量、稳定、可长期演进的协作规范，让后续 AI 在推进 `courseapp` 时有统一入口和统一流程。

## 当前约束
1. 当前唯一主线必须是 `courseapp`
2. 旧模块只读，不再推进
3. `submit_package/` 仅作为交付产物目录
4. 规范必须轻量，不能过度流程化

## 方案概述
采用以下结构：

1. `docs/README.md` 作为唯一执行入口
2. `docs/roadmap.md` 和 `docs/stages/current.md` 统一阶段口径
3. `docs/changes/` 承接中等以上任务
4. `docs/changes/active.md` 指向当前活跃 change
5. `AGENTS.md` 约束 AI 默认工作方式

## 影响范围
1. 新增 `AGENTS.md`
2. 新增 docs 规范文件
3. 新增第一个治理类 change

## 关键流程
后续 AI 推进项目时默认流程：

1. 读 `docs/README.md`
2. 读 `docs/stages/current.md`
3. 读 `docs/changes/active.md`
4. 进入当前活跃 change
5. 若任务为中等以上改动，则先补齐 change 文件再实现

## 取舍
### 选择轻量 docs 驱动，而不是重型流程
原因：
当前项目规模不大，重流程会提高维护成本。

### 保留旧目录，但明确只读
原因：
直接重构或删除旧目录风险大，且没有必要。

## 回退方案
若后续证明这套规范不适用：

1. 只回退 docs 和 AGENTS 层文件
2. 不动 `courseapp` 源码
