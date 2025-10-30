/**
 * 前端权限指令
 * 用于页面元素级权限控制
 * 
 * 使用方法:
 * - v-permission="'elderly:edit'" - 单个权限
 * - v-permission="['elderly:edit', 'elderly:delete']" - 多个权限(需要全部拥有)
 * - v-permission-any="['elderly:edit', 'elderly:delete']" - 多个权限(拥有任一即可)
 */

import { hasPermission } from '@/router/role-config'
import { useUserStore } from '@/store/user'

/**
 * v-permission 指令
 * 检查用户是否拥有指定权限,无权限则隐藏元素
 */
export const permission = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const userRole = userStore.userRole

    if (value) {
      let hasAuth = false

      if (typeof value === 'string') {
        // 单个权限
        hasAuth = hasPermission(userRole, value)
      } else if (Array.isArray(value)) {
        // 多个权限,需要全部拥有
        hasAuth = value.every(permission => hasPermission(userRole, permission))
      }

      if (!hasAuth) {
        // 无权限,移除DOM元素
        el.parentNode?.removeChild(el)
      }
    }
  }
}

/**
 * v-permission-any 指令
 * 检查用户是否拥有任一指定权限,无权限则隐藏元素
 */
export const permissionAny = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const userRole = userStore.userRole

    if (value && Array.isArray(value)) {
      const hasAuth = value.some(permission => hasPermission(userRole, permission))

      if (!hasAuth) {
        // 无权限,移除DOM元素
        el.parentNode?.removeChild(el)
      }
    }
  }
}

/**
 * v-role 指令
 * 检查用户角色,不匹配则隐藏元素
 */
export const role = {
  mounted(el, binding) {
    const { value } = binding
    const userStore = useUserStore()
    const userRole = userStore.userRole

    if (value) {
      let hasRole = false

      if (typeof value === 'string') {
        hasRole = userRole === value
      } else if (Array.isArray(value)) {
        hasRole = value.includes(userRole)
      }

      if (!hasRole) {
        el.parentNode?.removeChild(el)
      }
    }
  }
}

// 导出所有指令
export default {
  permission,
  permissionAny,
  role
}
