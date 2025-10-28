# 智慧医养大数据公共服务平台 - 项目概述

## 项目描述

这是一个为老年人医疗和养老服务设计的综合性云端管理平台，实现医养结合的数字化服务模式。

## 核心功能

- 老人档案管理和健康监测
- 实时健康预警和风险评估
- 医疗设备管理和数据分析
- 用户权限控制和安全管理
- 家属端健康信息查看
- 大数据决策分析

## 技术架构

### 技术栈版本

- **后端**: Spring Boot 2.7.18 + MyBatis-Plus 3.5.3 + MySQL 8.0
- **前端**: Vue 3.4.0 + Element Plus 2.4.4 + Vite 5.0
- **缓存**: Redis 6.x
- **文档**: SpringDoc OpenAPI 1.6.15

### 系统架构图

```
前端 (Vue 3 + Element Plus + Vite)
    ↓ HTTP/HTTPS (Axios)
后端 (Spring Boot + MyBatis-Plus)
    ↓ JDBC
数据库 (MySQL 8.0 + Redis)
```

## 项目结构

````

SmartCare_Cloud/

├── backend/                    # Spring Boot 后端服务## 开发规范

│   ├── src/main/java/com/smartcare/cloud/- 后端使用RESTful API设计

│   │   ├── controller/         # REST API控制器- 前端采用组件化开发

│   │   ├── service/           # 业务逻辑层- 数据库表名使用下划线命名

│   │   ├── mapper/            # MyBatis-Plus Mapper接口- Java类名使用PascalCase

│   │   ├── entity/            # 数据库实体类 (MyBatis-Plus)- 所有接口需要权限验证

│   │   ├── dto/               # 数据传输对象
│   │   ├── vo/                # 视图对象
│   │   ├── config/            # 配置类
│   │   ├── security/          # Spring Security 安全配置
│   │   ├── utils/             # 工具类
│   │   └── exception/         # 异常处理
│   └── src/main/resources/
│       ├── mapper/            # MyBatis XML映射文件
│       ├── application.yml    # 应用配置
│       └── application-dev.yml # 开发环境配置
│
├── frontend/                  # Vue 3 前端应用
│   ├── src/
│   │   ├── views/             # 页面组件
│   │   ├── components/        # 可复用组件
│   │   ├── api/               # API接口封装
│   │   ├── router/            # Vue Router 路由配置
│   │   ├── store/             # Pinia 状态管理
│   │   ├── utils/             # 工具函数
│   │   └── styles/            # 全局样式
│   ├── public/                # 静态资源
│   └── vite.config.js         # Vite 配置
│
├── docs/                      # 项目文档
│   ├── development/           # 开发指南
│   ├── reports/               # 功能开发报告
│   ├── design/                # 设计规范
│   └── analysis/              # 项目分析报告
│
└── .github/                   # GitHub 配置
    └── copilot-instructions.md # Copilot 开发指令
```

## 核心业务实体

### 1. 老人 (Elderly)
- 基本信息、健康档案、联系方式
- 入院日期、护理级别、床位号
- 关联家属、责任医生

### 2. 医生 (Doctor)
- 医生信息、科室、职称
- 负责老人列表
- 工作记录

### 3. 健康记录 (HealthRecord)
- 生命体征数据
- 检查记录、用药记录
- 护理记录

### 4. 健康预警 (HealthWarning)
- 预警类型、级别
- 触发条件、处理状态
- 处理记录

### 5. 设备 (Equipment)
- 设备信息、状态
- 监测数据
- 维护记录

### 6. 用户 (User)
- 用户账号、角色
- 权限配置
- 登录记录

### 7. 家属 (Family)
- 家属信息
- 关联老人
- 权限配置

## 功能模块

### 1. 首页仪表板
- 数据统计卡片
- 健康预警实时展示
- 趋势图表
- 待办事项提醒

### 2. 老人档案管理
- 老人信息CRUD
- 健康档案查看
- 档案导入导出
- 批量操作

### 3. 健康预警系统
- 实时预警监控
- 预警处理流程
- 历史预警查询
- 预警统计分析

### 4. 评估报告
- 健康评估记录
- 风险等级评定
- 护理方案建议
- 报告打印导出

### 5. 重点人群管理
- 高风险人群筛选
- 重点关注列表
- 跟踪记录

### 6. 报表统计
- 数据统计报表
- 可视化图表
- 自定义报表
- 报表导出

### 7. 设备管理
- 设备台账
- 状态监控
- 数据采集
- 维护记录

### 8. 账户管理
- 老人账户管理
- 医生账户管理
- 家属账户管理
- 用户角色权限

### 9. 家属端功能
- 我的关联长辈
- 长辈健康信息
- 健康预警通知
- 个人中心

### 10. 大数据决策分析
- 多维数据分析
- 趋势预测
- 决策支持
- 智能推荐

### 11. 个人中心
- 个人信息管理
- 密码修改
- 操作日志
- 系统设置

## 开发规范

### 后端开发规范
- 使用 **RESTful API** 设计风格
- 控制器返回统一的 `ResponseResult<T>` 格式
- 使用 **MyBatis-Plus** 注解进行ORM映射
- 所有公共方法必须包含完整的 **Javadoc** 注释
- 类名使用 **PascalCase**,方法名使用 **camelCase**
- 数据库表名使用 **snake_case** 命名
- 统一的审计字段: created_time, updated_time
- 敏感数据必须加密存储

### 前端开发规范
- 组件名使用 **PascalCase**
- 使用 **Vue 3 Composition API**
- 统一使用 **Element Plus** 组件库
- API调用封装在 `api/` 目录下
- 状态管理使用 **Pinia**
- 路由配置使用 **Vue Router 4**
- 样式使用 **SCSS** 预处理器

### 安全规范
- 所有API接口需要 **JWT** 身份验证
- 基于 **RBAC** 的权限控制
- 个人健康信息需要脱敏处理
- 前端参数必须经过后端验证
- 防止SQL注入、XSS攻击

### 性能规范
- 列表查询必须使用 **PageHelper** 分页
- 合理使用 **Redis** 缓存
- 数据库字段添加适当索引
- 大数据量处理使用异步方式
- 前端列表使用虚拟滚动

### 测试规范
- 单元测试覆盖率 ≥ 80%
- 核心业务逻辑必须有集成测试
- 使用 JUnit 5 + Mockito
- API接口需要提供测试用例

## 数据库设计规范

### 表命名规范
- 使用 snake_case 命名
- 表名使用复数形式
- 关联表命名: `表1_表2_relation`

### 字段规范
- 主键统一使用 `id` (BIGINT, AUTO_INCREMENT)
- 外键使用 `关联表_id` 格式
- 布尔值使用 `is_` 或 `has_` 前缀
- 日期时间使用 `DATETIME` 类型

### 审计字段
所有业务表必须包含:
- `created_time` - 创建时间
- `updated_time` - 更新时间
- `created_by` - 创建人 (可选)
- `updated_by` - 更新人 (可选)

## API设计规范

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1698765432000
}
```

### RESTful路径设计
- `GET /api/elderly` - 获取老人列表
- `GET /api/elderly/{id}` - 获取老人详情
- `POST /api/elderly` - 创建老人
- `PUT /api/elderly/{id}` - 更新老人
- `DELETE /api/elderly/{id}` - 删除老人

### 权限验证
所有接口使用 `@PreAuthorize` 注解进行权限验证

## 部署架构

### 开发环境
- 前端: Vite Dev Server (localhost:3000)
- 后端: Spring Boot (localhost:8080)
- 数据库: MySQL 8.0
- 缓存: Redis

### 生产环境
- 前端: Nginx 静态部署
- 后端: Spring Boot JAR包部署
- 数据库: MySQL 主从集群
- 缓存: Redis 集群
- 反向代理: Nginx
- 负载均衡: Nginx

## 相关文档

- [README.md](../README.md) - 项目说明
- [DATABASE_SETUP.md](./DATABASE_SETUP.md) - 数据库设置
- [WORKSPACE_SETUP.md](./WORKSPACE_SETUP.md) - 工作区配置
- [GIT_COMMIT_GUIDE.md](./GIT_COMMIT_GUIDE.md) - Git提交规范
- [RBAC权限系统说明.md](./RBAC权限系统说明.md) - 权限系统文档
- [前端开发指南](./development/前端开发指南.md) - 前端开发规范

---

**最后更新**: 2025-10-28
````
