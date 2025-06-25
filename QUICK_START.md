# 🚀 智慧医养平台 - 快速启动指南

## 📋 环境要求

### 必需软件
- ✅ **JDK 17+** (后端运行环境)
- ✅ **Node.js 18+** (前端开发环境)
- ✅ **Maven 3.6+** (Java项目构建)
- ✅ **MySQL 8.0+** (数据库)
- ✅ **VS Code** (推荐IDE)

### 推荐VS Code扩展
```json
{
  "recommendations": [
    "ms-vscode.vscode-java-pack",
    "Vue.volar",
    "bradlc.vscode-tailwindcss",
    "ms-vscode.vscode-typescript-next",
    "GitHub.copilot",
    "ms-python.python"
  ]
}
```

## 🏁 快速开始

### 1️⃣ 克隆项目
```bash
git clone <repository-url>
cd SmartCare_Cloud
```

### 2️⃣ 打开VS Code工作区
```bash
code SmartCare_Cloud.code-workspace
```

### 3️⃣ 一键启动开发环境
在VS Code中按 `Ctrl+Shift+P`，选择 **Tasks: Run Task**，然后选择：
**🚀 启动开发环境**

或者手动执行：

#### 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

#### 启动前端服务
```bash
cd frontend
npm install
npm run dev
```

### 4️⃣ 访问应用
- 🎨 **前端应用**: http://localhost:3000
- 🚀 **后端API**: http://localhost:8080
- 📖 **API文档**: http://localhost:8080/doc.html

## 🗄️ 数据库配置

### 创建数据库
```sql
CREATE DATABASE smartcare_cloud CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 配置连接信息
编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smartcare_cloud?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 初始化数据
```bash
mysql -u your_username -p smartcare_cloud < backend/src/main/resources/sql/init.sql
```

## 🛠️ 开发工具配置

### VS Code 任务快捷键
- `Ctrl+Shift+P` → **Tasks: Run Task**
  - 🚀 启动开发环境
  - 🔨 后端编译
  - 📦 前端安装依赖
  - 🏃‍♂️ 启动后端服务
  - 🎨 启动前端服务
  - 📊 运行测试

### 调试配置
1. 按 `F5` 启动调试
2. 选择配置：
   - **Debug Spring Boot** (调试后端)
   - **Debug Frontend** (调试前端)

## 📝 代码规范

### Java代码规范
- 类名使用 `PascalCase`
- 方法名使用 `camelCase`
- 常量使用 `UPPER_SNAKE_CASE`
- 所有公共方法需要Javadoc注释

### Vue代码规范
- 组件名使用 `PascalCase`
- 文件名使用 `kebab-case`
- 样式使用 `SCSS`
- 遵循Vue3 Composition API风格

### 提交规范
```bash
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试相关
chore: 构建或工具相关
```

## 🚨 常见问题

### Q: 后端启动失败
**A**: 检查以下项目：
1. JDK版本是否为17+
2. MySQL服务是否启动
3. 数据库连接配置是否正确
4. 端口8080是否被占用

### Q: 前端启动失败
**A**: 检查以下项目：
1. Node.js版本是否为18+
2. 是否执行了 `npm install`
3. 端口3000是否被占用
4. 网络是否正常（下载依赖需要）

### Q: 构建警告
**A**: 当前已知警告：
- Sass @import 规则废弃警告（不影响功能）
- Vite CJS API 废弃警告（不影响功能）
- 依赖安全警告（moderate级别，可选修复）

### Q: 数据库连接失败
**A**: 确认配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smartcare_cloud
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 📊 开发状态检查

### 检查命令
```bash
# 检查Java版本
java -version

# 检查Node版本
node -v

# 检查Maven版本
mvn -version

# 检查MySQL状态
mysql --version

# 后端编译测试
cd backend && mvn compile

# 前端构建测试
cd frontend && npm run build
```

### 预期输出
- ✅ **后端编译**: `BUILD SUCCESS`
- ✅ **前端构建**: `built in xxx s`
- ✅ **数据库**: 连接成功
- ✅ **服务启动**: 无错误日志

## 🎯 下一步开发

### 立即可开始的任务
1. **完善实体类**: 补充业务字段和关系
2. **实现API接口**: 基于RESTful设计
3. **前端页面开发**: 完善表单和列表功能
4. **用户认证**: 实现登录注册功能

### 推荐开发顺序
1. 数据库设计和初始化脚本
2. 用户管理和权限控制
3. 老人档案管理功能
4. 健康数据记录功能
5. 预警系统开发
6. 报表统计功能

## 📱 联系支持

遇到问题时：
1. 检查控制台错误信息
2. 查看 `PROJECT_STATUS.md` 详细说明
3. 使用VS Code的问题面板查看错误
4. 检查网络连接和防火墙设置

---

**🎉 恭喜！你的智慧医养平台开发环境已就绪！**
