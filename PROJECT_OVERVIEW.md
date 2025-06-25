# 智慧医养大数据公共服务平台 - 项目概述

## 项目描述
这是一个为老年人医疗和养老服务设计的综合性云端管理平台，实现医养结合的数字化服务模式。

## 核心功能
- 老人档案管理和健康监测
- 实时健康预警和风险评估
- 医疗设备管理和数据分析
- 用户权限控制和安全管理

## 技术架构
```
前端 (Vue.js 3 + Element Plus)
    ↓ HTTP/HTTPS
后端 (Spring Boot + MyBatis)
    ↓ JDBC
数据库 (MySQL 8.0 + Redis)
```

## 项目结构
```
SmartCare_Cloud/
├── backend/                    # Spring Boot后端
│   ├── src/main/java/com/smartcare/cloud/
│   │   ├── controller/         # REST API控制器
│   │   ├── service/           # 业务逻辑层
│   │   ├── mapper/            # 数据访问层
│   │   ├── entity/            # JPA实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── vo/                # 视图对象
│   │   ├── config/            # 配置类
│   │   ├── utils/             # 工具类
│   │   └── exception/         # 异常处理
│   └── src/main/resources/
│       ├── mapper/            # MyBatis映射文件
│       ├── application.yml    # 应用配置
│       └── sql/               # 数据库脚本
├── frontend/                  # Vue.js前端
│   ├── src/
│   │   ├── views/             # 页面组件
│   │   ├── components/        # 公共组件
│   │   ├── api/               # API接口
│   │   ├── router/            # 路由配置
│   │   ├── store/             # Pinia状态管理
│   │   └── utils/             # 工具函数
│   └── public/                # 静态资源
├── sql/                       # 数据库脚本
├── docs/                      # 项目文档
└── docker/                    # Docker配置
```

## 开发规范
- 后端使用RESTful API设计
- 前端采用组件化开发
- 数据库表名使用下划线命名
- Java类名使用PascalCase
- 所有接口需要权限验证
