/**
 * 角色管理API
 */
import request from '@/utils/request'

/**
 * 获取所有角色列表
 */
export function getAllRoles() {
  return request({
    url: '/api/system/roles/all',
    method: 'get'
  })
}

/**
 * 分页查询角色
 */
export function getRolePage(params) {
  return request({
    url: '/api/system/roles',
    method: 'get',
    params
  })
}

/**
 * 获取角色详情
 */
export function getRoleById(id) {
  return request({
    url: `/api/system/roles/${id}`,
    method: 'get'
  })
}

/**
 * 创建角色
 */
export function createRole(data) {
  return request({
    url: '/api/system/roles',
    method: 'post',
    data
  })
}

/**
 * 更新角色
 */
export function updateRole(id, data) {
  return request({
    url: `/api/system/roles/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除角色
 */
export function deleteRole(id) {
  return request({
    url: `/api/system/roles/${id}`,
    method: 'delete'
  })
}

/**
 * 获取角色权限
 */
export function getRolePermissions(id) {
  return request({
    url: `/api/system/roles/${id}/permissions`,
    method: 'get'
  })
}

/**
 * 获取角色权限ID列表
 */
export function getRolePermissionIds(id) {
  return request({
    url: `/api/system/roles/${id}/permission-ids`,
    method: 'get'
  })
}

/**
 * 分配角色权限
 */
export function assignPermissions(id, permissionIds) {
  return request({
    url: `/api/system/roles/${id}/permissions`,
    method: 'post',
    data: permissionIds
  })
}

/**
 * 检查角色编码是否存在
 */
export function checkRoleCode(roleCode, excludeId) {
  return request({
    url: '/api/system/roles/check-code',
    method: 'get',
    params: {
      roleCode,
      excludeId
    }
  })
}
