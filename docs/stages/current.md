# 当前阶段定义

## 1. 阶段名称
Phase 1：主线稳定

## 2. 阶段目标
1. 在 `courseapp` 主线上保持可编译
2. 在 `courseapp` 主线上保持可演示
3. 在 `courseapp` 主线上保持可答辩、可解释
4. 用真实业务 change 验证这套 docs + change 协作机制
5. 继续保持旧模块只读，不重新扩散主线

## 3. 阶段输入
1. 已建立的 docs 主入口与 AGENTS 规则
2. 已存在的 `courseapp` 源码
3. 已归档到 `docs/references/courseapp/` 的答辩参考文档
4. 已存在的 `submit_package/`
5. 已完成首轮验证的治理类 change

## 4. 阶段交付物
1. 至少一个真实业务 change
2. 对 `courseapp` 当前实现边界的清晰说明
3. 对“引导页 / 设置 / CSV 导出 / 答辩资料”的统一任务清单
4. 与当前业务 change 对应的验证记录

## 5. 当前活跃 change
- `docs/changes/20260414-courseapp-phase1-stabilization/`

## 6. Gate / 验收条件
满足以下条件才算通过：

1. 当前业务 change 已建立并可承接真实任务
2. `courseapp` 当前实现范围已被清楚记录
3. 已明确下一轮最小可做事项，而不是无序扩 scope
4. 关键功能可说明其实现技术与关键函数
5. 验证记录能支撑“可编译 / 可演示 / 可答辩”的阶段口径

## 7. 失败回退
如果 Phase 1 推进方式不适应实际使用：

1. 不回退 `courseapp` 源码
2. 只回退当前业务 change 与阶段文档
3. 保留 Phase 0 的治理底盘

## 8. 当前禁止事项
1. 不把旧模块重新拉为默认推进主线
2. 不跳过 `docs/changes/active.md` 直接推进中等以上任务
3. 不在未建立任务清单前直接大范围改 `courseapp`
4. 不跳过 `verify.md` 就宣称 change 完成
