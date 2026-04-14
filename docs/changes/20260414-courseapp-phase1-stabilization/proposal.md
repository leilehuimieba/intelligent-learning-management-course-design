# Proposal - courseapp-phase1-stabilization

## 标题
建立 `courseapp` Phase 1 主线稳定 change

## 背景
Phase 0 已完成项目治理底盘建设，当前需要进入第一个真实业务 change，验证这套规范能否承接 `courseapp` 的实际推进。

结合当前源码检查可见，`courseapp` 已存在以下能力：

1. 首次引导页 `OnboardingActivity`
2. 设置页 `SettingsActivity`
3. 首页 CSV 导出 `ExportUtils + FileProvider`
4. 与课程答辩相关的参考文档 `docs/references/courseapp/`

因此，当前更适合先做“主线稳定与任务收拢”，而不是立刻扩展复杂功能。

## 目标
1. 将项目阶段切换到 Phase 1：主线稳定
2. 为 `courseapp` 建立第一个真实业务 change
3. 明确当前已实现内容、当前缺口与后续最小推进项
4. 保持项目轻量、好答辩、好解释

## 非目标
1. 本 change 不直接新增复杂业务功能
2. 不重构旧模块
3. 不引入新框架或新依赖

## 范围
本 change 当前仅覆盖：

1. Phase 1 阶段文档切换
2. `courseapp` 当前实现边界确认
3. 真实业务 change 的 proposal/design/tasks/status/verify 建立

## 风险
1. 若不先收拢当前实现边界，后续很容易继续无序加功能
2. 若业务 change 目标定得过大，会让项目变难讲、难答辩

## 为什么现在做
因为当前源码里你关心的“引导页 + 设置 + CSV 导出”已经基本具备，如果不先建立一个真实业务 change，后续继续推进时仍会缺少统一任务线。
