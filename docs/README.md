# 项目执行入口

## 1. 当前唯一执行主线
- 当前唯一开发主线：`courseapp`
- 当前默认推进范围：`courseapp + docs + docs/changes + AGENTS.md`
- 历史遗留模块：`app / core / feature / worker`
- 交付目录：`submit_package/`

## 2. 当前阶段
- 当前阶段：**Phase 1：主线稳定**
- 当前活跃 change：`docs/changes/20260414-courseapp-phase1-stabilization/`
- 当前目标：在 `courseapp` 主线上建立可编译、可解释、可答辩的稳定推进基线

## 3. 执行入口顺序
后续推进项目时，默认按以下顺序读取：

1. `docs/README.md`
2. `docs/stages/current.md`
3. `docs/changes/active.md`
4. 当前活跃 change 下的：
   - `proposal.md`
   - `design.md`
   - `tasks.md`
   - `status.md`
   - `verify.md`

## 4. 文档优先级
文档冲突时，按以下优先级处理：

1. `docs/changes/active.md` 指向的当前活跃 change
2. 当前活跃 change 下的 `status.md / tasks.md / design.md`
3. `docs/stages/current.md`
4. `docs/roadmap.md`
5. `docs/README.md`
6. `docs/references/courseapp/` 下的课程答辩与参考文档

## 5. 历史内容说明
- `app / core / feature / worker`：历史遗留，只读，不作为当前默认推进对象。
- `docs/references/courseapp/`：围绕 `courseapp` 的答辩与说明文档，默认作为参考资料使用。
- `submit_package/`：交付产物目录，不作为默认开发入口。

## 6. AI / 人协作默认流程
1. 先确认当前主线是否仍为 `courseapp`
2. 再确认当前阶段与当前活跃 change
3. 若缺 proposal / design / tasks / status / verify，先输出缺口
4. 若任务属于中等以上改动，先进入 change 工作区
5. 实现后同步更新 `tasks.md / status.md / verify.md`

## 7. 当前缺口
- 当前还缺基于真实业务 change 的持续使用记录
- 当前还缺页面级实际手工演示结果记录
- 当前还缺你自己过一遍后的最终答辩口语化练习

## 8. 下一步建议
1. 以后所有中等以上任务先在 `docs/changes/` 建 change
2. 每次继续项目时先读 `docs/changes/active.md`
3. 当前先结合“手工演示清单 + 老师追问速答版”自己过一遍，再决定是否最小修补
