import request from '@/utils/request';
import {
  batchDeleteMockUsers,
  checkMockEmail,
  checkMockUsername,
  createMockUser,
  deleteMockUser,
  getMockUserPage,
  mockDepartments,
  mockRoles,
  toggleMockUserStatus,
  updateMockUser
} from './mockSystem';

/**
 * 系统管理相关API
 */

// 开发模式控制
const isDev = false; // 是否使用Mock数据，生产环境设为false

/**
 * 获取用户列表（新版本，直接从数据库获取）
 * @param {Object} params - 查询参数
 */
export function fetchUserList(params) {
  return request({
    url: '/temp/list-users',
    method: 'get',
    params
  })
}

/**
 * 获取用户列表（原版本）
 * @param {Object} params - 查询参数
 * @param {string} params.keyword - 关键词
 * @param {number} params.status - 状态
 * @param {number} params.roleId - 角色ID
 * @param {number} params.current - 当前页
 * @param {number} params.size - 页面大小
 */
export function getUserList(params) {
  if (isDev) {
    return Promise.resolve(getMockUserPage(params))
  }
  return request({
    url: '/system/users',
    method: 'get',
    params
  })
}

/**
 * 创建用户
 * @param {Object} data - 用户数据
 */
export function createUser(data) {
  if (isDev) {
    return Promise.resolve(createMockUser(data))
  }
  return request({
    url: '/system/users',
    method: 'post',
    data
  })
}

/**
 * 更新用户
 * @param {number} id - 用户ID
 * @param {Object} data - 用户数据
 */
export function updateUser(id, data) {
  if (isDev) {
    return Promise.resolve(updateMockUser(id, data))
  }
  return request({
    url: `/system/users/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除用户
 * @param {number} id - 用户ID
 */
export function deleteUser(id) {
  if (isDev) {
    return Promise.resolve(deleteMockUser(id))
  }
  return request({
    url: `/system/users/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除用户
 * @param {Array} ids - 用户ID数组
 */
export function batchDeleteUsers(ids) {
  if (isDev) {
    return Promise.resolve(batchDeleteMockUsers(ids))
  }
  return request({
    url: '/system/users/batch',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 切换用户状态
 * @param {number} id - 用户ID
 * @param {number} status - 状态 (0:禁用, 1:启用)
 */
export function toggleUserStatus(id, status) {
  if (isDev) {
    return Promise.resolve(toggleMockUserStatus(id, status))
  }
  return request({
    url: `/system/users/${id}/status`,
    method: 'put',
    params: { status }
  })
}

/**
 * 重置用户密码
 * @param {number} id - 用户ID
 */
export function resetUserPassword(id) {
  if (isDev) {
    return Promise.resolve({ code: 200, message: '密码重置成功' })
  }
  return request({
    url: `/system/users/${id}/password/reset`,
    method: 'put'
  })
}

/**
 * 获取角色列表
 */
export function getRoleList() {
  if (isDev) {
    return Promise.resolve({
      code: 200,
      message: '查询成功',
      data: mockRoles
    })
  }
  return request({
    url: '/system/roles',
    method: 'get'
  })
}

/**
 * 获取部门列表
 */
export function getDepartmentList() {
  if (isDev) {
    return Promise.resolve({
      code: 200,
      message: '查询成功',
      data: mockDepartments
    })
  }
  return request({
    url: '/system/departments',
    method: 'get'
  })
}

/**
 * 导出用户数据
 * @param {Object} params - 导出参数
 */
export function exportUsers(params) {
  return request({
    url: '/system/users/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

/**
 * 获取用户详情
 * @param {number} id - 用户ID
 */
export function getUserDetail(id) {
  return request({
    url: `/system/users/${id}`,
    method: 'get'
  })
}

/**
 * 检查用户名是否可用
 * @param {string} username - 用户名
 * @param {number} excludeId - 排除的用户ID（编辑时使用）
 */
export function checkUsername(username, excludeId = null) {
  if (isDev) {
    return Promise.resolve(checkMockUsername(username, excludeId))
  }
  return request({
    url: '/system/users/check-username',
    method: 'get',
    params: { username }
  })
}

/**
 * 检查邮箱是否可用
 * @param {string} email - 邮箱
 * @param {number} excludeId - 排除的用户ID（编辑时使用）
 */
export function checkEmail(email, excludeId = null) {
  if (isDev) {
    return Promise.resolve(checkMockEmail(email, excludeId))
  }
  return request({
    url: '/system/users/check-email',
    method: 'get',
    params: { email }
  })
}

/**
 * 获取用户操作日志
 * @param {number} userId - 用户ID
 * @param {Object} params - 查询参数
 */
export function getUserOperationLogs(userId, params) {
  return request({
    url: `/system/users/${userId}/logs`,
    method: 'get',
    params
  })
}
