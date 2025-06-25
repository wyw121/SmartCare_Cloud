import request from '@/utils/request'

/**
 * 系统管理相关API
 */

/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 * @param {string} params.keyword - 关键词
 * @param {number} params.status - 状态
 * @param {number} params.roleId - 角色ID
 * @param {number} params.current - 当前页
 * @param {number} params.size - 页面大小
 */
export function getUserList(params) {
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
  return request({
    url: `/system/users/${id}/status`,
    method: 'patch',
    data: { status }
  })
}

/**
 * 重置用户密码
 * @param {number} id - 用户ID
 * @param {string} newPassword - 新密码
 */
export function resetUserPassword(id, newPassword) {
  return request({
    url: `/system/users/${id}/password`,
    method: 'patch',
    data: { password: newPassword }
  })
}

/**
 * 获取角色列表
 */
export function getRoleList() {
  return request({
    url: '/system/roles',
    method: 'get'
  })
}

/**
 * 获取部门列表
 */
export function getDepartmentList() {
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
  return request({
    url: '/system/users/check-username',
    method: 'post',
    data: { username, excludeId }
  })
}

/**
 * 检查邮箱是否可用
 * @param {string} email - 邮箱
 * @param {number} excludeId - 排除的用户ID（编辑时使用）
 */
export function checkEmail(email, excludeId = null) {
  return request({
    url: '/system/users/check-email',
    method: 'post',
    data: { email, excludeId }
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
