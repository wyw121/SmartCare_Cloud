# GitHub Copilot 配置文件

## 项目上下文
- **项目名称**: 智慧医养大数据公共服务平台云端后台管理系统
- **项目类型**: 医疗健康管理系统
- **技术栈**: Spring Boot + Vue.js + MySQL + Redis
- **目标用户**: 医护人员、管理员、老年人及其家属

## 业务领域知识
### 医养结合概念
医养结合是指将医疗服务和养老服务相结合，为老年人提供持续性的健康管理和生活照护服务。

### 核心业务实体
1. **老人(Elderly)**: 系统服务的主要对象
2. **医生(Doctor)**: 提供医疗服务的专业人员
3. **护理员(Caregiver)**: 提供日常照护的工作人员
4. **健康记录(HealthRecord)**: 老人的健康数据记录
5. **预警(Warning)**: 健康异常的预警信息
6. **设备(Equipment)**: 医疗监测设备

### 业务流程
1. **入院流程**: 老人信息登记 → 健康评估 → 护理级别确定 → 分配床位和医生
2. **日常监护**: 生命体征监测 → 数据记录 → 异常预警 → 处理跟踪
3. **健康评估**: 定期体检 → 数据分析 → 风险评估 → 护理方案调整

## 代码生成模板

### 实体类模板
```java
/**
 * {实体描述}
 * 
 * @author GitHub Copilot
 * @date {当前日期}
 */
@Entity
@Table(name = "{表名}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class {实体名} {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 其他字段...
    
    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
}
```

### 控制器模板
```java
/**
 * {模块名称}控制器
 * 
 * @author GitHub Copilot
 * @date {当前日期}
 */
@RestController
@RequestMapping("/api/{模块路径}")
@Api(tags = "{模块名称}管理")
@Slf4j
public class {模块名}Controller {
    
    @Autowired
    private {模块名}Service {模块名}Service;
    
    @GetMapping
    @ApiOperation("获取{模块名称}列表")
    public ResponseResult<PageResult<{模块名}VO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            {查询参数类} queryParams) {
        try {
            PageResult<{模块名}VO> result = {模块名}Service.getPageList(page, size, queryParams);
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("获取{模块名称}列表失败", e);
            return ResponseResult.error("获取数据失败");
        }
    }
}
```

### 服务类模板
```java
/**
 * {模块名称}服务实现类
 * 
 * @author GitHub Copilot
 * @date {当前日期}
 */
@Service
@Transactional
@Slf4j
public class {模块名}ServiceImpl implements {模块名}Service {
    
    @Autowired
    private {模块名}Mapper {模块名}Mapper;
    
    @Override
    public PageResult<{模块名}VO> getPageList(Integer page, Integer size, {查询参数类} queryParams) {
        try {
            // 构建查询条件
            PageHelper.startPage(page, size);
            List<{实体类}> list = {模块名}Mapper.selectByCondition(queryParams);
            PageInfo<{实体类}> pageInfo = new PageInfo<>(list);
            
            // 转换为VO对象
            List<{模块名}VO> voList = list.stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
            
            return PageResult.<{模块名}VO>builder()
                    .total(pageInfo.getTotal())
                    .page(page)
                    .size(size)
                    .records(voList)
                    .build();
        } catch (Exception e) {
            log.error("获取{模块名称}分页数据失败", e);
            throw new BusinessException("获取数据失败");
        }
    }
}
```

### Vue组件模板
```vue
<template>
  <div class="{组件名}-container">
    <el-card class="page-card">
      <template #header>
        <div class="card-header">
          <span>{页面标题}</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="请输入关键词" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 数据表格 -->
      <el-table :data="tableData" :loading="loading" border>
        <!-- 表格列定义 -->
      </el-table>
      
      <!-- 分页组件 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const queryForm = reactive({
  keyword: ''
})
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 生命周期
onMounted(() => {
  loadData()
})

// 方法定义
const loadData = async () => {
  loading.value = true
  try {
    // API调用逻辑
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.{组件名}-container {
  padding: 20px;
}

.page-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>
```

## 数据库设计规范

### 表命名规范
- 使用下划线分隔单词
- 体现业务含义
- 避免使用保留字

### 字段设计规范
```sql
-- 基础字段模板
CREATE TABLE {表名} (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    
    -- 业务字段...
    
    -- 通用字段
    status TINYINT DEFAULT 1 COMMENT '状态：1正常，0停用',
    remark TEXT COMMENT '备注',
    created_by BIGINT COMMENT '创建人ID',
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 索引
    INDEX idx_{表名}_status (status),
    INDEX idx_{表名}_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='{表说明}';
```

## API设计规范

### RESTful接口设计
- GET /api/{resource} - 获取资源列表
- GET /api/{resource}/{id} - 获取单个资源
- POST /api/{resource} - 创建资源
- PUT /api/{resource}/{id} - 更新资源
- DELETE /api/{resource}/{id} - 删除资源

### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2025-06-25T10:00:00Z"
}
```

## 异常处理规范

### 全局异常处理器
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ResponseResult.error(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseResult.error("系统繁忙，请稍后重试");
    }
}
```

## 安全规范

### 权限注解使用
```java
@PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
@GetMapping("/{id}")
public ResponseResult<ElderlyDetailVO> getElderlyDetail(@PathVariable Long id) {
    // 方法实现
}
```

### 数据脱敏处理
```java
@JsonSerialize(using = SensitiveDataSerializer.class)
@SensitiveData(type = SensitiveType.ID_CARD)
private String idCard;
```

## 日志规范

### 日志级别使用
- ERROR: 系统错误、异常情况
- WARN: 警告信息、业务异常
- INFO: 关键业务操作、状态变更
- DEBUG: 调试信息、详细执行过程

### 日志格式
```java
// 操作日志
log.info("用户[{}]对老人档案[{}]执行了[{}]操作", userId, elderlyId, operation);

// 错误日志
log.error("获取老人健康数据失败，老人ID：{}，错误信息：{}", elderlyId, e.getMessage(), e);
```

## 性能优化建议

### 数据库优化
- 合理使用索引
- 避免SELECT *
- 使用分页查询
- 读写分离

### 缓存策略
- 热点数据缓存
- 查询结果缓存
- 会话缓存
- 合理设置过期时间

### 前端优化
- 组件懒加载
- 图片懒加载
- 虚拟滚动
- 防抖节流

## 测试规范

### 单元测试命名
```java
@Test
void should_{期望结果}_when_{测试条件}() {
    // 测试实现
}
```

### 测试覆盖率要求
- 核心业务逻辑：90%+
- 工具类方法：80%+
- 控制器方法：70%+
