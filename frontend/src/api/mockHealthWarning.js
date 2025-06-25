/**
 * 健康预警 Mock 数据
 * 为开发阶段提供测试数据
 */

// 生成随机ID
const generateId = () => Math.floor(Math.random() * 10000) + 1

// 生成随机时间
const generateTime = (daysBack = 7) => {
  const now = new Date()
  const randomTime = new Date(now.getTime() - Math.random() * daysBack * 24 * 60 * 60 * 1000)
  return randomTime.toISOString().slice(0, 19).replace('T', ' ')
}

// 老人姓名列表
const elderlyNames = [
  '张三', '李四', '王五', '赵六', '钱七', '孙八', '周九', '吴十',
  '陈明', '刘华', '黄丽', '杨敏', '朱强', '许静', '何勇', '郭雷',
  '谢飞', '邓霞', '韩磊', '曹颖', '范超', '彭亮', '方芳', '夏雪'
]

// 预警类型列表
const warningTypes = [
  '血压异常', '血糖异常', '心率异常', '体温异常', 
  '长期不活动', '药物未按时服用', '跌倒风险', '其他异常'
]

// 数据来源列表
const dataSources = [
  '血压监测设备', '血糖仪', '心率监测器', '体温计',
  '活动监测器', '药物提醒器', '跌倒检测器', '手动录入'
]

// 生成单条预警数据
const generateWarningData = () => {
  const warningLevel = Math.floor(Math.random() * 4) + 1
  const status = Math.floor(Math.random() * 5)
  const warningType = warningTypes[Math.floor(Math.random() * warningTypes.length)]
  const elderlyName = elderlyNames[Math.floor(Math.random() * elderlyNames.length)]
  const dataSource = dataSources[Math.floor(Math.random() * dataSources.length)]
  
  const titles = {
    '血压异常': `${elderlyName}血压监测异常`,
    '血糖异常': `${elderlyName}血糖水平异常`,
    '心率异常': `${elderlyName}心率不规律`,
    '体温异常': `${elderlyName}体温异常`,
    '长期不活动': `${elderlyName}长时间无活动迹象`,
    '药物未按时服用': `${elderlyName}未按时服药提醒`,
    '跌倒风险': `${elderlyName}跌倒风险预警`,
    '其他异常': `${elderlyName}健康状况异常`
  }
  
  const triggerDataMap = {
    '血压异常': `收缩压: ${140 + Math.floor(Math.random() * 40)}mmHg, 舒张压: ${90 + Math.floor(Math.random() * 20)}mmHg`,
    '血糖异常': `血糖值: ${6.1 + Math.random() * 4}mmol/L`,
    '心率异常': `心率: ${100 + Math.floor(Math.random() * 30)}次/分`,
    '体温异常': `体温: ${37.5 + Math.random() * 2}°C`,
    '长期不活动': `连续${8 + Math.floor(Math.random() * 4)}小时无活动记录`,
    '药物未按时服用': `${['降压药', '降糖药', '心脏药'][Math.floor(Math.random() * 3)]}延迟${1 + Math.floor(Math.random() * 3)}小时服用`,
    '跌倒风险': `行走不稳，步态异常评分: ${70 + Math.floor(Math.random() * 20)}`,
    '其他异常': `综合健康评分异常: ${60 + Math.floor(Math.random() * 20)}`
  }
  
  return {
    id: generateId(),
    elderlyName,
    warningType,
    warningLevel,
    title: titles[warningType],
    content: `根据${dataSource}监测数据，发现${elderlyName}的${warningType}情况，需要及时关注和处理。`,
    triggerData: triggerDataMap[warningType],
    dataSource,
    status,
    handlerName: status >= 2 ? ['张医生', '李护士', '王医生'][Math.floor(Math.random() * 3)] : null,
    triggerTime: generateTime(),
    handleTime: status >= 2 ? generateTime(1) : null,
    handleRemark: status === 3 ? `已联系家属，${elderlyName}的${warningType}情况已得到及时处理，目前状况稳定。` : null
  }
}

// 生成模拟数据
const generateMockData = (count = 50) => {
  const data = []
  for (let i = 0; i < count; i++) {
    data.push(generateWarningData())
  }
  return data
}

// 导出模拟数据
export const mockWarningData = generateMockData(50)

// 统计数据
export const mockStatistics = {
  urgent: mockWarningData.filter(item => item.warningLevel === 4).length,
  high: mockWarningData.filter(item => item.warningLevel === 3).length,
  medium: mockWarningData.filter(item => item.warningLevel === 2).length,
  low: mockWarningData.filter(item => item.warningLevel === 1).length
}

// 分页查询函数
export const getMockWarningPage = (params) => {
  let filteredData = [...mockWarningData]
  
  // 过滤条件
  if (params.elderlyName) {
    filteredData = filteredData.filter(item => 
      item.elderlyName.includes(params.elderlyName)
    )
  }
  
  if (params.warningType) {
    filteredData = filteredData.filter(item => 
      item.warningType === params.warningType
    )
  }
  
  if (params.warningLevel !== undefined && params.warningLevel !== '') {
    filteredData = filteredData.filter(item => 
      item.warningLevel === params.warningLevel
    )
  }
  
  if (params.status !== undefined && params.status !== '') {
    filteredData = filteredData.filter(item => 
      item.status === params.status
    )
  }
  
  if (params.startTime && params.endTime) {
    filteredData = filteredData.filter(item => 
      item.triggerTime >= params.startTime && item.triggerTime <= params.endTime
    )
  }
  
  // 排序（按触发时间倒序）
  filteredData.sort((a, b) => new Date(b.triggerTime) - new Date(a.triggerTime))
  
  // 分页
  const pageNum = params.pageNum || 1
  const pageSize = params.pageSize || 10
  const total = filteredData.length
  const start = (pageNum - 1) * pageSize
  const end = start + pageSize
  const list = filteredData.slice(start, end)
  
  return {
    code: 200,
    message: '查询成功',
    data: {
      list,
      total,
      pageNum,
      pageSize,
      pages: Math.ceil(total / pageSize)
    }
  }
}

// 级别统计数据
export const getMockLevelStatistics = () => {
  return {
    code: 200,
    message: '查询成功',
    data: [
      { level: 1, count: mockStatistics.low },
      { level: 2, count: mockStatistics.medium },
      { level: 3, count: mockStatistics.high },
      { level: 4, count: mockStatistics.urgent }
    ]
  }
}
