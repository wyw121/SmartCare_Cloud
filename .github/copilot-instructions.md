# 智慧医养大数据公共服务平台 - GitHub Copilot 指令

## 项目概述
智慧医养大数据公共服务平台的云端后台管理系统，面向老年人医疗和养老服务，采用医养结合的服务模式。

## 技术栈
- 后端：Spring Boot 2.7.18 + MyBatis-Plus + MySQL 8.0
- 前端：Vue.js 3 + Element Plus + Vite
- 缓存：Redis
- 文档：Swagger/OpenAPI

## 核心业务实体
老人(Elderly)、医生(Doctor)、健康记录(HealthRecord)、预警信息(HealthWarning)、设备(Equipment)。

## 功能模块
首页仪表板、老人档案、健康预警、评估报告、重点人群、报表统计、设备管理、老人账户管理、医生账户管理、大数据决策分析、用户账号管理、个人中心。

## 开发规范
- Java：PascalCase类名，camelCase方法名，完整Javadoc注释
- 数据库：snake_case表名，统一审计字段
- API：RESTful设计，统一ResponseResult返回格式
- 前端：Vue 3 Composition API，Element Plus组件
- 安全：数据加密、权限验证、参数校验、信息脱敏
- 性能：分页查询、Redis缓存、索引优化、异步处理
- 测试：80%覆盖率，单元测试+集成测试

## 提示文件
使用 `.github/prompts/` 目录下的专用提示文件进行特定任务开发。
