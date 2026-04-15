# Verify - courseapp-phase1-stabilization

## 验证对象
验证 Phase 1 阶段切换和第一个真实业务 change 是否已成功建立。

## 验证方法
1. 检查 `docs/README.md`、`docs/roadmap.md`、`docs/stages/current.md` 是否切换到 Phase 1
2. 检查 `docs/changes/active.md` 是否指向当前 change
3. 检查当前 change 的 proposal/design/tasks/status/verify 是否已创建
4. 检查 `courseapp` 中是否存在：
   - `HomeActivity`
   - `OnboardingActivity`
   - `SettingsActivity`
   - `ExportUtils`
5. 检查 `docs/references/courseapp/` 中是否保留答辩参考文档
6. 执行 `./gradlew :courseapp:assembleDebug` 验证当前主线是否可构建
7. 对照答辩说明、技术实现讲解、关键函数清单与当前源码是否一致
8. 为页面级实际演示准备可执行的手工演示清单
9. 为老师高频追问准备一页式速答材料
10. 统一应用显示名与答辩文档中的项目对外名称
11. 统一 `submit_package` 中提交材料的项目名称口径
12. 基于现有答辩文档生成课程设计报告正式稿初版
13. 为当前项目补充 `.gitignore`，避免提交构建产物和本机配置
14. 初始化当前项目 Git 仓库并完成首次提交
15. 创建公开 GitHub 仓库并完成首轮远程推送
16. 补充根目录 `README.md` 作为仓库首页入口说明
17. 将仓库首页 README 调整为更标准的 GitHub 项目展示结构
18. 为仓库首页补充截图展示区，并建立截图目录占位说明
19. 在本地 Android 模拟器中启动 `courseapp`，生成真实页面截图并接入 README
20. 在当前 Windows 中文目录下补充最小 Gradle 配置，使 `courseapp` 能完成本地构建与安装
21. 将真实运行截图插入课程设计报告的仓库参考版与提交包版

## 验证结果
- 已切换到 Phase 1：主线稳定
- 已建立第一个真实业务 change
- 已确认 `courseapp` 中存在引导页、设置页和 CSV 导出相关实现
- 已确认答辩相关参考文档仍可作为说明依据
- 已执行 `./gradlew :courseapp:assembleDebug`，构建成功
- 已完成页面流程与关键函数的首轮源码核对，未发现结构性冲突
- 已补充手工演示清单，可直接用于答辩前自测
- 已补充老师追问一页速答版，可直接用于答辩前口语化练习
- 已将应用显示名和主要答辩文档中的项目名称统一为“智能学习管理程序”
- 已将 `submit_package` 中的提交文档和源码副本显示名同步统一
- 已补充课程设计报告正式稿初版，可直接在此基础上填写个性信息并提交
- 已补充 `.gitignore`，当前忽略规则覆盖 Gradle 构建目录与 `local.properties`
- 已完成当前项目 Git 仓库初始化与首次提交
- 已完成公开 GitHub 仓库创建与首轮远程推送
- 已补充根目录 `README.md`，可直接作为仓库首页使用
- 已将仓库首页整理为更标准的 GitHub README 风格
- 已补充仓库首页截图展示区结构，并提供截图目录占位入口
- 已在 Android 模拟器中完成真实运行截图，首页已展示引导页、首页、任务页、专注页、统计页和设置页
- 已在 `gradle.properties` 中补充最小路径检查覆盖配置，当前仓库可在中文目录下完成 `:courseapp:assembleDebug` 与 `:courseapp:installDebug`
- 已将真实运行截图插入课程设计报告，可直接用于报告预览与后续导出

## 证据位置
1. `docs/README.md`
2. `docs/roadmap.md`
3. `docs/stages/current.md`
4. `docs/changes/active.md`
5. `docs/changes/20260414-courseapp-phase1-stabilization/`
6. `courseapp/src/main/java/com/team/focusstudy/courseapp/ui/`
7. `courseapp/src/main/java/com/team/focusstudy/courseapp/util/`
8. `docs/references/courseapp/`
9. 构建命令：`./gradlew :courseapp:assembleDebug`
10. `docs/references/courseapp/14-courseapp-源码核对记录.md`
11. `docs/references/courseapp/15-courseapp-手工演示清单.md`
12. `docs/references/courseapp/16-courseapp-老师追问一页速答.md`
13. `courseapp/src/main/res/values/strings.xml`
14. `submit_package/文档/07-courseapp-答辩说明.md`
15. `submit_package/文档/08-课程设计报告骨架.md`
16. `submit_package/文档/09-个人总结模板.md`
17. `submit_package/提交说明.txt`
18. `submit_package/源码/focus-study-courseapp-source/courseapp/src/main/res/values/strings.xml`
19. `docs/references/courseapp/08-课程设计报告骨架.md`
20. `submit_package/文档/08-课程设计报告骨架.md`
21. `.gitignore`
22. 本地 Git 提交记录
23. GitHub 公开仓库页面与远程推送记录
24. `README.md`
25. `docs/assets/screenshots/README.md`
26. `docs/assets/screenshots/onboarding.png`
27. `docs/assets/screenshots/home.png`
28. `docs/assets/screenshots/task.png`
29. `docs/assets/screenshots/focus.png`
30. `docs/assets/screenshots/stats.png`
31. `docs/assets/screenshots/settings.png`
32. `gradle.properties`
33. `docs/references/courseapp/08-课程设计报告骨架.md`
34. `submit_package/文档/08-课程设计报告骨架.md`
35. `submit_package/文档/截图/`

## 未验证项
1. 尚未执行页面级实际手工演示验证
2. 尚未根据手工演示结果判断是否需要最小修补
3. 尚未形成最终“通过/未通过/需修补”结果表
4. 尚未根据你的实际表达习惯对速答版做二次压缩
5. 尚未补充报告中的个人信息和运行截图

## 是否达到当前 Gate
- 已达到“Phase 1 入口建立 + 可构建 + 口径一致 + 可演示准备完成 + 可答辩准备完成”这一子 Gate，可进入下一步实际手工演示、口语化练习与最小修补阶段
