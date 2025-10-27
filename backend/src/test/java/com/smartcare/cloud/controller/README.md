# 测试控制器说明

本目录包含**仅供开发和测试使用**的控制器，这些控制器已从生产代码目录移动到测试目录。

## 📋 文件列表

### 1. TestController.java
- **用途**: 基础测试控制器
- **功能**: 验证系统基本功能
- **Profile**: `@Profile("dev")`
- **映射路径**: `/api/test/*`

### 2. TestDataController.java
- **用途**: 测试数据初始化
- **功能**: 批量创建测试数据（老人、医生、家属等）
- **Profile**: `@Profile("dev")`
- **映射路径**: `/test/*`

### 3. TempUserController.java
- **用途**: 临时用户添加工具
- **功能**: 快速添加测试用户账号
- **Profile**: `@Profile("dev")`
- **映射路径**: `/api/temp/*`

### 4. DatabaseManagementController.java
- **用途**: 数据库管理工具
- **功能**: 表初始化、数据维护、状态检查
- **Profile**: `@Profile("dev")`
- **映射路径**: `/api/database-management/*`
- **⚠️ 警告**: 包含危险操作（DROP TABLE等），仅限开发环境使用

## 🚫 生产环境安全

所有控制器都使用 `@Profile("dev")` 注解，确保：
- ✅ **开发环境(dev)**: 自动加载，可正常使用
- ❌ **生产环境(prod)**: 自动排除，不会被加载

## 🔧 使用方法

### 开发环境启动
```bash
# application.yml 配置
spring:
  profiles:
    active: dev
```

### 如需在开发中使用
这些控制器在开发环境下仍可正常访问：
- http://localhost:8080/api/test/elderly/all
- http://localhost:8080/test/init-elderly-data
- http://localhost:8080/api/temp/add-test-users
- http://localhost:8080/api/database-management/init

### 生产环境打包
使用 Maven 打包时，test 目录的代码不会被包含：
```bash
mvn clean package -Pprofile=prod
```

## 📝 迁移日志

**迁移时间**: 2025-10-27  
**原因**: 代码清理，将测试代码与生产代码分离  
**原路径**: `src/main/java/com/smartcare/cloud/controller/`  
**新路径**: `src/test/java/com/smartcare/cloud/controller/`  
**影响**: 无，Profile控制确保生产环境不受影响

## ⚠️ 注意事项

1. **不要在生产环境使用这些接口**
2. **DatabaseManagementController 包含危险操作**
3. **测试完成后记得清理测试数据**
4. **生产打包前确认 Profile 设置为 prod**

## 🔗 相关文档

- [项目深度状态分析报告](../../../../../../../项目深度状态分析报告_最终版.md)
- [Spring Profiles 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
