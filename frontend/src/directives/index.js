/**
 * 自定义指令集合
 */

import lazy from './lazy'

export default {
  install(app) {
    // 注册图片懒加载指令
    app.directive('lazy', lazy)
  }
}

// 单独导出，方便按需引入
export { lazy }
