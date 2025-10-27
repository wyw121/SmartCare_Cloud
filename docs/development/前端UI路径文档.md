# 智慧医养平台 - 前端UI路径文档

## 📍 访问地址
- **前端服务**: http://localhost:3002
- **后端API**: http://localhost:8080/api （需要数据库）

## 🗂️ 前端页面路径映射表

### 🏠 主要入口页面
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/` | 首页重定向 | - | 自动跳转到 `/home` | [http://localhost:3002/](http://localhost:3002/) |
| `/home` | 平台首页 | `frontend/src/views/home.vue` | 智慧医养平台欢迎页 | [http://localhost:3002/home](http://localhost:3002/home) |
| `/login` | 登录页面 | `frontend/src/views/login/index.vue` | 用户登录界面 | [http://localhost:3002/login](http://localhost:3002/login) |

### 📊 仪表板 Dashboard
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/dashboard` | 首页仪表板 | `frontend/src/views/dashboard/working.vue` | 数据概览、图表可视化 | [http://localhost:3002/dashboard](http://localhost:3002/dashboard) |

### 👥 老人档案管理 Elderly
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/elderly/list` | 老人档案列表 | `frontend/src/views/elderly/list.vue` | 查看所有老人档案 | [http://localhost:3002/elderly/list](http://localhost:3002/elderly/list) |
| `/elderly/add` | 新增老人档案 | `frontend/src/views/elderly/form.vue` | 添加新的老人信息 | [http://localhost:3002/elderly/add](http://localhost:3002/elderly/add) |
| `/elderly/edit/:id` | 编辑老人档案 | `frontend/src/views/elderly/form.vue` | 编辑老人信息（需要ID） | [http://localhost:3002/elderly/edit/1](http://localhost:3002/elderly/edit/1) |
| `/elderly/profile/:id` | 老人详细档案 | `frontend/src/views/elderly/profile.vue` | 查看老人详情（需要ID） | [http://localhost:3002/elderly/profile/1](http://localhost:3002/elderly/profile/1) |

### 👨‍⚕️ 医生管理 Doctor
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/doctor/list` | 医生列表 | `frontend/src/views/doctor/list.vue` | 查看所有医生信息 | [http://localhost:3002/doctor/list](http://localhost:3002/doctor/list) |

### 🏥 健康管理 Health
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/health/warning` | 健康预警 | `frontend/src/views/health-warning/index.vue` | 健康异常预警信息 | [http://localhost:3002/health/warning](http://localhost:3002/health/warning) |
| `/health/records` | 健康记录 | `frontend/src/views/health/records.vue` | 老人健康记录管理 | [http://localhost:3002/health/records](http://localhost:3002/health/records) |
| `/health/assessment` | 评估报告 | `frontend/src/views/health/assessment.vue` | 健康评估报告 | [http://localhost:3002/health/assessment](http://localhost:3002/health/assessment) |

### 🔧 设备管理 Equipment
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/equipment/list` | 设备列表 | `frontend/src/views/equipment/list.vue` | 医疗设备清单 | [http://localhost:3002/equipment/list](http://localhost:3002/equipment/list) |
| `/equipment/monitor` | 设备监控 | `frontend/src/views/equipment/monitor.vue` | 设备状态实时监控 | [http://localhost:3002/equipment/monitor](http://localhost:3002/equipment/monitor) |

### 📈 报表统计 Reports
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/reports/statistics` | 统计报表 | `frontend/src/views/reports/statistics.vue` | 各类统计数据报表 | [http://localhost:3002/reports/statistics](http://localhost:3002/reports/statistics) |
| `/reports/analysis` | 大数据分析 | `frontend/src/views/reports/analysis.vue` | 数据挖掘和分析 | [http://localhost:3002/reports/analysis](http://localhost:3002/reports/analysis) |

### ⚙️ 系统管理 System
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/system/users` | 用户管理 | `frontend/src/views/system/users.vue` | 系统用户管理 | [http://localhost:3002/system/users](http://localhost:3002/system/users) |
| `/system/roles` | 角色管理 | `frontend/src/views/system/roles.vue` | 用户角色权限 | [http://localhost:3002/system/roles](http://localhost:3002/system/roles) |
| `/system/permissions` | 权限管理 | `frontend/src/views/system/permissions.vue` | 系统权限配置 | [http://localhost:3002/system/permissions](http://localhost:3002/system/permissions) |

### 👤 个人中心 Profile
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/profile` | 个人中心 | `frontend/src/views/profile/index.vue` | 个人信息管理 | [http://localhost:3002/profile](http://localhost:3002/profile) |

### 🧪 测试页面 (隐藏)
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/test` | 测试页面 | `frontend/src/views/test.vue` | 功能测试页面 | [http://localhost:3002/test](http://localhost:3002/test) |
| `/debug` | 调试页面 | `frontend/src/views/debug.vue` | 问题调试页面 | [http://localhost:3002/debug](http://localhost:3002/debug) |
| `/chart-test` | 图表测试 | `frontend/src/views/chart-test.vue` | ECharts图表测试 | [http://localhost:3002/chart-test](http://localhost:3002/chart-test) |
| `/simple` | 简单测试 | `frontend/src/views/simple-test.vue` | 简化功能测试 | [http://localhost:3002/simple](http://localhost:3002/simple) |

### ❌ 错误页面
| 路由路径 | 页面名称 | 文件路径 | 功能说明 | 访问链接 |
|---------|----------|----------|----------|----------|
| `/*` | 404错误 | `frontend/src/views/error/404.vue` | 页面不存在提示 | [http://localhost:3002/nonexistent](http://localhost:3002/nonexistent) |

## 🗂️ 组件文件结构映射

### 布局组件 Layout
| 组件名称 | 文件路径 | 功能说明 |
|---------|----------|----------|
| 主布局 | `frontend/src/layout/index.vue` | 整体页面布局框架 |
| 侧边栏 | `frontend/src/layout/components/Sidebar/index.vue` | 左侧导航菜单 |
| 侧边栏项 | `frontend/src/layout/components/Sidebar/SidebarItem.vue` | 菜单项组件 |
| 导航栏 | `frontend/src/layout/components/Navbar.vue` | 顶部导航栏 |
| 面包屑 | `frontend/src/layout/components/Breadcrumb.vue` | 面包屑导航 |
| 主内容区 | `frontend/src/layout/components/AppMain.vue` | 页面主体内容 |
| 页脚 | `frontend/src/layout/components/AppFooter.vue` | 页面底部 |

### API接口配置
| 文件名称 | 文件路径 | 功能说明 |
|---------|----------|----------|
| 主配置 | `frontend/src/api/index.js` | API基础配置 |
| 老人接口 | `frontend/src/api/elderly.js` | 老人档案相关API |
| 医生接口 | `frontend/src/api/doctor.js` | 医生管理相关API |
| 健康预警 | `frontend/src/api/healthWarning.js` | 健康预警相关API |

### 状态管理 Store
| 文件名称 | 文件路径 | 功能说明 |
|---------|----------|----------|
| 主配置 | `frontend/src/store/index.js` | Vuex状态管理配置 |
| 应用状态 | `frontend/src/store/app.js` | 应用全局状态 |
| 用户状态 | `frontend/src/store/user.js` | 用户信息状态 |
| 标签状态 | `frontend/src/store/tagsView.js` | 页面标签管理 |

### 工具函数 Utils
| 文件名称 | 文件路径 | 功能说明 |
|---------|----------|----------|
| 请求封装 | `frontend/src/utils/request.js` | Axios请求拦截器 |
| 表单验证 | `frontend/src/utils/validate.js` | 表单验证规则 |

### 样式文件 Styles
| 文件名称 | 文件路径 | 功能说明 |
|---------|----------|----------|
| 主样式 | `frontend/src/styles/index.scss` | 全局样式入口 |
| 变量定义 | `frontend/src/styles/variables.scss` | SCSS变量定义 |
| 布局样式 | `frontend/src/styles/layout.scss` | 布局相关样式 |
| 通用样式 | `frontend/src/styles/common.scss` | 通用组件样式 |

## 🔍 页面排查要点

### 1. 路由问题排查
- 检查 `frontend/src/router/index.js` 路由配置
- 确认组件路径是否正确
- 验证路由参数和meta信息

### 2. 组件加载问题
- 检查 `import` 语句路径
- 确认 `.vue` 文件是否存在
- 验证组件语法是否正确

### 3. 样式问题排查
- 检查 SCSS 文件导入
- 确认 Element Plus 组件样式
- 验证自定义样式覆盖

### 4. API接口问题
- 检查 `frontend/src/api/` 接口配置
- 确认后端服务是否启动
- 验证请求地址和参数

### 5. 状态管理问题
- 检查 Vuex store 配置
- 确认状态更新逻辑
- 验证组件与store绑定

## 🚀 快速测试建议

1. **先测试基础页面**: 访问 `/test` 和 `/simple` 确认基础功能
2. **测试图表功能**: 访问 `/chart-test` 验证 ECharts 集成
3. **测试主要业务**: 按模块逐一访问业务页面
4. **检查布局组件**: 确认侧边栏、导航栏等显示正常
5. **验证路由跳转**: 测试页面间导航是否正常

## 📝 问题记录模板

发现问题时，请记录：
- 访问的URL路径
- 错误现象描述
- 浏览器控制台错误信息
- 对应的Vue文件路径
- 预期的正常行为

这样可以快速定位和解决前端页面问题。
