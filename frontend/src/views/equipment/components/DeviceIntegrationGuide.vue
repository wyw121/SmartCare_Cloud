<template>
  <div class="device-integration-guide">
    <!-- 概述 -->
    <div class="guide-section">
      <h3><el-icon><InfoFilled /></el-icon> 设备接入概述</h3>
      <el-alert
        title="智慧医养设备接入平台"
        description="本平台支持多种智能医疗设备的接入，包括血压计、血糖仪、智能手环等，为老年人提供全方位的健康监测服务。"
        type="info"
        :closable="false"
        show-icon
      />
    </div>

    <!-- 设备类型选择 -->
    <div class="guide-section">
      <h3><el-icon><Monitor /></el-icon> 选择设备类型</h3>
      <el-tabs v-model="activeTab" type="card" @tab-click="loadDeviceInfo">
        <el-tab-pane label="智能血压计" name="BLOOD_PRESSURE">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="血糖仪" name="BLOOD_GLUCOSE">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="智能手环" name="SMART_WATCH">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="体温计" name="THERMOMETER">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="心电监护仪" name="ECG_MONITOR">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="智能体重秤" name="WEIGHT_SCALE">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="定位器" name="LOCATOR">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="紧急呼叫器" name="EMERGENCY_BUTTON">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
        <el-tab-pane label="环境监测器" name="ENVIRONMENT_SENSOR">
          <device-integration-detail :device-info="currentDeviceInfo" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 通用接入流程 -->
    <div class="guide-section">
      <h3><el-icon><List /></el-icon> 通用接入流程</h3>
      <el-steps :active="4" align-center>
        <el-step title="设备注册" description="在平台注册设备信息" />
        <el-step title="API配置" description="配置设备API接入参数" />
        <el-step title="数据测试" description="发送测试数据验证连接" />
        <el-step title="正式接入" description="设备开始正常上报数据" />
        <el-step title="监控运维" description="持续监控设备状态" />
      </el-steps>
    </div>

    <!-- 技术支持 -->
    <div class="guide-section">
      <h3><el-icon><Service /></el-icon> 技术支持</h3>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="support-card">
            <template #header>
              <div class="card-header">
                <el-icon><Phone /></el-icon>
                <span>技术热线</span>
              </div>
            </template>
            <div class="support-content">
              <p><strong>7×24小时技术支持</strong></p>
              <p>电话：400-888-9999</p>
              <p>工作日：9:00-18:00</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="support-card">
            <template #header>
              <div class="card-header">
                <el-icon><Message /></el-icon>
                <span>在线咨询</span>
              </div>
            </template>
            <div class="support-content">
              <p><strong>即时技术咨询</strong></p>
              <p>邮箱：support@smartcare.com</p>
              <p>QQ群：123456789</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="support-card">
            <template #header>
              <div class="card-header">
                <el-icon><Document /></el-icon>
                <span>开发文档</span>
              </div>
            </template>
            <div class="support-content">
              <p><strong>详细技术文档</strong></p>
              <p>API文档、SDK下载</p>
              <p>集成示例代码</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { equipment } from '@/api'
import { Document, InfoFilled, List, Message, Monitor, Phone, Service } from '@element-plus/icons-vue'
import { onMounted, ref } from 'vue'
import DeviceIntegrationDetail from './DeviceIntegrationDetail.vue'

// 响应式数据
const activeTab = ref('BLOOD_PRESSURE')
const currentDeviceInfo = ref({})
const allDeviceInfo = ref({})

// 加载设备信息
const loadDeviceInfo = async () => {
  try {
    if (!allDeviceInfo.value[activeTab.value]) {
      const response = await equipment.getIntegrationInfo(activeTab.value)
      if (response.code === 200) {
        allDeviceInfo.value[activeTab.value] = response.data
      }
    }
    currentDeviceInfo.value = allDeviceInfo.value[activeTab.value] || {}
  } catch (error) {
    console.error('加载设备信息失败:', error)
  }
}

// 初始化
onMounted(() => {
  loadDeviceInfo()
})
</script>

<style scoped>
.device-integration-guide {
  padding: 20px;
}

.guide-section {
  margin-bottom: 30px;
}

.guide-section h3 {
  color: #303133;
  font-size: 18px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.support-card {
  height: 100%;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.support-content {
  text-align: center;
}

.support-content p {
  margin: 8px 0;
  color: #606266;
}

.support-content p:first-child {
  color: #303133;
  font-weight: 600;
}

:deep(.el-tabs__header) {
  margin-bottom: 20px;
}

:deep(.el-steps) {
  margin: 20px 0;
}
</style>
