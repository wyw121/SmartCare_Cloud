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
      console.log('📝 [TagsView Store] addVisitedView 调用', {
        viewPath: view.path,
        viewName: view.name,
        viewTitle: view.meta?.title,
        isAffix: view.meta?.affix,
        currentViewsCount: this.visitedViews.length
      })
      
      if (this.visitedViews.some(v => v.path === view.path)) {
        console.log('⏭️ [TagsView Store] 视图已存在，跳过添加')
        return
      }
      
      const newView = Object.assign({}, view, {
        title: view.meta?.title || 'no-name'
      })
      
      // 如果是固定标签，插入到固定标签区域的末尾
      if (view.meta?.affix) {
        console.log('📌 [TagsView Store] 添加固定标签')
        // 找到最后一个固定标签的位置
        let lastAffixIndex = -1
        for (let i = 0; i < this.visitedViews.length; i++) {
          if (this.visitedViews[i].meta?.affix) {
            lastAffixIndex = i
          }
        }
        // 插入到最后一个固定标签之后
        console.log('📌 [TagsView Store] 插入位置', { lastAffixIndex })
        this.visitedViews.splice(lastAffixIndex + 1, 0, newView)
      } else {
        console.log('📄 [TagsView Store] 添加普通标签到末尾')
        // 非固定标签直接添加到末尾
        this.visitedViews.push(newView)
      }
      
      console.log('✅ [TagsView Store] 视图添加完成', {
        newViewsCount: this.visitedViews.length,
        allViews: this.visitedViews.map(v => ({ path: v.path, name: v.name, title: v.title, affix: v.meta?.affix }))
      })
    },

    /**
     * 添加缓存的视图
     */
    addCachedView(view) {
      console.log('💾 [TagsView Store] addCachedView 调用', {
        viewName: view.name,
        noCache: view.meta?.noCache,
        currentCachedCount: this.cachedViews.length
      })
      
      if (this.cachedViews.includes(view.name)) {
        console.log('⏭️ [TagsView Store] 缓存视图已存在，跳过添加')
        return
      }
      
      if (!view.meta?.noCache) {
        this.cachedViews.push(view.name)
        console.log('✅ [TagsView Store] 缓存视图添加完成', {
          newCachedCount: this.cachedViews.length,
          allCached: this.cachedViews
        })
      } else {
        console.log('🚫 [TagsView Store] 视图标记为不缓存，跳过')
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
        // 保留固定标签和当前视图，并确保固定标签在前面
        const affixViews = this.visitedViews.filter(v => v.meta?.affix)
        const currentView = this.visitedViews.find(v => v.path === view.path)
        
        this.visitedViews = [...affixViews]
        
        // 如果当前视图不是固定标签，添加到末尾
        if (currentView && !currentView.meta?.affix) {
          this.visitedViews.push(currentView)
        }
        
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
      console.log('🔄 [TagsView Store] addView 调用', {
        viewPath: view.path,
        viewName: view.name,
        viewTitle: view.meta?.title
      })
      
      this.addVisitedView(view)
      this.addCachedView(view)
      
      console.log('🔄 [TagsView Store] addView 完成')
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
