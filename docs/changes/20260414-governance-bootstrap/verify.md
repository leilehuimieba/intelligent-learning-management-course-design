# Verify - governance-bootstrap

## 验证对象
验证项目执行规范、AI 协作规范和 change 工作区是否已落地到仓库。

## 验证方法
1. 检查是否已创建以下文件：
   - `AGENTS.md`
   - `docs/README.md`
   - `docs/INDEX.md`
   - `docs/roadmap.md`
   - `docs/stages/current.md`
   - `docs/changes/README.md`
   - `docs/changes/active.md`
   - 当前 change 的 proposal/design/tasks/status/verify
2. 检查文档中是否明确：
   - `courseapp` 为唯一主线
   - 旧模块只读
   - 中等以上任务必须进入 change
   - AI 默认先检查状态再实现
3. 检查 `courseapp` 参考文档是否已归入 `docs/references/courseapp/`
4. 搜索 docs 内旧的 `docs/07-13` 根路径引用是否已清理

## 验证结果
- 已创建所需规范文件
- 已明确当前主线、阶段、活跃 change、旧模块定位和 change 规则
- `courseapp` 参考文档已从根 `docs/` 收敛到 `docs/references/courseapp/`
- `docs/` 根目录已不再保留原 `07-13` 文件
- 当前索引与入口文档已统一引用 `docs/references/courseapp/`

## 证据位置
1. `AGENTS.md`
2. `docs/README.md`
3. `docs/roadmap.md`
4. `docs/stages/current.md`
5. `docs/changes/README.md`
6. `docs/changes/active.md`
7. `docs/changes/20260414-governance-bootstrap/`
8. `docs/references/courseapp/`

## 未验证项
1. 尚未通过后续真实业务 change 长期验证这套规则的耐用性

## 是否达到当前 Gate
- 已达到当前阶段 Gate，可进入后续真实任务使用阶段
