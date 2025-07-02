import request from '@/utils/request'

/**
 * 医生排班管理相关API
 */

// 获取医生排班列表
export function getDoctorScheduleList(data) {
  return request({
    url: '/api/doctor/schedule/list',
    method: 'post',
    data
  })
}

// 获取医生指定时间段的排班
export function getDoctorScheduleByDate(doctorId, startDate, endDate) {
  return request({
    url: `/api/doctor/schedule/${doctorId}`,
    method: 'get',
    params: {
      startDate,
      endDate
    }
  })
}

// 新增医生排班
export function addDoctorSchedule(data) {
  return request({
    url: '/api/doctor/schedule/add',
    method: 'post',
    data
  })
}

// 更新医生排班
export function updateDoctorSchedule(data) {
  return request({
    url: '/api/doctor/schedule/update',
    method: 'put',
    data
  })
}

// 删除医生排班
export function deleteDoctorSchedule(id) {
  return request({
    url: `/api/doctor/schedule/${id}`,
    method: 'delete'
  })
}

// 批量新增医生排班
export function batchAddDoctorSchedule(data) {
  return request({
    url: '/api/doctor/schedule/batch',
    method: 'post',
    data
  })
}

// 获取排班模板
export function getScheduleTemplates() {
  return request({
    url: '/api/doctor/schedule/templates',
    method: 'get'
  })
}

// 复制排班
export function copyDoctorSchedule(data) {
  return request({
    url: '/api/doctor/schedule/copy',
    method: 'post',
    data
  })
}
