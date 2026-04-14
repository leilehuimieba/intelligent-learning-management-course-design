# AGENTS.md

## 1. 回答语言
- 默认使用简体中文。
- 代码、命令、日志、报错保持原文。
- 若用户明确要求英文，再切换。

## 2. 当前唯一执行主线
- 当前唯一开发主线是 `courseapp`。
- `app / core / feature / worker` 为历史遗留，只读，不作为当前默认推进对象。
- `submit_package/` 为交付产物目录，不作为默认开发目录。

## 3. 默认工作方式
- 默认先检查状态，再进入实现。
- 项目推进类请求，优先读取：
  1. `docs/README.md`
  2. `docs/stages/current.md`
  3. `docs/changes/active.md`
  4. 当前活跃 change 下的 `proposal.md / design.md / tasks.md / status.md / verify.md`
- 若上述文档不存在，应先指出缺口，再建议补齐。

## 4. 缺关键信息时的默认行为
- 缺少阶段目标、当前活跃 change、验收标准、风险或回退时，先输出缺口，不直接编码。
- 不在关键信息缺失时硬猜完整架构。
- 先最小化补齐文档，再继续推进。

## 5. change 工作区规则
- 中等以上任务必须进入 `docs/changes/`。
- 中等以上任务包括：
  - 新功能
  - 新页面
  - 改数据结构
  - 改流程
  - 改项目规范
  - 改提交结构
  - 影响多个文件或模块
- 每个 change 默认包含：
  - `proposal.md`
  - `design.md`
  - `tasks.md`
  - `status.md`
  - `verify.md`

## 6. 继续任务时的默认读取顺序
- 继续任何任务时，优先读取 `docs/changes/active.md` 指向的 change。
- 若没有活跃 change：
  - 先判断任务是否需要建 change
  - 若需要，先建议建立 change
  - 若不需要，再做最小改动

## 7. 实现后必须同步更新
- 完成实现后，至少同步更新：
  - `tasks.md`
  - `status.md`
- 若设计发生变化，还要更新：
  - `design.md`
- 若范围或目标发生变化，还要更新：
  - `proposal.md`
- 若已完成验证，还要更新：
  - `verify.md`

## 8. 验证前必须补的内容
- 未补 `verify.md` 或未记录验证证据时，不应宣称“完成”。
- verify 至少要说明：
  - 验证对象
  - 验证方式
  - 验证结果
  - 未验证项
  - 是否达到当前 Gate

## 9. 默认输出格式
- 对项目推进类请求，默认优先输出：
  - 当前状态
  - 缺失项
  - 下一步建议

## 10. 禁止行为
- 禁止跳过 verify 直接宣称完成
- 禁止跳过阶段 Gate
- 禁止擅自扩大 scope
- 禁止绕过当前活跃 change 开新主线
- 禁止把历史遗留模块当作当前默认推进对象
- 禁止把 `submit_package/` 当作开发主目录
