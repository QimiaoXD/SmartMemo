# 📓 SmartMemo —— Android 智能记事应用

SmartMemo 是一个基于 **Android Studio + Java** 开发的智能记事应用，支持本地记事管理、云端智能摘要、关键词提取以及浅色 / 夜间模式切换。本项目作为 Android 应用开发实验，综合运用了 UI 布局、多 Activity 生命周期管理、本地数据存储与网络访问等核心技术。

---

## ✨ 功能特性

- 📝 **记事管理**
  - 新建、编辑、删除记事
  - 支持记事置顶与更新时间展示
- 🔍 **全文搜索**
  - 支持按标题 / 内容 / 摘要进行搜索
- 🤖 **智能摘要（DeepSeek API）**
  - 基于云端大模型生成记事摘要
  - 自动提取关键词，提升内容可读性
- 🌗 **浅色 / 夜间模式**
  - 基于 Material DayNight 主题
  - 支持手动切换，界面自动适配
- 📤 **系统级分享**
  - 通过 Intent 将记事内容分享到其他应用
- 💾 **本地数据持久化**
  - 使用 SQLite 数据库存储记事信息
  - 离线可用

---

## 🧱 项目结构说明

```text
app/
 ├─ src/main/java/com/example/memoweather/
 │   ├─ MainActivity.java        # 主界面（记事列表、搜索）
 │   ├─ EditNoteActivity.java    # 新建 / 编辑记事 + 智能摘要
 │   ├─ SettingsActivity.java    # 设置（夜间模式）
 │   ├─ Note.java                # 数据模型
 │   ├─ NotesDao.java            # 数据访问对象（CRUD）
 │   ├─ NotesDbHelper.java       # SQLite 数据库管理
 │   ├─ NoteAdapter.java         # RecyclerView 适配器
 │   └─ DeepSeekClient.java      # 网络访问（大模型 API）
 └─ src/main/res/
     ├─ layout/                  # 界面布局文件
     ├─ values/                  # 主题、颜色、字符串
     └─ values-night/            # 夜间模式配色

```


---


## 🛠 技术栈

- **开发语言**：Java
- **开发环境**：Android Studio
- **UI 设计**：Material Design Components
- **数据存储**：SQLite
- **网络请求**：OkHttp
- **智能摘要服务**：DeepSeek 大模型 API

## 🚀 运行说明

1. 使用 Android Studio 打开本项目  
2. 在 `DeepSeekClient.java` 中配置你的 DeepSeek API Key  
3. Sync Gradle 并运行项目（推荐使用模拟器或真机）

> ⚠️ 注意：智能摘要功能需要网络连接及有效的 API Key。

## 📚 实验说明

本项目完成并覆盖了以下实验要求：

1. **UI 布局和控件使用**  
2. **多 Activity 全生命周期管理**  
3. **数据存储、数据分享与网络访问**

## 🧠 项目亮点

- 接入云端大模型 API，实现智能摘要与关键词提取  
- 支持浅色 / 夜间模式切换，提升用户体验  
- DAO 结构封装数据库操作，代码结构清晰、易维护

## 📄 License

本项目仅用于学习与课程实验目的，未用于商业用途。
