---
applyTo: "backend/**/*.java"
---

# 后端开发特定指令 - Java/Spring Boot

## 适用范围
本指令适用于 `backend/` 目录下的所有Java源代码文件。

## 必须使用的技术栈

### ORM框架
- **使用**: MyBatis-Plus 3.5.3.1
- **禁止**: 不要使用JPA注解 (`@Entity`, `@Table`, `@Column`, `@ManyToOne` 等)
- **注解**: 使用 `@TableName`, `@TableId`, `@TableField`

### API文档
- **使用**: SpringDoc OpenAPI 1.6.15
- **禁止**: 不要使用Swagger 2注解 (`@Api`, `@ApiOperation`, `@ApiParam` 等)
- **注解**: 使用 `@Tag`, `@Operation`, `@Parameter`

## 实体类开发规范

### 实体类模板
```java
/**
 * {实体描述}
 * 
 * @author {作者}
 * @date {日期}
 */
@Data
@TableName("{表名}")
public class {实体名} {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 业务字段...
    
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;
    
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
}
```

## Mapper接口开发规范

### Mapper接口模板
```java
/**
 * {实体名}数据访问层
 * 
 * @author {作者}
 * @date {日期}
 */
@Mapper
public interface {实体名}Mapper extends BaseMapper<{实体名}> {
    
    /**
     * 自定义查询方法(需要在XML中实现)
     * 
     * @param param 查询参数
     * @return 查询结果
     */
    List<{实体名}> selectCustom(@Param("param") {参数类型} param);
}
```

## Controller开发规范

### Controller模板
```java
/**
 * {模块名}控制器
 * 
 * @author {作者}
 * @date {日期}
 */
@Tag(name = "{模块名}", description = "{模块描述}")
@RestController
@RequestMapping("/api/{模块路径}")
@Slf4j
public class {模块名}Controller {
    
    @Autowired
    private {模块名}Service service;
    
    @Operation(summary = "{操作描述}", description = "{详细说明}")
    @GetMapping("/list")
    public ResponseResult<Page<{实体}VO>> list(
        @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
        @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") Integer size,
        @Parameter(description = "查询条件") {查询DTO} query
    ) {
        Page<{实体}VO> page = service.page(current, size, query);
        return ResponseResult.success(page);
    }
    
    @Operation(summary = "新增{实体}")
    @PostMapping
    public ResponseResult<Long> create(@RequestBody @Validated {实体}DTO dto) {
        Long id = service.create(dto);
        return ResponseResult.success(id);
    }
    
    @Operation(summary = "更新{实体}")
    @PutMapping("/{id}")
    public ResponseResult<Void> update(
        @PathVariable Long id,
        @RequestBody @Validated {实体}DTO dto
    ) {
        service.update(id, dto);
        return ResponseResult.success();
    }
    
    @Operation(summary = "删除{实体}")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseResult.success();
    }
}
```

## Service开发规范

### Service接口模板
```java
/**
 * {模块名}业务逻辑接口
 * 
 * @author {作者}
 * @date {日期}
 */
public interface {模块名}Service {
    
    /**
     * 分页查询
     */
    Page<{实体}VO> page(Integer current, Integer size, {查询DTO} query);
    
    /**
     * 根据ID查询
     */
    {实体}VO getById(Long id);
    
    /**
     * 新增
     */
    Long create({实体}DTO dto);
    
    /**
     * 更新
     */
    void update(Long id, {实体}DTO dto);
    
    /**
     * 删除
     */
    void delete(Long id);
}
```

### Service实现类模板
```java
/**
 * {模块名}业务逻辑实现
 * 
 * @author {作者}
 * @date {日期}
 */
@Service
@Slf4j
public class {模块名}ServiceImpl implements {模块名}Service {
    
    @Autowired
    private {实体}Mapper mapper;
    
    @Override
    public Page<{实体}VO> page(Integer current, Integer size, {查询DTO} query) {
        Page<{实体}> page = new Page<>(current, size);
        
        LambdaQueryWrapper<{实体}> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        if (StringUtils.hasText(query.getName())) {
            wrapper.like({实体}::getName, query.getName());
        }
        
        mapper.selectPage(page, wrapper);
        
        // 转换为VO
        return page.convert(entity -> {
            {实体}VO vo = new {实体}VO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create({实体}DTO dto) {
        {实体} entity = new {实体}();
        BeanUtils.copyProperties(dto, entity);
        
        mapper.insert(entity);
        
        return entity.getId();
    }
}
```

## 关键开发要求

### 1. 异常处理
- 使用统一的业务异常类 `BusinessException`
- Controller层不捕获异常,由全局异常处理器处理
- Service层抛出明确的业务异常

### 2. 参数校验
- DTO类使用 `javax.validation` 注解进行校验
- Controller方法参数使用 `@Validated` 启用校验
- 必填字段使用 `@NotNull`, `@NotBlank`

### 3. 日志规范
- 使用 Lombok 的 `@Slf4j` 注解
- 关键业务操作记录INFO日志
- 异常记录ERROR日志,包含堆栈信息
- 敏感信息不输出到日志

### 4. 事务管理
- Service层的写操作方法添加 `@Transactional`
- 设置 `rollbackFor = Exception.class`
- 避免大事务,及时提交

### 5. 性能优化
- 分页查询默认每页20条
- 使用LambdaQueryWrapper避免字符串硬编码
- 合理使用索引
- 热点数据使用Redis缓存

## 禁止事项

❌ **禁止使用JPA注解**: `@Entity`, `@Table`, `@Column`, `@OneToMany`, `@ManyToOne` 等  
❌ **禁止使用Swagger 2注解**: `@Api`, `@ApiOperation`, `@ApiParam` 等  
❌ **禁止在Controller捕获异常**: 应由全局异常处理器统一处理  
❌ **禁止返回Entity给前端**: 必须转换为VO对象  
❌ **禁止SQL注入风险**: 使用参数化查询,避免字符串拼接SQL  

## 必须遵循

✅ **必须使用MyBatis-Plus注解**: `@TableName`, `@TableId`, `@TableField`  
✅ **必须使用SpringDoc注解**: `@Tag`, `@Operation`, `@Parameter`  
✅ **必须包含完整Javadoc注释**  
✅ **必须使用统一返回格式**: `ResponseResult<T>`  
✅ **必须进行参数校验**: 使用 `@Validated` 和 `javax.validation` 注解  
