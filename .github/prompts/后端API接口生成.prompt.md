# 后端API接口生成

## 目标
为智慧医养平台生成标准的Spring Boot REST API接口

## 必需信息
如果未提供，请询问：
- API功能描述
- 实体类名称
- 主要字段信息

## API开发规范

### 控制器结构
```java
@RestController
@RequestMapping("/api/v1/{module}")
@Api(tags = "{模块}管理")
public class {Entity}Controller {
    
    @Autowired
    private {Entity}Service {entity}Service;
    
    // CRUD操作方法
}
```

### 统一响应格式
- 使用 `ResponseResult<T>` 作为统一返回类型
- 成功：`ResponseResult.success(data)`
- 失败：`ResponseResult.error(message)`

### 参数验证
- 使用 `@Valid` 注解进行参数验证
- 使用 `@NotNull`, `@NotBlank`, `@Size` 等约束注解
- 添加详细的错误提示信息

### 权限控制
- 使用 `@PreAuthorize` 注解控制访问权限
- 按照角色分配权限：ADMIN, DOCTOR, NURSE

### 日志记录
- 在关键操作处添加日志记录
- 使用 `@Slf4j` 注解
- 记录操作用户、时间、参数

### 接口文档
- 使用 `@ApiOperation` 描述接口功能
- 使用 `@ApiParam` 描述参数
- 提供清晰的示例数据

## 生成要求
1. 包含完整的CRUD操作
2. 添加分页查询支持
3. 包含数据验证和异常处理
4. 遵循RESTful设计规范
5. 生成对应的Service和Mapper接口
