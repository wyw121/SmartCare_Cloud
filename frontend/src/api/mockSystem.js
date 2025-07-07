/**
 * 系统用户管理 Mock 数据
 * 为开发阶段提供测试数据
 */

// 生成随机ID
const generateId = () => Math.floor(Math.random() * 10000) + 1

// 生成随机时间
const generateTime = (daysBack = 30) => {
  const now = new Date()
  const randomTime = new Date(now.getTime() - Math.random() * daysBack * 24 * 60 * 60 * 1000)
  return randomTime.toISOString().slice(0, 19).replace('T', ' ')
}

// 用户姓名列表
const userNames = [
  '张医生', '李护士', '王主任', '赵医生', '钱护士', '孙主管', '周医生', '吴护士',
  '陈主任', '刘医生', '黄护士', '杨主管', '朱医生', '许护士', '何主任', '郭医生',
  '谢护士', '邓主管', '韩医生', '曹护士', '范主任', '彭医生', '方护士', '夏主管'
]

// 部门列表
const departments = [
  { id: 1, name: '管理部' },
  { id: 2, name: '医疗部' },
  { id: 3, name: '护理部' },
  { id: 4, name: '运营部' },
  { id: 5, name: '技术部' },
  { id: 6, name: '财务部' },
  { id: 7, name: '人事部' },
  { id: 8, name: '采购部' }
]

// 角色列表
const roles = [
  { id: 1, name: '超级管理员', permissions: ['*'] },
  { id: 2, name: '系统管理员', permissions: ['system:*'] },
  { id: 3, name: '医生', permissions: ['medical:*', 'elderly:read'] },
  { id: 4, name: '护士', permissions: ['nursing:*', 'elderly:read'] },
  { id: 5, name: '运营人员', permissions: ['operation:*'] },
  { id: 6, name: '财务人员', permissions: ['finance:*'] },
  { id: 7, name: '普通用户', permissions: ['basic:*'] }
]

// 生成单个用户数据
const generateUser = (index = 1) => {
  const name = userNames[index % userNames.length]
  const username = `user${String(index).padStart(3, '0')}`
  const email = `${username}@smartcare.com`
  const phone = `138${String(Math.floor(Math.random() * 100000000)).padStart(8, '0')}`
  const role = roles[Math.floor(Math.random() * roles.length)]
  const department = departments[Math.floor(Math.random() * departments.length)]
  const status = Math.random() > 0.1 ? 1 : 0 // 90% 概率启用
  
  return {
    id: index,
    username,
    realName: name,
    email,
    phone,
    roleId: role.id,
    roleName: role.name,
    departmentId: department.id,
    departmentName: department.name,
    status,
    createTime: generateTime(365),
    lastLoginTime: status ? generateTime(7) : null,
    remark: `${name}的备注信息`,
    avatar: null
  }
}

// 生成用户列表
const generateUserList = (count = 50) => {
  const users = []
  
  // 添加默认管理员
  users.push({
    id: 1,
    username: 'admin',
    realName: '系统管理员',
    email: 'admin@smartcare.com',
    phone: '13800138001',
    roleId: 1,
    roleName: '超级管理员',
    departmentId: 1,
    departmentName: '管理部',
    status: 1,
    createTime: '2024-01-01 00:00:00',
    lastLoginTime: generateTime(1),
    remark: '系统默认管理员账号',
    avatar: null
  })
  
  // 生成其他用户
  for (let i = 2; i <= count; i++) {
    users.push(generateUser(i))
  }
  
  return users
}

// 导出Mock数据
export const mockUsers = generateUserList(50)
export const mockRoles = roles
export const mockDepartments = departments

// 分页查询用户
export const getMockUserPage = (params) => {
  let filteredUsers = [...mockUsers]
  
  // 关键词搜索
  if (params.keyword) {
    const keyword = params.keyword.toLowerCase()
    filteredUsers = filteredUsers.filter(user => 
      user.username.toLowerCase().includes(keyword) ||
      user.realName.toLowerCase().includes(keyword) ||
      user.email.toLowerCase().includes(keyword) ||
      user.phone.includes(keyword)
    )
  }
  
  // 状态筛选
  if (params.status !== undefined && params.status !== '') {
    filteredUsers = filteredUsers.filter(user => user.status === Number(params.status))
  }
  
  // 角色筛选
  if (params.roleId) {
    filteredUsers = filteredUsers.filter(user => user.roleId === Number(params.roleId))
  }
  
  // 部门筛选
  if (params.departmentId) {
    filteredUsers = filteredUsers.filter(user => user.departmentId === Number(params.departmentId))
  }
  
  // 排序
  filteredUsers.sort((a, b) => b.id - a.id)
  
  // 分页
  const current = params.current || 1
  const size = params.size || 10
  const total = filteredUsers.length
  const start = (current - 1) * size
  const end = start + size
  const records = filteredUsers.slice(start, end)
  
  return {
    code: 200,
    message: '查询成功',
    data: {
      records,
      total,
      current,
      size,
      pages: Math.ceil(total / size)
    }
  }
}

// 创建用户
export const createMockUser = (userData) => {
  const newUser = {
    id: Math.max(...mockUsers.map(u => u.id)) + 1,
    ...userData,
    createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    lastLoginTime: null
  }
  
  // 查找角色名称
  const role = mockRoles.find(r => r.id === userData.roleId)
  if (role) {
    newUser.roleName = role.name
  }
  
  // 查找部门名称
  const department = mockDepartments.find(d => d.id === userData.departmentId)
  if (department) {
    newUser.departmentName = department.name
  }
  
  mockUsers.unshift(newUser)
  
  return {
    code: 200,
    message: '创建成功',
    data: newUser
  }
}

// 更新用户
export const updateMockUser = (id, userData) => {
  const userIndex = mockUsers.findIndex(u => u.id === id)
  if (userIndex === -1) {
    return {
      code: 404,
      message: '用户不存在'
    }
  }
  
  // 查找角色名称
  const role = mockRoles.find(r => r.id === userData.roleId)
  if (role) {
    userData.roleName = role.name
  }
  
  // 查找部门名称
  const department = mockDepartments.find(d => d.id === userData.departmentId)
  if (department) {
    userData.departmentName = department.name
  }
  
  Object.assign(mockUsers[userIndex], userData)
  
  return {
    code: 200,
    message: '更新成功',
    data: mockUsers[userIndex]
  }
}

// 删除用户
export const deleteMockUser = (id) => {
  const userIndex = mockUsers.findIndex(u => u.id === id)
  if (userIndex === -1) {
    return {
      code: 404,
      message: '用户不存在'
    }
  }
  
  mockUsers.splice(userIndex, 1)
  
  return {
    code: 200,
    message: '删除成功'
  }
}

// 批量删除用户
export const batchDeleteMockUsers = (ids) => {
  ids.forEach(id => {
    const userIndex = mockUsers.findIndex(u => u.id === id)
    if (userIndex !== -1) {
      mockUsers.splice(userIndex, 1)
    }
  })
  
  return {
    code: 200,
    message: '批量删除成功'
  }
}

// 切换用户状态
export const toggleMockUserStatus = (id, status) => {
  const user = mockUsers.find(u => u.id === id)
  if (!user) {
    return {
      code: 404,
      message: '用户不存在'
    }
  }
  
  user.status = status
  
  return {
    code: 200,
    message: '状态更新成功',
    data: user
  }
}

// 检查用户名唯一性
export const checkMockUsername = (username, excludeId = null) => {
  const exists = mockUsers.some(user => 
    user.username === username && user.id !== excludeId
  )
  
  return {
    code: 200,
    message: '检查完成',
    data: {
      available: !exists
    }
  }
}

// 检查邮箱唯一性
export const checkMockEmail = (email, excludeId = null) => {
  const exists = mockUsers.some(user => 
    user.email === email && user.id !== excludeId
  )
  
  return {
    code: 200,
    message: '检查完成',
    data: {
      available: !exists
    }
  }
}
