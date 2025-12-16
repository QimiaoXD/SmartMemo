# SmartMemo（Android 应用集成化实验）

SmartMemo 是一个基于 **Android Studio** 开发的综合性 Android 应用实验项目，围绕真实应用场景完成集成化功能实现，覆盖 **UI 布局与控件使用、多 Activity 全生命周期管理、数据存储/数据分享/网络访问** 等核心知识点，并包含DeepSeek api调用和夜间模式等扩展能力。

## 功能概览
- **UI 布局与控件**：常用控件使用、页面布局与交互设计
- **多 Activity 管理**：多页面跳转与 Activity 生命周期管理
- **数据存储**：本地数据持久化（按项目实际实现为准）
- **数据分享**：通过 Intent 等方式进行数据共享/跳转传参
- **网络访问**：API 请求与数据解析展示（如DeepSeek api/备忘录同步等）
- **亮点功能**：夜间模式适配（Dark Mode）

## 实验要求对应说明
- 1）UI布局和控件使用 ✅  
- 2）多 Activity 的全生命周期管理 ✅  
- 3）数据存储、数据分享、网络访问等 ✅  

## 开发环境
- **开发工具**：Android Studio
- **构建系统**：Gradle（Kotlin DSL）
- **编程语言**：Java（Java 11）
- **最低支持版本**：Android 7.0（API 24）
- **目标 / 编译版本**：Android API 36
- **主要依赖库**：
  - AndroidX AppCompat 1.7.0
  - Material Components 1.12.0
  - RecyclerView 1.3.2
  - CoordinatorLayout 1.2.0
  - OkHttp 4.12.0（用于网络请求）

## 运行方式
1. 克隆项目到本地：
   ```bash
   git clone https://github.com/QimiaoXD/SmartMemo.git
