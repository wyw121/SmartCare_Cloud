<template>
  <div class="elderly-form">
    <el-card>
      <template #header>
        <span>{{ isEdit ? '编辑老人档案' : '新增老人档案' }}</span>
      </template>
      
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        size="default"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入老人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="form.birthDate"
                type="date"
                placeholder="请选择出生日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="照护等级" prop="careLevel">
              <el-select v-model="form.careLevel" placeholder="请选择照护等级">
                <el-option label="自理" :value="1" />
                <el-option label="半自理" :value="2" />
                <el-option label="不能自理" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="家庭地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入家庭地址" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="健康状况" prop="healthStatus">
              <el-select v-model="form.healthStatus" placeholder="请选择健康状况">
                <el-option label="健康" value="HEALTHY" />
                <el-option label="亚健康" value="SUBHEALTH" />
                <el-option label="疾病" value="SICK" />
                <el-option label="重病" value="SERIOUS" />
                <el-option label="危险" value="DANGER" />
                <el-option label="预警" value="WARNING" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="form.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            {{ isEdit ? '更新' : '提交' }}
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getElderlyById, updateElderly, createElderly } from '@/api/elderly'

const route = useRoute()
const router = useRouter()

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)
const elderlyId = ref(null)

// 表单数据
const form = reactive({
  name: '',
  gender: 1,  // 数字类型，1=男，0=女
  idCard: '',
  birthDate: '',
  phone: '',
  address: '',
  healthStatus: 'HEALTHY',
  careLevel: 1,
  remarks: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入老人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 初始化
onMounted(() => {
  elderlyId.value = route.params.id
  if (elderlyId.value) {
    isEdit.value = true
    getElderlyInfo()
  }
})

// 监听性别字段变化（用于调试）
watch(() => form.gender, (newValue, oldValue) => {
  console.log('性别字段发生变化:', { oldValue, newValue })
}, { immediate: true })

// 获取老人信息
const getElderlyInfo = async () => {
  try {
    loading.value = true
    const response = await getElderlyById(elderlyId.value)
    const data = response.data
    
    // 调试信息：打印接收到的数据
    console.log('接收到的老人数据:', data)
    console.log('性别字段值:', data.gender, '类型:', typeof data.gender)
    
    // 将后端数据赋值到表单
    Object.assign(form, {
      name: data.name || '',
      gender: data.gender !== undefined ? Number(data.gender) : 1,  // 确保类型为数字
      idCard: data.idCard || '',
      birthDate: data.birthDate || '',
      phone: data.phone || '',
      address: data.address || '',
      healthStatus: data.healthStatus || 'HEALTHY',
      careLevel: data.careLevel !== undefined ? data.careLevel : 1,
      remarks: data.remarks || ''
    })
    
    // 调试信息：打印赋值后的表单数据
    console.log('表单gender字段:', form.gender, '类型:', typeof form.gender)
  } catch (error) {
    console.error('获取老人信息失败:', error)
    ElMessage.error('获取老人信息失败')
    // 如果获取失败，可以选择返回到列表页面
    // router.push('/elderly/list')
  } finally {
    loading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    
    if (isEdit.value) {
      // 更新老人信息
      await updateElderly({ ...form, id: elderlyId.value })
      ElMessage.success('更新老人档案成功')
    } else {
      // 新增老人信息
      await createElderly(form)
      ElMessage.success('新增老人档案成功')
    }
    
    router.push('/elderly/list')
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
  } finally {
    loading.value = false
  }
}

// 取消
const handleCancel = () => {
  router.back()
}
</script>

<style scoped>
.elderly-form {
  padding: 20px;
}
</style>
