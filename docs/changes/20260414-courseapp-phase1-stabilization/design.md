# Design - courseapp-phase1-stabilization

## 设计目标
在不增加项目复杂度的前提下，把 `courseapp` 的真实推进收拢到一个轻量、可解释、可答辩的 Phase 1 change 中。

## 当前约束
1. 主线必须继续保持为 `courseapp`
2. 技术路线继续保持 Java + Activity + XML + SQLite + SharedPreferences
3. 不为了“看起来完整”而额外加复杂架构
4. 后续所有中等以上任务继续走 change 工作区

## 现状结论
基于当前源码检查：

1. `HomeActivity` 已在启动时判断是否进入引导页
2. `OnboardingActivity` 已支持保存昵称、每日目标、默认专注时长
3. `SettingsActivity` 已支持修改上述配置
4. `ExportUtils` 已支持导出 CSV 并通过系统分享
5. 课程答辩说明、技术讲解、关键函数清单已存在于 `docs/references/courseapp/`

因此，Phase 1 不应再把“是否有这些功能”当作主要问题，而应把重点放在：

1. 当前实现是否稳定
2. 当前说明是否统一
3. 后续若继续做功能，哪些是最小且必要的

## 方案概述
采用“先收拢、后实现”的轻量方案：

1. 切换阶段到 Phase 1
2. 将本 change 作为新的当前活跃 change
3. 先记录已实现能力与缺口
4. 再按任务清单决定是否进入下一步代码实现

## 当前建议推进边界
本 change 后续默认优先考虑以下轻量事项：

1. 验证 `courseapp` 可构建
2. 核对页面流程与答辩说明是否一致
3. 若存在明显小缺口，再做最小修补
4. 最后再考虑提交包整理

## 回退方案
若本 change 的任务定义不合适：

1. 回退 `docs/stages/current.md`
2. 回退 `docs/changes/active.md`
3. 保留 Phase 0 治理底盘，不影响 `courseapp` 源码
