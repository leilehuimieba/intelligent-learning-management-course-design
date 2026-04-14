# Change 工作区规范

## 1. 目录作用
`docs/changes/` 用来承接中等以上任务，避免直接在仓库中裸奔推进。

## 2. 哪类任务必须建 change
以下任务必须建 change：

1. 新功能
2. 新页面
3. 改数据库
4. 改导出逻辑
5. 改页面流程
6. 改 docs 主入口
7. 改 AGENTS 协作规则
8. 影响多个文件或模块的改动

以下任务可不建 change：

1. 小 typo
2. 小文案修正
3. 单文件极小调整
4. 纯解释和纯答疑

## 3. 命名规则
统一使用：

`YYYYMMDD-short-name`

例如：

- `20260414-governance-bootstrap`
- `20260415-task-edit-flow`

## 4. 标准文件
每个 change 默认包含：

1. `proposal.md`
2. `design.md`
3. `tasks.md`
4. `status.md`
5. `verify.md`

## 5. 当前活跃 change
- 见 `docs/changes/active.md`

## 6. 并行 change 规则
- 可以存在多个 change
- 但同一时刻只有一个默认主推进项
- 若切换主推进项，必须同步更新 `active.md`

## 7. 完成后的处理
- 已完成或废弃的 change 后续移入 `archive/`
- 归档后只读，不再作为当前执行依据
