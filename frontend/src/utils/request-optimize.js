/**
 * 防抖函数
 * @param {Function} func 需要防抖的函数
 * @param {number} wait 等待时间（毫秒）
 * @param {boolean} immediate 是否立即执行
 * @returns {Function} 防抖后的函数
 */
export function debounce(func, wait = 300, immediate = false) {
  let timeout

  return function executedFunction(...args) {
    const context = this

    const later = () => {
      timeout = null
      if (!immediate) func.apply(context, args)
    }

    const callNow = immediate && !timeout

    clearTimeout(timeout)
    timeout = setTimeout(later, wait)

    if (callNow) func.apply(context, args)
  }
}

/**
 * 节流函数
 * @param {Function} func 需要节流的函数
 * @param {number} limit 时间间隔（毫秒）
 * @returns {Function} 节流后的函数
 */
export function throttle(func, limit = 300) {
  let inThrottle

  return function executedFunction(...args) {
    const context = this

    if (!inThrottle) {
      func.apply(context, args)
      inThrottle = true

      setTimeout(() => {
        inThrottle = false
      }, limit)
    }
  }
}

/**
 * 创建可取消的请求
 * 使用示例:
 * const { request, cancel } = createCancelableRequest()
 * request(api.getList).then(data => {}).catch(err => {})
 * cancel() // 取消请求
 */
export function createCancelableRequest() {
  let controller = new AbortController()

  return {
    request: async (requestFn, ...args) => {
      try {
        // 传入signal给axios
        const config = args[args.length - 1] || {}
        config.signal = controller.signal
        return await requestFn(...args)
      } catch (error) {
        if (error.name === 'CanceledError' || error.name === 'AbortError') {
          console.log('请求已取消')
          throw error
        }
        throw error
      }
    },
    cancel: (message = '请求已取消') => {
      controller.abort(message)
      // 创建新的controller供下次使用
      controller = new AbortController()
    }
  }
}

/**
 * 并行请求优化 - 批量请求
 * @param {Array} requests 请求函数数组
 * @returns {Promise} 所有请求结果
 */
export async function batchRequests(requests) {
  try {
    const results = await Promise.all(requests.map(req => req()))
    return results
  } catch (error) {
    console.error('批量请求失败:', error)
    throw error
  }
}

/**
 * 并行请求优化 - 限制并发数
 * @param {Array} requests 请求函数数组
 * @param {number} limit 并发限制数
 * @returns {Promise} 所有请求结果
 */
export async function batchRequestsWithLimit(requests, limit = 5) {
  const results = []
  const executing = []

  for (const request of requests) {
    const p = Promise.resolve().then(() => request())
    results.push(p)

    if (limit <= requests.length) {
      const e = p.then(() => executing.splice(executing.indexOf(e), 1))
      executing.push(e)

      if (executing.length >= limit) {
        await Promise.race(executing)
      }
    }
  }

  return Promise.all(results)
}

/**
 * 请求缓存装饰器
 * @param {Function} requestFn 请求函数
 * @param {number} cacheTime 缓存时间（毫秒），默认5分钟
 * @returns {Function} 带缓存的请求函数
 */
export function withCache(requestFn, cacheTime = 5 * 60 * 1000) {
  const cache = new Map()

  return async function(...args) {
    const key = JSON.stringify(args)
    const cached = cache.get(key)

    if (cached && Date.now() - cached.time < cacheTime) {
      console.log('使用缓存数据')
      return cached.data
    }

    const data = await requestFn(...args)
    cache.set(key, { data, time: Date.now() })

    return data
  }
}

/**
 * 自动重试装饰器
 * @param {Function} requestFn 请求函数
 * @param {number} maxRetries 最大重试次数
 * @param {number} retryDelay 重试延迟（毫秒）
 * @returns {Function} 带重试的请求函数
 */
export function withRetry(requestFn, maxRetries = 3, retryDelay = 1000) {
  return async function(...args) {
    let lastError

    for (let i = 0; i <= maxRetries; i++) {
      try {
        return await requestFn(...args)
      } catch (error) {
        lastError = error
        
        if (i < maxRetries) {
          console.log(`请求失败，${retryDelay}ms后进行第${i + 1}次重试...`)
          await new Promise(resolve => setTimeout(resolve, retryDelay))
        }
      }
    }

    throw lastError
  }
}
