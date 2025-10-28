# 脚本工具说明

本目录包含项目开发和维护的辅助脚本。

## 📋 脚本列表

### PowerShell脚本

#### `add-lombok-annotations.ps1`
**用途**: 批量为Java类添加Lombok注解  
**功能**:
- 自动扫描Java文件
- 添加@Data、@Getter、@Setter等注解
- 简化代码，减少样板代码

**使用方式**:
```powershell
.\add-lombok-annotations.ps1
```

#### `family_health_warning_test.ps1`
**用途**: 家属健康预警功能测试脚本  
**功能**:
- 测试健康预警API接口
- 模拟家属端查看预警
- 验证权限控制

**使用方式**:
```powershell
.\family_health_warning_test.ps1
```

## ⚠️ 注意事项

- 所有脚本仅用于开发和测试环境
- 执行前请仔细阅读脚本内容
- 建议在虚拟机或测试环境中运行
- PowerShell执行策略设置：
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```

## 🔧 环境要求

- PowerShell 5.1 或更高版本
- Windows 10/11 或 Windows Server
- 项目后端服务需运行在 localhost:8080
