<template>
  <div class="device-integration-detail" v-if="deviceInfo.deviceType">
    <!-- 设备基本信息 -->
    <div class="info-section">
      <h4><el-icon><Monitor /></el-icon> 设备信息</h4>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备类型">
          <el-tag type="primary">{{ deviceInfo.deviceType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="通信协议">
          <el-tag>{{ deviceInfo.protocol }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="数据格式">
          <el-tag type="success">{{ deviceInfo.dataFormat }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上报频率">
          {{ deviceInfo.frequency }}
        </el-descriptions-item>
        <el-descriptions-item label="设备描述" :span="2">
          {{ deviceInfo.description }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!-- API接入参数 -->
    <div class="info-section">
      <h4><el-icon><Link /></el-icon> API接入参数</h4>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="api-card">
            <template #header>
              <span class="card-title">基础配置</span>
            </template>
            <div class="api-item">
              <label>基础URL：</label>
              <el-input 
                v-model="deviceInfo.baseUrl" 
                readonly
                class="copy-input"
              >
                <template #append>
                  <el-button @click="copyToClipboard(deviceInfo.baseUrl)">
                    <el-icon><DocumentCopy /></el-icon>
                  </el-button>
                </template>
              </el-input>
            </div>
            <div class="api-item">
              <label>接入端点：</label>
              <el-input 
                v-model="deviceInfo.apiEndpoint" 
                readonly
                class="copy-input"
              >
                <template #append>
                  <el-button @click="copyToClipboard(deviceInfo.apiEndpoint)">
                    <el-icon><DocumentCopy /></el-icon>
                  </el-button>
                </template>
              </el-input>
            </div>
            <div class="api-item">
              <label>请求方法：</label>
              <el-tag type="warning">{{ deviceInfo.httpMethod }}</el-tag>
            </div>
            <div class="api-item">
              <label>Content-Type：</label>
              <el-tag>{{ deviceInfo.contentType }}</el-tag>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="api-card">
            <template #header>
              <span class="card-title">认证配置</span>
            </template>
            <div class="api-item">
              <label>认证方式：</label>
              <el-tag type="success">{{ deviceInfo.authentication }}</el-tag>
            </div>
            <div class="api-item">
              <label>超时时间：</label>
              <span>{{ deviceInfo.timeout }}</span>
            </div>
            <div class="api-item">
              <label>重试策略：</label>
              <span>{{ deviceInfo.retryPolicy }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 数据字段说明 -->
    <div class="info-section">
      <h4><el-icon><DataLine /></el-icon> 数据字段说明</h4>
      <el-table :data="fieldTableData" border stripe>
        <el-table-column prop="field" label="字段名" width="150" />
        <el-table-column prop="type" label="数据类型" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getFieldTypeColor(scope.row.type)">
              {{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="required" label="是否必填" width="100">
          <template #default="scope">
            <el-tag 
              size="small" 
              :type="scope.row.required ? 'danger' : 'info'"
            >
              {{ scope.row.required ? '必填' : '可选' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="字段说明" />
        <el-table-column prop="example" label="示例值" width="120" />
      </el-table>
    </div>

    <!-- 数据示例 -->
    <div class="info-section">
      <h4><el-icon><Document /></el-icon> 数据示例</h4>
      <el-card class="code-card">
        <template #header>
          <div class="code-header">
            <span>JSON数据格式示例</span>
            <el-button 
              size="small" 
              @click="copyToClipboard(deviceInfo.sampleData)"
            >
              <el-icon><DocumentCopy /></el-icon>
              复制
            </el-button>
          </div>
        </template>
        <pre class="code-content">{{ formatJson(deviceInfo.sampleData) }}</pre>
      </el-card>
    </div>

    <!-- cURL示例 -->
    <div class="info-section">
      <h4><el-icon><Monitor /></el-icon> cURL调用示例</h4>
      <el-card class="code-card">
        <template #header>
          <div class="code-header">
            <span>命令行调用示例</span>
            <el-button 
              size="small" 
              @click="copyToClipboard(curlExample)"
            >
              <el-icon><DocumentCopy /></el-icon>
              复制
            </el-button>
          </div>
        </template>
        <pre class="code-content">{{ curlExample }}</pre>
      </el-card>
    </div>

    <!-- 代码示例 -->
    <div class="info-section">
      <h4><el-icon><Edit /></el-icon> 代码示例</h4>
      <el-tabs>
        <el-tab-pane label="JavaScript" name="javascript">
          <el-card class="code-card">
            <template #header>
              <div class="code-header">
                <span>JavaScript接入示例</span>
                <el-button 
                  size="small" 
                  @click="copyToClipboard(jsExample)"
                >
                  <el-icon><DocumentCopy /></el-icon>
                  复制
                </el-button>
              </div>
            </template>
            <pre class="code-content">{{ jsExample }}</pre>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="Python" name="python">
          <el-card class="code-card">
            <template #header>
              <div class="code-header">
                <span>Python接入示例</span>
                <el-button 
                  size="small" 
                  @click="copyToClipboard(pythonExample)"
                >
                  <el-icon><DocumentCopy /></el-icon>
                  复制
                </el-button>
              </div>
            </template>
            <pre class="code-content">{{ pythonExample }}</pre>
          </el-card>
        </el-tab-pane>
        <el-tab-pane label="Java" name="java">
          <el-card class="code-card">
            <template #header>
              <div class="code-header">
                <span>Java接入示例</span>
                <el-button 
                  size="small" 
                  @click="copyToClipboard(javaExample)"
                >
                  <el-icon><DocumentCopy /></el-icon>
                  复制
                </el-button>
              </div>
            </template>
            <pre class="code-content">{{ javaExample }}</pre>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
  <div v-else class="loading-placeholder">
    <el-empty description="请选择设备类型查看接入信息" />
  </div>
</template>

<script setup>
import { DataLine, Document, DocumentCopy, Edit, Link, Monitor } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed } from 'vue'

// Props
const props = defineProps({
  deviceInfo: {
    type: Object,
    default: () => ({})
  }
})

// 计算属性
const fieldTableData = computed(() => {
  if (!props.deviceInfo.requiredFields) return []
  
  const fieldMap = {
    'systolic': { type: 'number', description: '收缩压值(mmHg)', example: '120' },
    'diastolic': { type: 'number', description: '舒张压值(mmHg)', example: '80' },
    'heartRate': { type: 'number', description: '心率(次/分钟)', example: '72' },
    'glucose': { type: 'number', description: '血糖值(mmol/L)', example: '6.5' },
    'measureType': { type: 'string', description: '测量类型', example: 'fasting' },
    'steps': { type: 'number', description: '步数', example: '5000' },
    'sleepDuration': { type: 'number', description: '睡眠时长(分钟)', example: '480' },
    'temperature': { type: 'number', description: '体温(°C)', example: '36.5' },
    'measureSite': { type: 'string', description: '测量部位', example: 'forehead' },
    'ecgData': { type: 'string', description: 'ECG数据(Base64)', example: 'base64data...' },
    'heartRhythm': { type: 'string', description: '心律状态', example: 'normal' },
    'weight': { type: 'number', description: '体重(kg)', example: '65.5' },
    'bmi': { type: 'number', description: 'BMI指数', example: '23.2' },
    'bodyFat': { type: 'number', description: '体脂率(%)', example: '18.5' },
    'latitude': { type: 'number', description: '纬度', example: '39.9042' },
    'longitude': { type: 'number', description: '经度', example: '116.4074' },
    'accuracy': { type: 'number', description: '定位精度(米)', example: '5' },
    'alertType': { type: 'string', description: '警报类型', example: 'emergency' },
    'location': { type: 'object', description: '位置信息', example: '{lat,lng}' },
    'humidity': { type: 'number', description: '湿度(%)', example: '45' },
    'airQuality': { type: 'string', description: '空气质量', example: 'good' },
    'timestamp': { type: 'string', description: '时间戳(ISO8601)', example: '2025-07-08T10:30:00' },
    'deviceId': { type: 'string', description: '设备ID', example: 'BP001' },
    'elderlyId': { type: 'number', description: '老人ID', example: '1' }
  }
  
  return props.deviceInfo.requiredFields.map(field => ({
    field,
    required: true,
    ...fieldMap[field] || { type: 'string', description: '数据字段', example: '-' }
  }))
})

const curlExample = computed(() => {
  if (!props.deviceInfo.baseUrl || !props.deviceInfo.apiEndpoint) return ''
  
  return `curl -X POST "${props.deviceInfo.baseUrl}${props.deviceInfo.apiEndpoint}" \\
  -H "Content-Type: application/json" \\
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \\
  -d '${props.deviceInfo.sampleData || '{}'}'`
})

const jsExample = computed(() => {
  return `// JavaScript 接入示例
const deviceData = ${props.deviceInfo.sampleData || '{}'};

async function sendDeviceData(data) {
  try {
    const response = await fetch('${props.deviceInfo.baseUrl || 'https://api.smartcare.com'}${props.deviceInfo.apiEndpoint || '/api/devices/data'}', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
      },
      body: JSON.stringify(data)
    });
    
    const result = await response.json();
    console.log('数据上报成功:', result);
  } catch (error) {
    console.error('数据上报失败:', error);
  }
}

// 发送数据
sendDeviceData(deviceData);`
})

const pythonExample = computed(() => {
  return `# Python 接入示例
import requests
import json
from datetime import datetime

# 设备数据
device_data = ${props.deviceInfo.sampleData || '{}'}

def send_device_data(data):
    url = "${props.deviceInfo.baseUrl || 'https://api.smartcare.com'}${props.deviceInfo.apiEndpoint || '/api/devices/data'}"
    headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer YOUR_ACCESS_TOKEN'
    }
    
    try:
        response = requests.post(url, headers=headers, json=data, timeout=30)
        response.raise_for_status()
        print("数据上报成功:", response.json())
    except requests.RequestException as e:
        print("数据上报失败:", e)

# 发送数据
send_device_data(device_data)`
})

const javaExample = computed(() => {
  return `// Java 接入示例
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

public class DeviceDataSender {
    
    private static final String BASE_URL = "${props.deviceInfo.baseUrl || 'https://api.smartcare.com'}";
    private static final String ENDPOINT = "${props.deviceInfo.apiEndpoint || '/api/devices/data'}";
    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN";
    
    public void sendDeviceData(String jsonData) {
        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
            
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + ACCESS_TOKEN)
            .POST(HttpRequest.BodyPublishers.ofString(jsonData))
            .build();
            
        try {
            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());
            System.out.println("数据上报成功: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("数据上报失败: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        String deviceData = "${props.deviceInfo.sampleData || '{}'}";
        new DeviceDataSender().sendDeviceData(deviceData);
    }
}`
})

// 方法
const getFieldTypeColor = (type) => {
  const colorMap = {
    'string': '',
    'number': 'success',
    'object': 'warning',
    'boolean': 'info'
  }
  return colorMap[type] || ''
}

const formatJson = (jsonString) => {
  try {
    const obj = JSON.parse(jsonString)
    return JSON.stringify(obj, null, 2)
  } catch {
    return jsonString
  }
}

const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success('已复制到剪贴板')
  } catch {
    ElMessage.error('复制失败')
  }
}
</script>

<style scoped>
.device-integration-detail {
  padding: 20px 0;
}

.info-section {
  margin-bottom: 30px;
}

.info-section h4 {
  color: #303133;
  font-size: 16px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.api-card {
  height: 100%;
}

.card-title {
  font-weight: 600;
  color: #303133;
}

.api-item {
  margin-bottom: 15px;
}

.api-item label {
  display: block;
  color: #606266;
  font-size: 14px;
  margin-bottom: 5px;
  font-weight: 500;
}

.copy-input {
  width: 100%;
}

.code-card {
  margin-bottom: 15px;
}

.code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.code-content {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.5;
  color: #303133;
  margin: 0;
  overflow-x: auto;
  white-space: pre;
}

.loading-placeholder {
  text-align: center;
  padding: 60px 0;
}

:deep(.el-descriptions__cell) {
  padding: 12px !important;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
}
</style>
