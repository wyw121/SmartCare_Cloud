/**
 * 报表统计 Mock 数据
 * 为开发阶段提供测试数据
 */

// 生成随机数据
const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1)) + min
const randomFloat = (min, max) => Math.random() * (max - min) + min

// 生成时间序列数据
const generateTimeSeriesData = (days = 30) => {
  const data = []
  const now = new Date()
  
  for (let i = days - 1; i >= 0; i--) {
    const date = new Date(now.getTime() - i * 24 * 60 * 60 * 1000)
    data.push({
      date: date.toISOString().split('T')[0],
      value: randomInt(10, 100)
    })
  }
  
  return data
}

// 数据概览统计
export const mockOverviewStatistics = {
  code: 200,
  message: '获取成功',
  data: {
    totalElderly: randomInt(800, 1200),
    totalDoctors: randomInt(50, 100),
    totalEquipment: randomInt(200, 500),
    totalWarnings: randomInt(100, 300),
    healthyRate: randomFloat(85, 95).toFixed(1),
    treatmentRate: randomFloat(70, 85).toFixed(1),
    satisfactionRate: randomFloat(90, 98).toFixed(1)
  }
}

// 健康状况统计
export const mockHealthStatusStatistics = {
  code: 200,
  message: '获取成功',
  data: {
    healthy: randomInt(600, 800),
    subHealth: randomInt(200, 300),
    chronic: randomInt(100, 200),
    serious: randomInt(10, 50),
    distribution: [
      { name: '健康', value: randomInt(600, 800), color: '#52c41a' },
      { name: '亚健康', value: randomInt(200, 300), color: '#faad14' },
      { name: '慢性病', value: randomInt(100, 200), color: '#fa8c16' },
      { name: '重病', value: randomInt(10, 50), color: '#f5222d' }
    ]
  }
}

// 预警统计分析
export const mockWarningAnalysis = {
  code: 200,
  message: '获取成功',
  data: {
    totalWarnings: randomInt(200, 400),
    urgentWarnings: randomInt(20, 50),
    handledWarnings: randomInt(150, 300),
    pendingWarnings: randomInt(30, 80),
    warningTypes: [
      { type: '血压异常', count: randomInt(50, 100), trend: 'up' },
      { type: '血糖异常', count: randomInt(30, 80), trend: 'down' },
      { type: '心率异常', count: randomInt(20, 60), trend: 'stable' },
      { type: '体温异常', count: randomInt(10, 40), trend: 'up' }
    ],
    monthlyTrend: generateTimeSeriesData(30)
  }
}

// 医疗服务统计
export const mockMedicalServiceStatistics = {
  code: 200,
  message: '获取成功',
  data: {
    totalServices: randomInt(500, 1000),
    completedServices: randomInt(400, 800),
    ongoingServices: randomInt(50, 150),
    scheduledServices: randomInt(20, 100),
    serviceTypes: [
      { type: '健康检查', count: randomInt(100, 200), rate: randomFloat(85, 95).toFixed(1) },
      { type: '药物管理', count: randomInt(80, 150), rate: randomFloat(90, 98).toFixed(1) },
      { type: '康复治疗', count: randomInt(60, 120), rate: randomFloat(80, 90).toFixed(1) },
      { type: '心理咨询', count: randomInt(30, 80), rate: randomFloat(75, 85).toFixed(1) }
    ],
    monthlyVolume: generateTimeSeriesData(30)
  }
}

// 照护等级统计
export const mockCareLevelStatistics = {
  code: 200,
  message: '获取成功',
  data: {
    levelDistribution: [
      { level: '一级照护', count: randomInt(200, 300), percentage: randomFloat(25, 35).toFixed(1) },
      { level: '二级照护', count: randomInt(150, 250), percentage: randomFloat(20, 30).toFixed(1) },
      { level: '三级照护', count: randomInt(100, 200), percentage: randomFloat(15, 25).toFixed(1) },
      { level: '特级照护', count: randomInt(50, 100), percentage: randomFloat(10, 20).toFixed(1) }
    ],
    careQuality: {
      excellent: randomInt(300, 500),
      good: randomInt(200, 400),
      average: randomInt(100, 200),
      poor: randomInt(10, 50)
    }
  }
}

// 设备使用统计
export const mockEquipmentUsageStatistics = {
  code: 200,
  message: '获取成功',
  data: {
    totalEquipment: randomInt(200, 500),
    activeEquipment: randomInt(180, 450),
    maintenanceEquipment: randomInt(10, 30),
    faultEquipment: randomInt(5, 20),
    equipmentTypes: [
      { type: '血压计', count: randomInt(50, 100), usage: randomFloat(85, 95).toFixed(1) },
      { type: '血糖仪', count: randomInt(40, 80), usage: randomFloat(80, 90).toFixed(1) },
      { type: '心率监测器', count: randomInt(30, 60), usage: randomFloat(75, 85).toFixed(1) },
      { type: '体温计', count: randomInt(60, 120), usage: randomFloat(90, 98).toFixed(1) }
    ],
    usageHistory: generateTimeSeriesData(30)
  }
}

// 趋势分析数据
export const mockTrendAnalysis = {
  code: 200,
  message: '获取成功',
  data: {
    elderlyTrend: generateTimeSeriesData(30),
    healthTrend: generateTimeSeriesData(30),
    serviceTrend: generateTimeSeriesData(30),
    satisfactionTrend: generateTimeSeriesData(30),
    predictions: {
      nextMonth: {
        elderlyGrowth: randomFloat(5, 15).toFixed(1),
        serviceIncrease: randomFloat(10, 25).toFixed(1),
        healthImprovement: randomFloat(3, 8).toFixed(1)
      }
    }
  }
}

// 地区分布数据
export const mockRegionDistribution = {
  code: 200,
  message: '获取成功',
  data: {
    regions: [
      { name: '朝阳区', count: randomInt(200, 400), percentage: randomFloat(20, 30).toFixed(1) },
      { name: '海淀区', count: randomInt(150, 350), percentage: randomFloat(15, 25).toFixed(1) },
      { name: '西城区', count: randomInt(100, 300), percentage: randomFloat(10, 20).toFixed(1) },
      { name: '东城区', count: randomInt(80, 250), percentage: randomFloat(8, 18).toFixed(1) },
      { name: '丰台区', count: randomInt(60, 200), percentage: randomFloat(6, 15).toFixed(1) }
    ]
  }
}

// 服务质量评分
export const mockServiceQuality = {
  code: 200,
  message: '获取成功',
  data: {
    overallScore: randomFloat(4.0, 5.0).toFixed(1),
    categories: [
      { name: '服务态度', score: randomFloat(4.2, 5.0).toFixed(1) },
      { name: '专业水平', score: randomFloat(4.0, 4.8).toFixed(1) },
      { name: '响应速度', score: randomFloat(3.8, 4.6).toFixed(1) },
      { name: '设备质量', score: randomFloat(4.1, 4.9).toFixed(1) }
    ],
    feedback: {
      excellent: randomInt(400, 600),
      good: randomInt(200, 400),
      average: randomInt(50, 150),
      poor: randomInt(10, 50)
    }
  }
}

// AI洞察报告
export const mockAIInsights = {
  code: 200,
  message: '获取成功',
  data: {
    insights: [
      {
        id: 1,
        title: '健康预警趋势分析',
        content: '根据近期数据分析，血压异常预警呈上升趋势，建议加强血压监测频次。',
        priority: 'high',
        category: '健康预警',
        createdAt: new Date().toISOString()
      },
      {
        id: 2,
        title: '服务质量改进建议',
        content: '用户满意度调查显示，响应速度有待提升，建议优化服务流程。',
        priority: 'medium',
        category: '服务质量',
        createdAt: new Date().toISOString()
      }
    ],
    summary: {
      totalInsights: randomInt(10, 20),
      highPriority: randomInt(2, 5),
      mediumPriority: randomInt(5, 10),
      lowPriority: randomInt(3, 8)
    }
  }
}

// 统计数据
export const mockStatisticsData = {
  code: 200,
  message: '获取成功',
  data: {
    overview: mockOverviewStatistics.data,
    healthStatus: mockHealthStatusStatistics.data,
    warnings: mockWarningAnalysis.data,
    services: mockMedicalServiceStatistics.data,
    careLevel: mockCareLevelStatistics.data,
    equipment: mockEquipmentUsageStatistics.data,
    trend: mockTrendAnalysis.data
  }
}
