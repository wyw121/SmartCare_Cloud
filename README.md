# 智慧医养大数据公共服务平台云端后台管理系统

[![GitHub Copilot](https://img.shields.io/badge/GitHub%20Copilot-Enabled-blue)](https://github.com/features/copilot)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.x-green)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-brightgreen)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)](https://www.mysql.com/)

## 📋 项目概述

智慧医养大数据公共服务平台是一个面向老年人医疗和养老服务的综合性云端管理系统，通过数字化手段提升医养服务质量，为老年人提供更好的健康管理和生活照护服务。

### 🎯 核心目标
- 建立完整的老人健康档案管理体系
- 实现健康数据的实时监控和智能预警
- 提供基于大数据的决策支持分析
- 优化医养资源配置和服务效率
- 保障老年人医疗数据安全和隐私保护

## 🏗️ 系统架构

### 技术栈
- **后端**: Spring Boot 2.7.x + Spring MVC + MyBatis + MySQL 8.0+
- **前端**: Vue.js 3.x + Element Plus + Pinia + Vite
- **缓存**: Redis 6.x
- **构建工具**: Maven 3.x
- **容器化**: Docker + Docker Compose
- **代理服务器**: Nginx
- **开发工具**: GitHub Copilot + VS Code

### 系统架构图
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端(Vue.js)   │────│  后端(Spring)   │────│  数据库(MySQL)  │
│                 │    │                 │    │                 │
│ - 用户界面      │    │ - 业务逻辑      │    │ - 数据存储      │
│ - 状态管理      │    │ - API接口       │    │ - 数据安全      │
│ - 路由管理      │    │ - 权限控制      │    │ - 备份恢复      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   缓存(Redis)   │
                    │                 │
                    │ - 会话存储      │
                    │ - 数据缓存      │
                    │ - 实时数据      │
                    └─────────────────┘
```

## 🚀 功能模块

### 📊 核心功能
| 模块           | 功能描述                     | 状态     |
| -------------- | ---------------------------- | -------- |
| 🏠 首页仪表板   | 系统概览、关键指标、快捷操作 | 🔄 开发中 |
| 👴 老人档案管理 | 个人信息、健康档案、家庭信息 | 🔄 开发中 |
| ⚠️ 健康预警系统 | 实时监控、异常预警、处理流程 | 🔄 开发中 |
| 📋 评估报告     | 健康评估、风险分析、康复跟踪 | 📋 计划中 |
| 👥 重点人群管理 | 高风险筛选、分级护理         | 📋 计划中 |
| 📈 报表统计     | 数据分析、趋势预测、决策支持 | 📋 计划中 |
| 🔧 设备管理     | 设备台账、状态监控、维护记录 | 📋 计划中 |
| 👤 用户管理     | 老人账户、医生账户、权限控制 | 🔄 开发中 |
| 🧠 大数据分析   | 健康趋势、服务效果、资源优化 | 📋 计划中 |
| ⚙️ 个人中心     | 用户信息、安全设置、操作日志 | 📋 计划中 |

## 📁 项目结构

```
SmartCare_Cloud/
├── .github/                          # GitHub配置文件
│   ├── copilot_instructions.md       # Copilot项目指导文档
│   ├── .prompt.md                    # Copilot开发提示
│   └── .instructions.md              # Copilot使用指引
├── backend/                          # 后端项目
│   ├── src/main/java/com/smartcare/cloud/
│   │   ├── SmartCareCloudApplication.java
│   │   ├── config/                   # 配置类
│   │   ├── controller/               # 控制器层
│   │   ├── service/                  # 业务逻辑层
│   │   ├── mapper/                   # 数据访问层
│   │   ├── entity/                   # 实体类
│   │   ├── dto/                      # 数据传输对象
│   │   ├── vo/                       # 视图对象
│   │   ├── utils/                    # 工具类
│   │   └── exception/                # 异常处理
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis映射文件
│   │   ├── application.yml           # 应用配置
│   │   └── sql/                      # 数据库脚本
│   └── pom.xml                       # Maven依赖配置
├── frontend/                         # 前端项目
│   ├── src/
│   │   ├── main.js                   # 入口文件
│   │   ├── router/                   # 路由配置
│   │   ├── store/                    # Pinia状态管理
│   │   ├── views/                    # 页面组件
│   │   ├── components/               # 公共组件
│   │   ├── api/                      # API接口
│   │   ├── utils/                    # 工具函数
│   │   └── assets/                   # 静态资源
│   ├── package.json                  # 依赖配置
│   └── vite.config.js               # Vite配置
├── docker/                          # Docker配置
├── docs/                            # 项目文档
├── sql/                             # 数据库脚本
├── README.md                        # 项目说明
└── docker-compose.yml               # 容器编排
```

## 🛠️ 快速开始

### 环境要求
- **Java**: JDK 8+
- **Node.js**: 16.x+
- **MySQL**: 8.0+
- **Redis**: 6.x+
- **Maven**: 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/your-username/SmartCare_Cloud.git
cd SmartCare_Cloud
```

2. **数据库初始化**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE smartcare_cloud CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
mysql -u root -p smartcare_cloud < sql/init.sql
```

3. **后端启动**
```bash
cd backend
# 修改application.yml中的数据库配置
mvn clean install
mvn spring-boot:run
```

4. **前端启动**
```bash
cd frontend
npm install
npm run dev
```

5. **访问系统**
- 前端地址: http://localhost:3000
- 后端API: http://localhost:8080
- API文档: http://localhost:8080/swagger-ui.html

## 🔧 开发指南

### GitHub Copilot 配置
本项目已完整配置GitHub Copilot开发环境，包含：

- **📋 copilot_instructions.md**: 项目整体指导文档
- **💡 .prompt.md**: 开发提示和最佳实践
- **📖 .instructions.md**: 详细的使用指引

使用Copilot开发时，请参考`.github`目录下的配置文档，以获得最佳的代码生成效果。

### 开发规范

#### 后端开发
```java
// 控制器命名规范
@RestController
@RequestMapping("/api/elderly")
@Api(tags = "老人档案管理")
public class ElderlyController {
    
    @Autowired
    private ElderlyService elderlyService;
    
    @GetMapping("/{id}")
    @ApiOperation("获取老人详细信息")
    public ResponseResult<ElderlyDetailVO> getElderlyDetail(@PathVariable Long id) {
        return ResponseResult.success(elderlyService.getElderlyDetail(id));
    }
}
```

#### 前端开发
```vue
<template>
  <div class="elderly-management">
    <el-card class="page-card">
      <!-- 页面内容 -->
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { elderlyApi } from '@/api/elderly'

// 组件逻辑
const elderlyList = ref([])

const loadElderlyList = async () => {
  try {
    const response = await elderlyApi.getElderlyList()
    elderlyList.value = response.data
  } catch (error) {
    console.error('加载老人列表失败:', error)
  }
}

onMounted(() => {
  loadElderlyList()
})
</script>
```

### API接口规范
```json
// 统一响应格式
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2025-06-25T10:00:00Z"
}

// 分页响应格式
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 20,
    "records": []
  }
}
```

## 🔐 安全说明

### 数据安全
- 敏感数据加密存储
- API接口权限控制
- SQL注入防护
- XSS攻击防护

### 隐私保护
- 个人信息脱敏处理
- 访问日志记录
- 数据备份加密
- 合规性检查

## 📊 性能优化

### 后端优化
- Redis缓存热点数据
- 数据库索引优化
- 分页查询优化
- 异步处理耗时操作

### 前端优化
- 组件懒加载
- 图片懒加载
- 虚拟滚动
- CDN静态资源

## 🧪 测试

### 单元测试
```bash
# 后端测试
cd backend
mvn test

# 前端测试
cd frontend
npm run test
```

### 集成测试
```bash
# API接口测试
npm run test:api

# 端到端测试
npm run test:e2e
```

## 📦 部署

### Docker部署
```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d
```

### 生产环境
```bash
# 后端打包
cd backend
mvn clean package -P prod

# 前端构建
cd frontend
npm run build
```

## 📚 文档

- [API文档](docs/api.md)
- [数据库设计](docs/database.md)
- [部署指南](docs/deployment.md)
- [开发规范](docs/coding-standards.md)

## 🤝 贡献

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系我们

- 项目维护者: [Your Name](mailto:your.email@example.com)
- 项目地址: [https://github.com/your-username/SmartCare_Cloud](https://github.com/your-username/SmartCare_Cloud)
- 问题反馈: [Issues](https://github.com/your-username/SmartCare_Cloud/issues)

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！
