# 智慧医养大数据公共服务平台 - GitHub Copilot 指令

## 项目概述
智慧医养大数据公共服务平台的云端后台管理系统,面向老年人医疗和养老服务,采用医养结合的服务模式。

## 技术栈
### 后端技术栈
- Spring Boot 2.7.18 + Spring MVC + Spring Security
- MyBatis-Plus 3.5.3.1 (ORM框架,**不是JPA**)
- MySQL 8.0.33
- Redis 6.x
- JWT认证 + RBAC权限系统

### 前端技术栈
- Vue 3.4.0 (使用Composition API)
- Element Plus 2.4.4 (UI组件库)
- Vite 5.0.10 (构建工具)
- Pinia 2.1.7 (状态管理)
- Vue Router 4.x
- Axios 1.6.x

### API文档
- SpringDoc OpenAPI 1.6.15 (使用 `@Tag`, `@Operation` 注解,**不是Swagger 2**)

### 构建工具
- 后端: Maven 3.8+
- 前端: npm / pnpm
- 容器化: Docker + Docker Compose

## 核心业务实体
- **老人(Elderly)**: 系统服务的主要对象,包含基础信息、健康档案、家庭关系
- **医生(Doctor)**: 提供医疗服务的专业人员
- **健康记录(HealthRecord)**: 老人的健康数据记录,包括体征数据、检查报告
- **健康预警(HealthWarning)**: 健康异常的预警信息,分级别处理
- **设备(Equipment)**: 医疗监测设备台账

## 功能模块
1. **首页仪表板**: 数据概览、关键指标、快捷操作、实时预警
2. **老人档案管理**: 个人信息、健康档案、家族病史、紧急联系人
3. **健康预警系统**: 实时监控、异常预警、分级处理、处理流程跟踪
4. **评估报告**: 健康评估、定期体检、风险评估、康复进度
5. **重点人群管理**: 高风险筛选、慢性病管理、分级护理
6. **报表统计分析**: 健康数据统计、服务质量分析、趋势预测
7. **设备管理系统**: 设备台账、状态监控、维护记录
8. **老人账户管理**: 账户创建、权限设置、信息维护
9. **医生账户管理**: 资质认证、科室分配、权限管理
10. **大数据决策分析**: 健康趋势、服务效果、资源配置优化
11. **用户账号管理**: 系统用户管理、角色分配
12. **个人中心**: 用户信息、密码修改、操作日志

## 构建和验证
### 后端构建
```bash
cd backend
mvn clean compile        # 编译
mvn spring-boot:run     # 运行开发服务器(端口8080)
mvn test                # 运行测试
mvn clean package       # 打包(生成jar)
```

### 前端构建
```bash
cd frontend
npm install             # 安装依赖
npm run dev            # 运行开发服务器(端口5173)
npm run build          # 生产构建
npm run test           # 运行测试
```

### Docker部署
```bash
docker-compose up -d    # 启动所有服务(MySQL, Redis, 后端, 前端)
```

## 项目结构
### 后端结构
```
backend/src/main/java/com/smartcare/cloud/
├── SmartCareCloudApplication.java  # 启动类
├── config/                         # 配置类
│   ├── WebMvcConfig.java
│   ├── MyBatisPlusConfig.java
│   ├── SecurityConfig.java
│   └── RedisConfig.java
├── controller/                     # 控制器层
├── service/                        # 业务逻辑层
├── mapper/                         # MyBatis-Plus Mapper接口
├── entity/                         # 数据库实体
├── dto/                           # 数据传输对象
├── vo/                            # 视图对象
├── utils/                         # 工具类
└── exception/                     # 异常处理
```

### 前端结构
```
frontend/src/
├── main.js                # 入口文件
├── App.vue               # 根组件
├── router/               # Vue Router配置
├── store/                # Pinia状态管理
├── views/                # 页面组件
├── components/           # 公共组件
├── api/                  # API接口
├── utils/                # 工具函数
└── styles/               # 全局样式
```

## 开发规范
### Java代码规范
- **类名**: PascalCase (例: `ElderlyController`, `HealthWarningService`)
- **方法名**: camelCase (例: `getElderlyInfo`, `createHealthRecord`)
- **常量**: UPPER_SNAKE_CASE (例: `MAX_AGE_LIMIT`, `DEFAULT_PAGE_SIZE`)
- **包名**: 全小写,单词间用点分隔
- **注释**: 所有公共方法必须包含完整的Javadoc注释

### 数据库规范
- **表名**: snake_case (例: `elderly_info`, `health_records`)
- **字段名**: snake_case (例: `created_time`, `updated_time`)
- **统一审计字段**: 所有表包含 `created_time`, `updated_time`, `created_by`, `updated_by`
- **主键**: 统一使用 `id` 字段,自增长

### API设计规范
- **RESTful风格**: GET查询, POST新增, PUT更新, DELETE删除
- **统一返回格式**: 使用 `ResponseResult<T>` 封装,包含 `code`, `message`, `data`
- **分页参数**: 使用 `Page<T>` 封装,包含 `current`, `size`, `total`, `records`
- **错误码**: 统一错误码体系,业务错误码以模块划分

### 前端代码规范
- **组件名**: PascalCase (例: `ElderlyList.vue`, `HealthWarning.vue`)
- **文件名**: kebab-case (例: `elderly-list.vue`, `health-warning.vue`)
- **变量/函数**: camelCase (例: `elderlyData`, `fetchElderlyList`)
- **常量**: UPPER_SNAKE_CASE (例: `API_BASE_URL`)
- **使用**: Vue 3 Composition API (`<script setup>`)
- **UI组件**: Element Plus组件

### 安全规范
- **数据加密**: 敏感数据(密码、身份证号)必须加密存储
- **权限验证**: 所有接口需要权限验证,使用RBAC模型
- **参数校验**: 所有输入参数必须进行校验
- **信息脱敏**: 日志和前端展示中敏感信息必须脱敏

### 性能规范
- **分页查询**: 所有列表查询必须分页,默认每页20条
- **缓存策略**: 使用Redis缓存热点数据
- **数据库索引**: 合理创建索引优化查询性能
- **异步处理**: 耗时操作使用异步处理

### 测试规范
- **测试覆盖率**: 核心业务代码测试覆盖率≥80%
- **单元测试**: 使用JUnit 5 + Mockito
- **集成测试**: 使用Spring Boot Test
- **前端测试**: 使用Vitest + Vue Test Utils

## MyBatis-Plus代码示例
### 实体类示例
```java
@Data
@TableName("elderly_info")
public class ElderlyInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private Integer age;
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
```

### Mapper接口示例
```java
@Mapper
public interface ElderlyMapper extends BaseMapper<ElderlyInfo> {
    // 继承BaseMapper获得基础CRUD方法
    // 自定义SQL方法写在对应的XML文件中
}
```

## SpringDoc OpenAPI注解示例
### Controller注解
```java
@Tag(name = "老人管理", description = "老人信息的增删改查接口")
@RestController
@RequestMapping("/api/elderly")
public class ElderlyController {
    
    @Operation(summary = "获取老人列表", description = "分页查询老人信息列表")
    @GetMapping("/list")
    public ResponseResult<Page<ElderlyVO>> list(
        @Parameter(description = "页码") @RequestParam int current,
        @Parameter(description = "每页数量") @RequestParam int size
    ) {
        // ...
    }
}
```

## 专用提示文件
使用 `.github/prompts/` 目录下的专用提示文件进行特定任务开发:
- `backend-controller.md`: 后端Controller开发
- `backend-service.md`: 后端Service业务逻辑开发
- `frontend-page.md`: 前端页面组件开发
- `frontend-api.md`: 前端API接口封装
