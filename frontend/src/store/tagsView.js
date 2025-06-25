import { defineStore } from 'pinia'

export const useTagsViewStore = defineStore('tagsView', {
  state: () => ({
    visitedViews: [],
    cachedViews: []
  }),

  getters: {
    // 获取访问过的视图
    getVisitedViews: (state) => state.visitedViews,
    // 获取缓存的视图
    getCachedViews: (state) => state.cachedViews
  },

  actions: {
    /**
     * 添加访问的视图
     */
    addVisitedView(view) {
      if (this.visitedViews.some(v => v.path === view.path)) return
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta?.title || 'no-name'
        })
      )
    },

    /**
     * 添加缓存的视图
     */
    addCachedView(view) {
      if (this.cachedViews.includes(view.name)) return
      if (!view.meta?.noCache) {
        this.cachedViews.push(view.name)
      }
    },

    /**
     * 删除访问的视图
     */
    delVisitedView(view) {
      return new Promise(resolve => {
        for (const [i, v] of this.visitedViews.entries()) {
          if (v.path === view.path) {
            this.visitedViews.splice(i, 1)
            break
          }
        }
        resolve([...this.visitedViews])
      })
    },

    /**
     * 删除缓存的视图
     */
    delCachedView(view) {
      return new Promise(resolve => {
        const index = this.cachedViews.indexOf(view.name)
        index > -1 && this.cachedViews.splice(index, 1)
        resolve([...this.cachedViews])
      })
    },

    /**
     * 删除其他访问的视图
     */
    delOthersVisitedViews(view) {
      return new Promise(resolve => {
        this.visitedViews = this.visitedViews.filter(v => {
          return v.meta?.affix || v.path === view.path
        })
        resolve([...this.visitedViews])
      })
    },

    /**
     * 删除其他缓存的视图
     */
    delOthersCachedViews(view) {
      return new Promise(resolve => {
        const index = this.cachedViews.indexOf(view.name)
        if (index > -1) {
          this.cachedViews = this.cachedViews.slice(index, index + 1)
        } else {
          this.cachedViews = []
        }
        resolve([...this.cachedViews])
      })
    },

    /**
     * 删除所有访问的视图
     */
    delAllVisitedViews() {
      return new Promise(resolve => {
        const affixTags = this.visitedViews.filter(tag => tag.meta?.affix)
        this.visitedViews = affixTags
        resolve([...this.visitedViews])
      })
    },

    /**
     * 删除所有缓存的视图
     */
    delAllCachedViews() {
      return new Promise(resolve => {
        this.cachedViews = []
        resolve([...this.cachedViews])
      })
    },

    /**
     * 更新访问的视图
     */
    updateVisitedView(view) {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          v = Object.assign(v, view)
          break
        }
      }
    },

    /**
     * 添加视图（同时添加到访问和缓存列表）
     */
    addView(view) {
      this.addVisitedView(view)
      this.addCachedView(view)
    },

    /**
     * 删除视图（同时从访问和缓存列表删除）
     */
    delView(view) {
      return new Promise(resolve => {
        this.delVisitedView(view)
        this.delCachedView(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    /**
     * 删除其他视图
     */
    delOthersViews(view) {
      return new Promise(resolve => {
        this.delOthersVisitedViews(view)
        this.delOthersCachedViews(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    /**
     * 删除所有视图
     */
    delAllViews() {
      return new Promise(resolve => {
        this.delAllVisitedViews()
        this.delAllCachedViews()
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    }
  }
})
