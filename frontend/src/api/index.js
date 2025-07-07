import request from '@/utils/request'

// 导入各个模块的API
export * from './doctor'
export * from './elderly'
export * from './healthWarning'

/**
 * 认证相关API
 */
export const auth = {
  // 用户登录
  login(data) {
    return request({
      url: '/auth/login',
      method: 'post',
      data
    })
  },

  // 用户注册
  register(data) {
    return request({
      url: '/auth/register',
      method: 'post',
      data
    })
  },

  // 检查用户名是否可用
  checkUsername(username) {
    return request({
      url: '/auth/check-username',
      method: 'get',
      params: { username }
    })
  },

  // 检查手机号是否可用
  checkPhone(phone) {
    return request({
      url: '/auth/check-phone',
      method: 'get',
      params: { phone }
    })
  },

  // 检查邮箱是否可用
  checkEmail(email) {
    return request({
      url: '/auth/check-email',
      method: 'get',
      params: { email }
    })
  },

  // 用户登出
  logout() {
    return request({
      url: '/auth/logout',
      method: 'post'
    })
  },

  // 刷新token
  refreshToken(data) {
    return request({
      url: '/auth/refresh',
      method: 'post',
      data
    })
  },

  // 获取验证码
  getCaptcha() {
    return request({
      url: '/auth/captcha',
      method: 'get'
    })
  }
}

/**
 * 用户相关API
 */
export const user = {
  // 获取用户信息
  getInfo() {
    return request({
      url: '/user/info',
      method: 'get'
    })
  },

  // 更新用户信息
  updateInfo(data) {
    return request({
      url: '/user/info',
      method: 'put',
      data
    })
  },

  // 修改密码
  changePassword(data) {
    return request({
      url: '/user/password',
      method: 'put',
      data
    })
  },

  // 上传头像
  uploadAvatar(data) {
    return request({
      url: '/user/avatar',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

/**
 * 老人档案相关API
 */
export const elderly = {
  // 获取老人列表
  getList(params) {
    return request({
      url: '/elderly',
      method: 'get',
      params
    })
  },

  // 获取老人详情
  getDetail(id) {
    return request({
      url: `/elderly/${id}`,
      method: 'get'
    })
  },

  // 新增老人档案
  create(data) {
    return request({
      url: '/elderly',
      method: 'post',
      data
    })
  },

  // 更新老人档案
  update(id, data) {
    return request({
      url: `/elderly/${id}`,
      method: 'put',
      data
    })
  },

  // 删除老人档案
  delete(id) {
    return request({
      url: `/elderly/${id}`,
      method: 'delete'
    })
  },

  // 批量删除
  batchDelete(ids) {
    return request({
      url: '/elderly/batch',
      method: 'delete',
      data: { ids }
    })
  },

  // 导出老人数据
  export(params) {
    return request({
      url: '/elderly/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

/**
 * 健康记录相关API
 */
export const health = {
  // 获取健康记录列表
  getRecords(params) {
    return request({
      url: '/health/records',
      method: 'get',
      params
    })
  },

  // 新增健康记录
  createRecord(data) {
    return request({
      url: '/health/records',
      method: 'post',
      data
    })
  },

  // 获取健康预警列表
  getWarnings(params) {
    return request({
      url: '/health/warnings',
      method: 'get',
      params
    })
  },

  // 处理预警
  handleWarning(id, data) {
    return request({
      url: `/health/warnings/${id}/handle`,
      method: 'post',
      data
    })
  },

  // 获取评估报告
  getAssessments(params) {
    return request({
      url: '/health/assessments',
      method: 'get',
      params
    })
  },

  // 生成评估报告
  generateAssessment(data) {
    return request({
      url: '/health/assessments',
      method: 'post',
      data
    })
  }
}

/**
 * 设备管理API
 */

// 开发模式控制
const isDev = process.env.NODE_ENV === 'development';

// 模拟设备数据
const mockEquipmentList = {
  code: 200,
  data: {
    records: [
      {
        id: 1,
        deviceId: 'DEV001',
        deviceName: '智能血压计A1',
        deviceType: 'BLOOD_PRESSURE',
        brand: '欧姆龙',
        model: 'HEM-7200',
        status: 'ONLINE',
        location: '201房间',
        elderlyId: 1,
        elderlyName: '张三',
        lastDataTime: '2025-07-08 10:30:00',
        installTime: '2025-01-15',
        batteryLevel: 85,
        signalStrength: 95
      },
      {
        id: 2,
        deviceId: 'DEV002',
        deviceName: '血糖仪B2',
        deviceType: 'BLOOD_GLUCOSE',
        brand: '强生',
        model: 'OneTouch',
        status: 'ONLINE',
        location: '202房间',
        elderlyId: 2,
        elderlyName: '李四',
        lastDataTime: '2025-07-08 09:45:00',
        installTime: '2025-02-01',
        batteryLevel: 70,
        signalStrength: 88
      },
      {
        id: 3,
        deviceId: 'DEV003',
        deviceName: '智能手环C3',
        deviceType: 'SMART_WATCH',
        brand: '小米',
        model: 'Mi Band 7',
        status: 'OFFLINE',
        location: '203房间',
        elderlyId: 3,
        elderlyName: '王五',
        lastDataTime: '2025-07-07 22:15:00',
        installTime: '2025-03-10',
        batteryLevel: 15,
        signalStrength: 0
      }
    ],
    total: 89,
    current: 1,
    size: 10
  }
};

const mockEquipmentStatistics = {
  code: 200,
  data: {
    totalCount: 89,
    onlineCount: 76,
    offlineCount: 13,
    faultCount: 5,
    maintenanceCount: 2
  }
};

export const equipment = {
  // 获取设备列表
  getList(params) {
    if (isDev) {
      // 开发模式返回模拟数据
      return Promise.resolve(mockEquipmentList);
    }
    return request({
      url: '/equipment/list',
      method: 'get',
      params
    })
  },

  // 获取设备详情
  getDetail(id) {
    return request({
      url: `/equipment/${id}`,
      method: 'get'
    })
  },

  // 创建设备
  create(data) {
    return request({
      url: '/equipment',
      method: 'post',
      data
    })
  },

  // 更新设备
  update(id, data) {
    return request({
      url: `/equipment/${id}`,
      method: 'put',
      data
    })
  },

  // 删除设备
  delete(id) {
    return request({
      url: `/equipment/${id}`,
      method: 'delete'
    })
  },

  // 批量删除设备
  batchDelete(ids) {
    return request({
      url: '/equipment/batch',
      method: 'delete',
      data: { ids }
    })
  },

  // 获取设备统计信息
  getStatistics() {
    if (isDev) {
      // 开发模式返回模拟数据
      return Promise.resolve(mockEquipmentStatistics);
    }
    return request({
      url: '/equipment/statistics',
      method: 'get'
    })
  },

  // 获取设备类型统计
  getTypeStatistics() {
    return request({
      url: '/equipment/type-statistics',
      method: 'get'
    })
  },

  // 获取设备接入参数
  getIntegrationParams(deviceType) {
    return request({
      url: `/equipment/integration-params/${deviceType}`,
      method: 'get'
    })
  },

  // 测试设备连接
  testConnection(id) {
    return request({
      url: `/equipment/${id}/test-connection`,
      method: 'post'
    })
  },

  // 更新设备状态
  updateStatus(id, status) {
    return request({
      url: `/equipment/${id}/status`,
      method: 'put',
      data: { status }
    })
  },

  // 获取设备日志
  getLogs(id, params) {
    return request({
      url: `/equipment/${id}/logs`,
      method: 'get',
      params
    })
  },

  // 导出设备列表
  export(params) {
    return request({
      url: '/equipment/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

/**
 * 报表统计相关API
 */
export const reports = {
  // 获取仪表板数据
  getDashboard() {
    return request({
      url: '/reports/dashboard',
      method: 'get'
    })
  },

  // 获取统计数据
  getStatistics(params) {
    return request({
      url: '/reports/statistics',
      method: 'get',
      params
    })
  },

  // 获取分析数据
  getAnalysis(params) {
    return request({
      url: '/reports/analysis',
      method: 'get',
      params
    })
  },

  // 导出报表
  export(type, params) {
    return request({
      url: `/reports/export/${type}`,
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

/**
 * 系统管理API
 */
export const system = {
  // 用户管理
  users: {
    getList(params) {
      return request({
        url: '/system/users',
        method: 'get',
        params
      })
    },
    create(data) {
      return request({
        url: '/system/users',
        method: 'post',
        data
      })
    },
    update(id, data) {
      return request({
        url: `/system/users/${id}`,
        method: 'put',
        data
      })
    },
    delete(id) {
      return request({
        url: `/system/users/${id}`,
        method: 'delete'
      })
    }
  },

  // 角色管理
  roles: {
    getList(params) {
      return request({
        url: '/system/roles',
        method: 'get',
        params
      })
    },
    create(data) {
      return request({
        url: '/system/roles',
        method: 'post',
        data
      })
    },
    update(id, data) {
      return request({
        url: `/system/roles/${id}`,
        method: 'put',
        data
      })
    },
    delete(id) {
      return request({
        url: `/system/roles/${id}`,
        method: 'delete'
      })
    }
  },

  // 权限管理
  permissions: {
    getList() {
      return request({
        url: '/system/permissions',
        method: 'get'
      })
    },
    getTree() {
      return request({
        url: '/system/permissions/tree',
        method: 'get'
      })
    }
  }
}

// 默认导出所有API模块
export default {
  auth,
  user,
  elderly,
  health,
  equipment,
  reports,
  system
}
