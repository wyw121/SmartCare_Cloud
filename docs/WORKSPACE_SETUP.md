# 🚀 智慧医养平台 - VS Code 工作区设置指南

## ✅ 当前配置状态

### 技术栈版本

- **后端**: Spring Boot 2.7.18 + MyBatis-Plus 3.5.3.1 + MySQL 8.0.33
- **前端**: Vue 3.4.0 + Element Plus 2.4.4 + Vite 5.0.10
- **缓存**: Redis
- **文档**: SpringDoc OpenAPI 1.6.15

### GitHub Copilot 配置 ✅

- `.github/copilot-instructions.md` - 官方主配置文件已就位
- 文件结构完全符合官方规范
- 工作区已启用 Copilot 智能提示

### VS Code 工作区优化 ✅

- 📁 多文件夹工作区：主项目 + 文档目录
- ⚙️ GitHub Copilot 专项配置
- 🎨 编辑器优化设置
- 🔧 Java + Vue.js 开发环境配置
- 🚀 一键启动任务和调试配置

## 🛠️ 下一步操作

### 1. 安装推荐扩展

VS Code 会自动提示安装推荐扩展，或手动安装：

**必需扩展：**

```
GitHub Copilot
GitHub Copilot Chat
Extension Pack for Java
Vue Language Features (Volar)
```

**推荐扩展：**

```
Material Icon Theme
GitLens
ESLint
Prettier
SonarLint
```

### 2. 验证 GitHub Copilot 配置

在 VS Code 中按 `Ctrl+Shift+P`，运行：

```
GitHub Copilot: Check Status
```

### 3. 测试自定义指令

在 Copilot Chat 中输入：

```
创建一个老人档案管理的Spring Boot控制器
```

应该会生成符合项目规范的代码。

### 4. 创建项目结构

当前建议的项目结构：

```
SmartCare_Cloud/
├── backend/          # Spring Boot后端项目
├── frontend/         # Vue.js前端项目
├── sql/              # 数据库脚本
├── docs/             # 项目文档
└── docker/           # Docker配置
```

## 🔧 工作区特性

### 多文件夹视图

- 🏥 **智慧医养平台** - 主项目代码
- 📄 **文档资料** - GitHub 配置文档

### 一键启动

在 VS Code 中按 `Ctrl+Shift+P`，选择：

- `Tasks: Run Task` → `🚀 启动开发环境`
- `Debug: Start Debugging` → `🚀 启动完整开发环境`

### GitHub Copilot 增强

- ✅ 全语言支持（Java, Vue, SQL, YAML 等）
- ✅ 项目上下文自动应用
- ✅ 智能代码生成和重构建议

## 💡 使用技巧

### 1. Copilot Chat 命令

```bash
@workspace 如何实现老人健康数据的预警算法？
#codebase 查找用户权限验证的实现
#file:copilot-instructions.md 查看项目规范
```

### 2. 代码生成

使用注释触发 Copilot：

```java
// 创建老人健康记录的MyBatis-Plus实体类，包含基础字段和审计字段
@TableName("elderly_health_records")
public class ElderlyHealthRecord {
    // Copilot 会自动生成完整的实体类
}
```

### 3. 快捷键

- `Ctrl+I` - 打开 Copilot 内联聊天
- `Ctrl+Shift+I` - 打开 Copilot 聊天面板
- `Tab` - 接受 Copilot 建议
- `Esc` - 拒绝建议

## 📊 项目开发流程

### 1. 后端开发

```bash
# 进入后端目录
cd backend

# Maven编译
mvn clean compile

# 启动Spring Boot
mvn spring-boot:run
```

### 2. 前端开发

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 3. 数据库设置

```sql
-- 创建数据库
CREATE DATABASE smartcare_cloud CHARACTER SET utf8mb4;

-- 运行初始化脚本
-- 位置：sql/init.sql
```

## 🎯 开发规范提醒

### 后端规范 (Spring Boot 2.7.18 + MyBatis-Plus 3.5.3.1)
- 控制器返回统一的 `ResponseResult<T>` 格式
- 所有公共方法必须包含 Javadoc 注释
- 使用 `@RestController` + `@RequestMapping` 定义API
- 使用 **MyBatis-Plus** 注解 (`@TableName`, `@TableId`) 进行ORM映射
- 数据库表名使用 snake_case 命名法
- Mapper继承 `BaseMapper<T>` 接口

### 前端规范 (Vue 3.4.0 + Element Plus 2.4.4)
- 组件名使用 PascalCase
- 使用 **Vue 3 Composition API** (`<script setup>`)
- 使用 **Element Plus 2.4.4** UI 组件库
- 路由使用 **Vue Router 4**
- 状态管理使用 **Pinia**
- 构建工具使用 **Vite 5.0.10**
- API调用统一封装在 `api/` 目录下

### 安全要求

- 敏感数据必须加密存储
- 所有 API 接口需要权限验证
- 个人健康信息需要脱敏处理

## 🔍 问题排查

### Copilot 不工作

1. 检查网络连接
2. 确认已登录 GitHub 账号
3. 验证 Copilot 订阅状态
4. 重启 VS Code

### 扩展冲突

1. 禁用不必要的扩展
2. 检查 Java 语言服务器状态
3. 清除工作区缓存

---

🎉 **配置完成！您现在可以开始高效的智慧医养平台开发了！**
