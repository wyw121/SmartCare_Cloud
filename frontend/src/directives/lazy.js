/**
 * 图片懒加载指令
 * 使用方法: <img v-lazy="imageSrc" />
 */

const defaultOptions = {
  loading: '/static/images/loading.gif', // 加载中的占位图
  error: '/static/images/error.png',     // 加载失败的占位图
  observerOptions: {
    rootMargin: '50px',  // 提前50px开始加载
    threshold: 0.01
  }
}

export default {
  mounted(el, binding) {
    const { value } = binding
    const { loading, error, observerOptions } = defaultOptions

    // 设置初始占位图
    el.src = loading

    // 创建 IntersectionObserver
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target
          const src = value || binding.value

          // 创建新的Image对象预加载
          const image = new Image()
          
          image.onload = () => {
            img.src = src
            img.classList.add('loaded')
          }
          
          image.onerror = () => {
            img.src = error
            img.classList.add('error')
          }
          
          image.src = src

          // 停止观察
          observer.unobserve(img)
        }
      })
    }, observerOptions)

    // 开始观察
    observer.observe(el)

    // 保存observer以便后续清理
    el._lazyLoadObserver = observer
  },

  unmounted(el) {
    // 清理observer
    if (el._lazyLoadObserver) {
      el._lazyLoadObserver.disconnect()
      delete el._lazyLoadObserver
    }
  },

  updated(el, binding) {
    // 如果图片src更新，重新设置
    if (binding.value !== binding.oldValue) {
      el.src = defaultOptions.loading
      
      if (el._lazyLoadObserver) {
        el._lazyLoadObserver.unobserve(el)
        el._lazyLoadObserver.observe(el)
      }
    }
  }
}
