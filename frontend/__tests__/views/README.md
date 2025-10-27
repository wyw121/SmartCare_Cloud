# 测试组件目录

本目录用于存放开发和测试期间使用的Vue组件。

## 目录结构

```
__tests__/
├── chart-test.vue              # 图表测试组件
├── debug.vue                   # 调试工具组件
├── dev-tools.vue               # 开发工具组件
├── doctor-simple-test.vue      # 医生功能简单测试
├── permission-demo.vue         # 权限演示组件
├── simple-test.vue             # 简单测试组件
├── test.vue                    # 通用测试组件
└── user-switcher.vue           # 用户切换组件
```

## 说明

- 这些组件**仅用于开发和测试**,不会在生产环境中使用
- 组件未在路由配置中注册,需要手动导入使用
- 建议在生产构建时排除此目录

## 使用方式

如需使用测试组件,可以在开发环境中手动导入:

```javascript
import ChartTest from '@/views/__tests__/chart-test.vue'
import DebugTool from '@/views/__tests__/debug.vue'
// ...
```

## 注意事项

⚠️ **不要将测试组件引入到生产代码中**
⚠️ **测试组件可能包含调试信息和未优化的代码**
