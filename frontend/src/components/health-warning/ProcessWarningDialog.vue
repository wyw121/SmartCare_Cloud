<template>
  <el-dialog
    v-model="visible"
    title="处理预警"
    width="500px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="processForm"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="预警信息">
        <div class="warning-info">
          <p><strong>老人姓名：</strong>{{ warningData.elderlyName }}</p>
          <p><strong>预警类型：</strong>{{ warningData.warningType }}</p>
          <p><strong>预警内容：</strong>{{ warningData.warningContent }}</p>
        </div>
      </el-form-item>
      
      <el-form-item label="处理方式" prop="handleType">
        <el-radio-group v-model="processForm.handleType">
          <el-radio-button label="immediate">立即处理</el-radio-button>
          <el-radio-button label="follow">后续跟进</el-radio-button>
          <el-radio-button label="refer">转介处理</el-radio-button>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="处理结果" prop="handleResult">
        <el-input
          v-model="processForm.handleResult"
          type="textarea"
          :rows="4"
          placeholder="请详细描述处理过程和结果"
        />
      </el-form-item>
      
      <el-form-item label="下次跟进" v-if="processForm.handleType === 'follow'">
        <el-date-picker
          v-model="processForm.followUpTime"
          type="datetime"
          placeholder="选择跟进时间"
          format="YYYY-MM-DD HH:mm"
          value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>
      
      <el-form-item label="转介科室" v-if="processForm.handleType === 'refer'" prop="referDepartment">
        <el-select v-model="processForm.referDepartment" placeholder="请选择科室">
          <el-option label="心血管科" value="心血管科" />
          <el-option label="内分泌科" value="内分泌科" />
          <el-option label="神经内科" value="神经内科" />
          <el-option label="急诊科" value="急诊科" />
          <el-option label="康复科" value="康复科" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input
          v-model="processForm.remark"
          type="textarea"
          :rows="2"
          placeholder="其他备注信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  warningData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'submit'])

const formRef = ref()
const submitLoading = ref(false)

const processForm = reactive({
  handleType: 'immediate',
  handleResult: '',
  followUpTime: '',
  referDepartment: '',
  remark: ''
})

const formRules = {
  handleType: [
    { required: true, message: '请选择处理方式', trigger: 'change' }
  ],
  handleResult: [
    { required: true, message: '请填写处理结果', trigger: 'blur' },
    { min: 10, message: '处理结果至少10个字符', trigger: 'blur' }
  ],
  referDepartment: [
    { required: true, message: '请选择转介科室', trigger: 'change' }
  ]
}

// 监听visible变化，重置表单
watch(() => props.visible, (newVal) => {
  if (!newVal) {
    resetForm()
  }
})

const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(processForm, {
    handleType: 'immediate',
    handleResult: '',
    followUpTime: '',
    referDepartment: '',
    remark: ''
  })
}

const handleClose = () => {
  emit('update:visible', false)
}

const handleSubmit = async () => {
  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    submitLoading.value = true
    
    // 构建提交数据
    const submitData = {
      warningId: props.warningData.id,
      ...processForm,
      handleTime: new Date().toLocaleString()
    }
    
    emit('submit', submitData)
    
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped>
.warning-info {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 10px;
}

.warning-info p {
  margin: 5px 0;
  color: #606266;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
