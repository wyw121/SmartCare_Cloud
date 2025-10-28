# 🏥 智慧医养大数据公共服务平台

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.3.1-blue.svg)](https://baomidou.com/)
[![Vue](https://img.shields.io/badge/Vue-3.4.0-green.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.4.4-blue.svg)](https://element-plus.org/)
[![Vite](https://img.shields.io/badge/Vite-5.0.10-purple.svg)](https://vitejs.dev/)

## 📋 项目简介

智慧医养大数据公共服务平台是一个面向老年人医疗和养老服务的综合性云端管理系统，采用医养结合的数字化服务模式，为医护人员、管理人员和家属提供全方位的健康管理和服务支持。

### 核心特性

- 🏥 **全面健康管理** - 老人档案、健康记录、实时监测
- ⚠️ **智能预警系统** - 健康异常自动预警、风险评估
- 📊 **数据分析决策** - 大数据分析、可视化报表
- 🔐 **完善权限体系** - 基于RBAC的细粒度权限控制
- 📱 **家属端支持** - 家属可查看关联长辈健康信息
- ⚙️ **设备管理** - 医疗设备监控和数据采集

## 🛠️ 技术架构

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 核心框架 |
| MyBatis-Plus | 3.5.3.1 | ORM框架 |
| MySQL | 8.0.33 | 关系型数据库 |
| Redis | - | 缓存中间件 |
| Druid | 1.2.16 | 数据库连接池 |
| PageHelper | 1.4.6 | 分页插件 |
| SpringDoc OpenAPI | 1.6.15 | API文档 |
| FastJSON | 2.0.25 | JSON处理 |
| JWT | 0.11.5 | 身份认证 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.0 | 前端框架 |
| Element Plus | 2.4.4 | UI组件库 |
| Vite | 5.0.10 | 构建工具 |
| Vue Router | 4.2.5 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Axios | 1.6.2 | HTTP客户端 |
| ECharts | 5.4.3 | 数据可视化 |

## 📁 项目结构

```
SmartCare_Cloud/
├── backend/                    # 后端服务 (Spring Boot)
│   ├── src/main/java/com/smartcare/cloud/
│   │   ├── controller/         # REST API控制器
│   │   ├── service/           # 业务逻辑层
│   │   ├── mapper/            # MyBatis-Plus Mapper
│   │   ├── entity/            # 数据库实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── vo/                # 视图对象
│   │   ├── config/            # 配置类
│   │   ├── security/          # 安全配置
│   │   ├── utils/             # 工具类
│   │   └── exception/         # 异常处理
│   └── src/main/resources/
│       ├── mapper/            # MyBatis XML映射文件
│       ├── application.yml    # 应用配置
│       └── application-dev.yml # 开发环境配置
│
├── frontend/                  # 前端应用 (Vue 3)
│   ├── src/
│   │   ├── views/             # 页面组件
│   │   ├── components/        # 可复用组件
│   │   ├── api/               # API接口封装
│   │   ├── router/            # 路由配置
│   │   ├── store/             # Pinia状态管理
│   │   ├── utils/             # 工具函数
│   │   └── styles/            # 样式文件
│   └── public/                # 静态资源
│
├── docs/                      # 项目文档
│   ├── development/           # 开发指南
│   ├── reports/               # 功能报告
│   ├── design/                # 设计规范
│   └── analysis/              # 分析报告
│
└── .github/                   # GitHub配置
    └── copilot-instructions.md # Copilot指令
```

## 🚀 快速开始

### 环境要求

- **JDK**: 1.8+
- **Maven**: 3.6+
- **Node.js**: 16.0+
- **MySQL**: 8.0+
- **Redis**: 最新稳定版

### 后端启动

```bash
# 进入后端目录
cd backend

# 编译项目
mvn clean compile

# 运行测试
mvn test

# 启动应用 (开发环境)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 或打包后运行
mvn clean package -DskipTests
java -jar target/smartcare-cloud-backend-1.0.0.jar
```

后端服务默认运行在 `http://localhost:8080`

### 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

前端应用默认运行在 `http://localhost:3000`

### 数据库配置

1. 创建数据库:
```sql
CREATE DATABASE smartcare_cloud CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行SQL脚本 (按顺序):
```bash
backend/create_user_table.sql
backend/create_role_system.sql
backend/create_family_tables_final.sql
backend/create_family_permission_tables.sql
```

3. 配置数据库连接:
编辑 `backend/src/main/resources/application-dev.yml`

## 📖 核心功能模块

### 1. 首页仪表板
- 数据统计概览
- 健康预警实时展示
- 关键指标可视化

### 2. 老人档案管理
- 基本信息管理
- 健康档案记录
- 档案导入导出

### 3. 健康预警系统
- 实时健康监测
- 异常自动预警
- 预警处理跟踪

### 4. 评估报告
- 健康评估报告
- 风险等级评定
- 护理建议生成

### 5. 设备管理
- 医疗设备监控
- 设备状态管理
- 数据采集配置

### 6. 系统管理
- 用户账号管理
- 角色权限配置
- 系统日志查看

### 7. 家属端功能
- 关联长辈管理
- 健康信息查看
- 预警消息接收

### 8. 大数据决策分析
- 多维数据分析
- 趋势预测
- 决策支持报表

## 🔒 权限体系

系统采用RBAC(基于角色的访问控制)权限模型:

- **超级管理员**: 系统全部权限
- **管理员**: 日常运营管理权限
- **医生**: 医疗数据查看和处理权限
- **老人**: 个人信息查看权限
- **家属**: 关联长辈信息查看权限

详见: [docs/RBAC权限系统说明.md](./docs/RBAC权限系统说明.md)

## 📚 开发文档

- [项目概览](./docs/PROJECT_OVERVIEW.md) - 项目整体介绍
- [数据库设置](./docs/DATABASE_SETUP.md) - 数据库配置指南
- [工作区设置](./docs/WORKSPACE_SETUP.md) - 开发环境配置
- [Git提交规范](./docs/GIT_COMMIT_GUIDE.md) - 代码提交规范
- [前端开发指南](./docs/development/前端开发指南.md) - 前端开发规范
- [设备管理接入指南](./docs/development/设备管理接入指南.md) - 设备接入文档

## 🔧 开发规范

### 后端开发规范
- 类名使用 PascalCase
- 方法名使用 camelCase
- 所有公共方法必须有完整的Javadoc注释
- Controller返回统一的 `ResponseResult<T>` 格式
- 使用MyBatis-Plus注解进行ORM映射
- 数据库表名使用 snake_case

### 前端开发规范
- 组件名使用 PascalCase
- 使用 Vue 3 Composition API
- 使用 Element Plus 组件库
- API调用统一封装在 `api/` 目录
- 状态管理使用 Pinia
- 样式使用 SCSS 预处理器

### 安全规范
- 敏感数据必须加密存储
- 所有API接口需要权限验证
- 个人健康信息需要脱敏处理
- 前端参数必须后端验证

### 性能要求
- 列表查询必须分页
- 合理使用Redis缓存
- 数据库字段添加索引
- 大数据量处理使用异步

## 🧪 测试

```bash
# 后端测试
cd backend
mvn test

# 前端测试 (如已配置)
cd frontend
npm run test
```

测试覆盖率要求: ≥80%

## 📊 API文档

启动后端服务后访问:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🤝 贡献指南

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

详见: [Git提交规范](./docs/GIT_COMMIT_GUIDE.md)

## 📄 许可证

本项目仅供学习和研究使用。

## 👥 联系方式

- 项目仓库: https://github.com/wyw121/SmartCare_Cloud
- 问题反馈: [Issues](https://github.com/wyw121/SmartCare_Cloud/issues)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

**最后更新**: 2025-10-28
