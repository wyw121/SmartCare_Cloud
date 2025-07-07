import request from '@/utils/request';
import { getMockLevelStatistics, getMockWarningPage } from './mockHealthWarning';

/**
 * ??????API
 */

// ??????
const isDev = false; // ????????Mock?????????

/**
 * ????????
 */
export function getWarningStatistics() {
  if (isDev) {
    return Promise.resolve({ 
      data: {
        code: 200,
        message: '????',
        data: { urgent: 15, high: 32, medium: 48, low: 25 }
      }
    })
  }
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

/**
 * ??????
 * @param {Object} params - ????
 */
export function getWarningList(params) {
  return request({
    url: '/health-warning/page',
    method: 'post',
    data: params
  })
}

/**
 * ??????
 * @param {number} id - ??ID
 */
export function getWarningDetail(id) {
  return request({
    url: `/health-warning/${id}`,
    method: 'get'
  })
}

/**
 * ??????
 * @param {number} id - ??ID
 * @param {string} status - ???
 */
export function updateWarningStatus(id, status) {
  return request({
    url: `/health-warning/${id}/status`,
    method: 'put',
    data: { status }
  })
}

/**
 * ??????
 * @param {Array} ids - ??ID??
 * @param {string} action - ????
 */
export function batchHandleWarnings(ids, action) {
  return request({
    url: '/health-warning/batch',
    method: 'post',
    data: { ids, action }
  })
}

/**
 * ??????????
 * @param {Object} data - ????
 */
export function getHealthWarningPageList(data) {
  if (isDev) {
    // ??????Mock??
    return Promise.resolve({ 
      data: getMockWarningPage(data)
    })
  }
  return request({
    url: '/health-warning/page',
    method: 'post',
    data
  })
}

// ??????
export function addHealthWarning(data) {
  return request({
    url: '/health-warning/add',
    method: 'post',
    data
  })
}

// ??????
export function updateHealthWarning(data) {
  return request({
    url: '/health-warning/update',
    method: 'put',
    data
  })
}

// ??ID????????
export function getHealthWarningById(id) {
  return request({
    url: `/health-warning/${id}`,
    method: 'get'
  })
}

// ??????
export function deleteHealthWarning(id) {
  return request({
    url: `/health-warning/${id}`,
    method: 'delete'
  })
}

// ????????
export function deleteHealthWarningBatch(ids) {
  return request({
    url: '/health-warning/batch',
    method: 'delete',
    data: ids
  })
}

// ??????
export function handleHealthWarning(id, data) {
  return request({
    url: `/health-warning/${id}/handle`,
    method: 'put',
    data
  })
}

// ????????
export function batchHandleHealthWarning(ids, data) {
  return request({
    url: '/health-warning/batch-handle',
    method: 'put',
    data: { ids, ...data }
  })
}

// ????????
export function getWarningTypeStatistics() {
  return request({
    url: '/health-warning/statistics/type',
    method: 'get'
  })
}

// ????????
export function getWarningLevelStatistics() {
  if (isDev) {
    // ??????Mock??
    return Promise.resolve({ 
      data: getMockLevelStatistics()
    })
  }
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

// ?????????
export function getUnhandledWarningCount() {
  return request({
    url: '/health-warning/count/unhandled',
    method: 'get'
  })
}

// ????????
export function getWarningTrendStatistics(params) {
  return request({
    url: '/health-warning/statistics/trend',
    method: 'get',
    params
  })
}

// ?????????????
export function getWarningStatisticsByLevel() {
  return request({
    url: '/health-warning/statistics/level',
    method: 'get'
  })
}

// ?????????????
export function getWarningStatisticsByType() {
  return request({
    url: '/health-warning/statistics/type',
    method: 'get'
  })
}

// ??????
export function exportWarningData(params) {
  return request({
    url: '/health-warning/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}

// ??????
export function getWarningConfig() {
  return request({
    url: '/health-warning/config',
    method: 'get'
  })
}

// ??????
export function updateWarningConfig(data) {
  return request({
    url: '/health-warning/config',
    method: 'put',
    data
  })
}
