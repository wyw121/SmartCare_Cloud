/**
 * 权限管理API
 */
import request from '@/utils/request'

/**
 * 获取权限树
 */
export function getPermissionTree() {
  return request({
    url: '/api/system/permissions/tree',
    method: 'get'
  })
}

/**
 * 分页查询权限
 */
export function getPermissionPage(params) {
  return request({
    url: '/api/system/permissions',
    method: 'get',
    params
  })
}

/**
 * 获取权限详情
 */
export function getPermissionById(id) {
  return request({
    url: `/api/system/permissions/${id}`,
    method: 'get'
  })
}

/**
 * 创建权限
 */
export function createPermission(data) {
  return request({
    url: '/api/system/permissions',
    method: 'post',
    data
  })
}

/**
 * 更新权限
 */
export function updatePermission(id, data) {
  return request({
    url: `/api/system/permissions/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除权限
 */
export function deletePermission(id) {
  return request({
    url: `/api/system/permissions/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除权限
 */
export function deletePermissions(ids) {
  return request({
    url: '/api/system/permissions/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 检查权限编码是否存在
 */
export function checkPermissionCode(permissionCode, excludeId) {
  return request({
    url: '/api/system/permissions/check-code',
    method: 'get',
    params: {
      permissionCode,
      excludeId
    }
  })
}

/**
 * 获取用户权限
 */
export function getUserPermissions(userId) {
  return request({
    url: `/api/system/permissions/user/${userId}`,
    method: 'get'
  })
}

/**
 * 获取角色权限
 */
export function getRolePermissions(roleId) {
  return request({
    url: `/api/system/permissions/role/${roleId}`,
    method: 'get'
  })
}
