import request from '@/utils/request'

/**
 * 医生管理相关API
 */

// 分页查询医生列表
export function getDoctorPageList(data) {
  return request({
    url: '/api/doctor/page',
    method: 'post',
    data
  })
}

// 新增医生
export function addDoctor(data) {
  return request({
    url: '/api/doctor/add',
    method: 'post',
    data
  })
}

// 更新医生信息
export function updateDoctor(data) {
  return request({
    url: '/api/doctor/update',
    method: 'put',
    data
  })
}

// 根据ID查询医生详情
export function getDoctorById(id) {
  return request({
    url: `/api/doctor/${id}`,
    method: 'get'
  })
}

// 删除医生
export function deleteDoctor(id) {
  return request({
    url: `/api/doctor/${id}`,
    method: 'delete'
  })
}

// 批量删除医生
export function deleteDoctorBatch(ids) {
  return request({
    url: '/api/doctor/batch',
    method: 'delete',
    data: ids
  })
}

// 根据科室查询医生列表
export function getDoctorByDepartment(department) {
  return request({
    url: `/api/doctor/department/${department}`,
    method: 'get'
  })
}

// 根据医院ID查询医生列表
export function getDoctorByHospitalId(hospitalId) {
  return request({
    url: `/api/doctor/hospital/${hospitalId}`,
    method: 'get'
  })
}

// 统计各科室医生数量
export function getDoctorDepartmentStatistics() {
  return request({
    url: '/api/doctor/statistics/department',
    method: 'get'
  })
}

// 更新医生状态
export function updateDoctorStatus(id, status) {
  return request({
    url: `/api/doctor/${id}/status/${status}`,
    method: 'put'
  })
}

// 获取医生个人统计数据
export function getDoctorStatistics(doctorId) {
  return request({
    url: `/api/doctor/${doctorId}/statistics`,
    method: 'get'
  })
}
