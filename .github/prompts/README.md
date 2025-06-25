# GitHub Copilot 提示文件索引

## 📁 可用的提示文件

### 🔧 开发相关
- **[后端API接口生成](./后端API接口生成.prompt.md)** - 生成标准的Spring Boot REST API
- **[前端Vue组件生成](./前端Vue组件生成.prompt.md)** - 创建Vue 3 + Element Plus组件
- **[数据库设计和SQL生成](./数据库设计和SQL生成.prompt.md)** - 设计数据库表结构和SQL脚本
- **[单元测试生成](./单元测试生成.prompt.md)** - 创建高质量的单元测试代码

### 🔍 质量保证
- **[代码审查检查清单](./代码审查检查清单.prompt.md)** - 全面的代码安全和质量审查
- **[文档生成规范](./文档生成规范.prompt.md)** - 标准化技术文档生成

## 🎯 使用方法

### 在VS Code中使用
1. 打开Copilot Chat面板
2. 使用 `#file:` 语法引用提示文件
3. 例如：`#file:.github/prompts/后端API接口生成.prompt.md 帮我生成老人信息管理的API接口`

### 常用组合示例
```
# 完整的功能开发流程
1. #file:.github/prompts/数据库设计和SQL生成.prompt.md 设计老人健康记录表
2. #file:.github/prompts/后端API接口生成.prompt.md 生成健康记录管理API
3. #file:.github/prompts/前端Vue组件生成.prompt.md 创建健康记录管理页面
4. #file:.github/prompts/单元测试生成.prompt.md 为API生成测试代码
5. #file:.github/prompts/代码审查检查清单.prompt.md 审查代码质量
```

## 🔄 更新记录
- 2025-06-25: 创建初始版本的6个提示文件
- 包含完整的开发工作流支持
- 覆盖后端、前端、数据库、测试、审查、文档各个环节

## 💡 提示
- 提示文件可以组合使用，形成完整的开发工作流
- 根据项目需要，可以继续添加新的提示文件
- 定期更新提示文件内容，保持与项目发展同步
