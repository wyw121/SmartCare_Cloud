<template>
  <div class="equipment-form">
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="loading"
    >
      <!-- 基本信息 -->
      <div class="form-section">
        <h4><el-icon><Monitor /></el-icon> 基本信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备编号" prop="deviceId">
              <el-input v-model="form.deviceId" placeholder="请输入设备编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="deviceName">
              <el-input v-model="form.deviceName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备类型" prop="deviceType">
              <el-select v-model="form.deviceType" placeholder="请选择设备类型" style="width: 100%">
                <el-option label="智能血压计" value="BLOOD_PRESSURE" />
                <el-option label="血糖仪" value="BLOOD_GLUCOSE" />
                <el-option label="智能手环" value="SMART_WATCH" />
                <el-option label="体温计" value="THERMOMETER" />
                <el-option label="心电监护仪" value="ECG_MONITOR" />
                <el-option label="智能体重秤" value="WEIGHT_SCALE" />
                <el-option label="定位器" value="LOCATOR" />
                <el-option label="紧急呼叫器" value="EMERGENCY_BUTTON" />
                <el-option label="环境监测器" value="ENVIRONMENT_SENSOR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择设备状态" style="width: 100%">
                <el-option label="在线" value="ONLINE" />
                <el-option label="离线" value="OFFLINE" />
                <el-option label="故障" value="FAULT" />
                <el-option label="维护中" value="MAINTENANCE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备品牌" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入设备品牌" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入设备型号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="制造商" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入制造商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入设备位置" />
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- 技术参数 -->
      <div class="form-section">
        <h4><el-icon><Setting /></el-icon> 技术参数</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="序列号" prop="serialNumber">
              <el-input v-model="form.serialNumber" placeholder="请输入序列号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="MAC地址" prop="macAddress">
              <el-input v-model="form.macAddress" placeholder="请输入MAC地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="IP地址" prop="ipAddress">
              <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通信协议" prop="protocol">
              <el-select v-model="form.protocol" placeholder="请选择通信协议" style="width: 100%">
                <el-option label="WiFi" value="WIFI" />
                <el-option label="蓝牙" value="BLUETOOTH" />
                <el-option label="ZigBee" value="ZIGBEE" />
                <el-option label="LoRa" value="LORA" />
                <el-option label="4G" value="4G" />
                <el-option label="5G" value="5G" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据频率(秒)" prop="dataFrequency">
              <el-input-number 
                v-model="form.dataFrequency" 
                :min="0" 
                :max="86400"
                placeholder="数据推送频率"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="固件版本" prop="firmwareVersion">
              <el-input v-model="form.firmwareVersion" placeholder="请输入固件版本" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="API端点" prop="apiEndpoint">
          <el-input v-model="form.apiEndpoint" placeholder="请输入API接入端点" />
        </el-form-item>
      </div>

      <!-- 运行状态 -->
      <div class="form-section">
        <h4><el-icon><DataLine /></el-icon> 运行状态</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电池电量(%)" prop="batteryLevel">
              <el-input-number 
                v-model="form.batteryLevel" 
                :min="0" 
                :max="100"
                placeholder="电池电量百分比"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="绑定老人ID" prop="elderlyId">
              <el-input-number 
                v-model="form.elderlyId" 
                :min="1"
                placeholder="绑定老人ID"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="安装日期" prop="installDate">
              <el-date-picker
                v-model="form.installDate"
                type="datetime"
                placeholder="选择安装日期"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保修到期" prop="warrantyExpiry">
              <el-date-picker
                v-model="form.warrantyExpiry"
                type="date"
                placeholder="选择保修到期日期"
                style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!-- 设备描述 -->
      <div class="form-section">
        <h4><el-icon><Document /></el-icon> 设备描述</h4>
        <el-form-item label="设备描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入设备描述信息"
          />
        </el-form-item>
      </div>

      <!-- 配置参数 -->
      <div class="form-section">
        <h4><el-icon><Setting /></el-icon> 配置参数</h4>
        <el-form-item label="配置参数" prop="configParams">
          <el-input
            v-model="form.configParams"
            type="textarea"
            :rows="4"
            placeholder="请输入JSON格式的配置参数"
          />
        </el-form-item>
      </div>
    </el-form>

    <!-- 操作按钮 -->
    <div class="form-actions">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ isEdit ? '更新' : '创建' }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { equipment } from '@/api'
import { DataLine, Document, Monitor, Setting } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { nextTick, reactive, ref, watch } from 'vue'

// Props
const props = defineProps({
  equipment: {
    type: Object,
    default: () => null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['success', 'cancel'])

// 响应式数据
const formRef = ref()
const loading = ref(false)

// 表单数据
const form = reactive({
  deviceId: '',
  deviceName: '',
  deviceType: '',
  brand: '',
  model: '',
  manufacturer: '',
  status: 'OFFLINE',
  location: '',
  elderlyId: null,
  serialNumber: '',
  macAddress: '',
  ipAddress: '',
  protocol: '',
  apiEndpoint: '',
  dataFrequency: 60,
  batteryLevel: null,
  firmwareVersion: '',
  installDate: '',
  warrantyExpiry: '',
  description: '',
  configParams: ''
})

// 表单验证规则
const rules = {
  deviceId: [
    { required: true, message: '请输入设备编号', trigger: 'blur' },
    { min: 3, max: 50, message: '设备编号长度为3-50个字符', trigger: 'blur' }
  ],
  deviceName: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 100, message: '设备名称长度为2-100个字符', trigger: 'blur' }
  ],
  deviceType: [
    { required: true, message: '请选择设备类型', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择设备状态', trigger: 'change' }
  ],
  macAddress: [
    { 
      pattern: /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/, 
      message: 'MAC地址格式不正确', 
      trigger: 'blur' 
    }
  ],
  ipAddress: [
    { 
      pattern: /^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, 
      message: 'IP地址格式不正确', 
      trigger: 'blur' 
    }
  ],
  configParams: [
    {
      validator: (rule, value, callback) => {
        if (value && value.trim()) {
          try {
            JSON.parse(value)
            callback()
          } catch (error) {
            callback(new Error('配置参数必须是有效的JSON格式'))
          }
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 监听props变化，初始化表单
watch(
  () => props.equipment,
  (newEquipment) => {
    if (newEquipment && props.isEdit) {
      nextTick(() => {
        Object.keys(form).forEach(key => {
          if (newEquipment[key] !== undefined) {
            form[key] = newEquipment[key]
          }
        })
      })
    } else {
      resetForm()
    }
  },
  { immediate: true }
)

// 重置表单
const resetForm = () => {
  Object.keys(form).forEach(key => {
    if (key === 'status') {
      form[key] = 'OFFLINE'
    } else if (key === 'dataFrequency') {
      form[key] = 60
    } else if (typeof form[key] === 'number') {
      form[key] = null
    } else {
      form[key] = ''
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    loading.value = true
    
    const submitData = { ...form }
    
    // 清空空值字段
    Object.keys(submitData).forEach(key => {
      if (submitData[key] === '' || submitData[key] === null) {
        delete submitData[key]
      }
    })
    
    let response
    if (props.isEdit) {
      response = await equipment.update(props.equipment.id, submitData)
    } else {
      response = await equipment.create(submitData)
    }
    
    if (response.code === 200) {
      ElMessage.success(props.isEdit ? '设备更新成功' : '设备创建成功')
      emit('success')
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error.message) {
      console.error('表单提交失败:', error)
      ElMessage.error('操作失败：' + error.message)
    }
  } finally {
    loading.value = false
  }
}

// 取消操作
const handleCancel = () => {
  emit('cancel')
}
</script>

<style scoped>
.equipment-form {
  padding: 20px;
}

.form-section {
  margin-bottom: 30px;
}

.form-section h4 {
  color: #303133;
  font-size: 16px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #ebeef5;
}

.form-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.form-actions .el-button {
  margin: 0 10px;
}

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #606266;
}

:deep(.el-input-number .el-input__inner) {
  text-align: left;
}

:deep(.el-textarea__inner) {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
}
</style>
