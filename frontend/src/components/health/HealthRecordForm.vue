<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑健康记录' : '添加健康记录'"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="default"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="记录类型" prop="recordType">
            <el-select v-model="form.recordType" placeholder="请选择记录类型">
              <el-option label="体检记录" :value="1" />
              <el-option label="就诊记录" :value="2" />
              <el-option label="用药记录" :value="3" />
              <el-option label="监测记录" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="记录日期" prop="recordDate">
            <el-date-picker
              v-model="form.recordDate"
              type="datetime"
              placeholder="请选择记录日期"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="标题">
            <el-input v-model="form.title" placeholder="请输入记录标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="医生">
            <el-input v-model="form.doctorName" placeholder="请输入医生姓名" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 生命体征 -->
      <el-divider content-position="left">生命体征</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="收缩压 (mmHg)">
            <el-input-number
              v-model="form.bloodPressureHigh"
              :min="0"
              :max="300"
              placeholder="收缩压"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="舒张压 (mmHg)">
            <el-input-number
              v-model="form.bloodPressureLow"
              :min="0"
              :max="200"
              placeholder="舒张压"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="心率 (次/分)">
            <el-input-number
              v-model="form.heartRate"
              :min="0"
              :max="300"
              placeholder="心率"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="体温 (℃)">
            <el-input-number
              v-model="form.temperature"
              :min="35"
              :max="45"
              :precision="1"
              placeholder="体温"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="血糖 (mmol/L)">
            <el-input-number
              v-model="form.bloodSugar"
              :min="0"
              :max="50"
              :precision="1"
              placeholder="血糖"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="体重 (kg)">
            <el-input-number
              v-model="form.weight"
              :min="0"
              :max="300"
              :precision="1"
              placeholder="体重"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 诊断信息 -->
      <el-divider content-position="left">诊断信息</el-divider>
      
      <el-form-item label="主要症状">
        <el-input
          v-model="form.symptoms"
          type="textarea"
          :rows="2"
          placeholder="请输入主要症状"
        />
      </el-form-item>

      <el-form-item label="诊断结果">
        <el-input
          v-model="form.diagnosis"
          type="textarea"
          :rows="2"
          placeholder="请输入诊断结果"
        />
      </el-form-item>

      <el-form-item label="治疗方案">
        <el-input
          v-model="form.treatment"
          type="textarea"
          :rows="2"
          placeholder="请输入治疗方案"
        />
      </el-form-item>

      <el-form-item label="用药情况">
        <el-input
          v-model="form.medication"
          type="textarea"
          :rows="2"
          placeholder="请输入用药情况"
        />
      </el-form-item>

      <el-form-item label="记录描述">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入详细描述"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险等级">
            <el-select v-model="form.riskLevel" placeholder="请选择风险等级">
              <el-option label="正常" :value="0" />
              <el-option label="轻度" :value="1" />
              <el-option label="中度" :value="2" />
              <el-option label="重度" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否异常">
            <el-switch
              v-model="form.isAbnormal"
              active-text="异常"
              inactive-text="正常"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSubmit">
          {{ isEdit ? '更新' : '保存' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { addHealthRecord, updateHealthRecord } from '@/api/elderly'
import { ElMessage } from 'element-plus'
import { computed, reactive, ref, watch } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  elderlyId: {
    type: [String, Number],
    required: true
  },
  record: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref()
const saving = ref(false)

const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const isEdit = computed(() => !!props.record?.id)

// 表单数据
const form = reactive({
  recordType: 1,
  recordDate: '',
  title: '',
  doctorName: '',
  bloodPressureHigh: null,
  bloodPressureLow: null,
  heartRate: null,
  temperature: null,
  bloodSugar: null,
  weight: null,
  symptoms: '',
  diagnosis: '',
  treatment: '',
  medication: '',
  description: '',
  riskLevel: 0,
  isAbnormal: false,
  remarks: ''
})

// 表单验证规则
const rules = {
  recordType: [
    { required: true, message: '请选择记录类型', trigger: 'change' }
  ],
  recordDate: [
    { required: true, message: '请选择记录日期', trigger: 'change' }
  ]
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    recordType: 1,
    recordDate: '',
    title: '',
    doctorName: '',
    bloodPressureHigh: null,
    bloodPressureLow: null,
    heartRate: null,
    temperature: null,
    bloodSugar: null,
    weight: null,
    symptoms: '',
    diagnosis: '',
    treatment: '',
    medication: '',
    description: '',
    riskLevel: 0,
    isAbnormal: false,
    remarks: ''
  })
}

// 监听record变化，用于编辑
watch(() => props.record, (newRecord) => {
  if (newRecord) {
    Object.assign(form, {
      recordType: newRecord.recordType || 1,
      recordDate: newRecord.recordDate || '',
      title: newRecord.title || '',
      doctorName: newRecord.doctorName || '',
      bloodPressureHigh: newRecord.bloodPressureHigh || null,
      bloodPressureLow: newRecord.bloodPressureLow || null,
      heartRate: newRecord.heartRate || null,
      temperature: newRecord.temperature || null,
      bloodSugar: newRecord.bloodSugar || null,
      weight: newRecord.weight || null,
      symptoms: newRecord.symptoms || '',
      diagnosis: newRecord.diagnosis || '',
      treatment: newRecord.treatment || '',
      medication: newRecord.medication || '',
      description: newRecord.description || '',
      riskLevel: newRecord.riskLevel || 0,
      isAbnormal: newRecord.isAbnormal || false,
      remarks: newRecord.remarks || ''
    })
  } else {
    resetForm()
  }
}, { immediate: true })

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    
    saving.value = true
    
    const data = {
      ...form,
      elderlyId: props.elderlyId
    }
    
    if (isEdit.value) {
      data.id = props.record.id
      await updateHealthRecord(data)
      ElMessage.success('更新健康记录成功')
    } else {
      await addHealthRecord(props.elderlyId, data)
      ElMessage.success('添加健康记录成功')
    }
    
    emit('success')
    handleClose()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败：' + (error.message || '未知错误'))
  } finally {
    saving.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
  resetForm()
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0 15px 0;
}

.el-form-item {
  margin-bottom: 18px;
}
</style>
