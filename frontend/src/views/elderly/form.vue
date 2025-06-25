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
            <el-form-item label="紧急联系人" prop="emergencyContactName">
              <el-input v-model="form.emergencyContactName" placeholder="请输入紧急联系人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人电话" prop="emergencyContactPhone">
              <el-input v-model="form.emergencyContactPhone" placeholder="请输入联系人电话" />
            </el-form-item>
          </el-col>
        </el-row>

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
          <el-col :span="12">
            <el-form-item label="医保类型" prop="insuranceType">
              <el-input v-model="form.insuranceType" placeholder="请输入医保类型" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="既往病史" prop="medicalHistory">
          <el-input
            v-model="form.medicalHistory"
            type="textarea"
            :rows="3"
            placeholder="请输入既往病史"
          />
        </el-form-item>

        <el-form-item label="过敏史" prop="allergyHistory">
          <el-input
            v-model="form.allergyHistory"
            type="textarea"
            :rows="3"
            placeholder="请输入过敏史"
          />
        </el-form-item>

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
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const formRef = ref()
const loading = ref(false)
const isEdit = ref(false)
const elderlyId = ref(null)

// 表单数据
const form = reactive({
  name: '',
  gender: 1,
  idCard: '',
  birthDate: '',
  phone: '',
  address: '',
  emergencyContactName: '',
  emergencyContactPhone: '',
  healthStatus: 'HEALTHY',
  careLevel: 1,
  insuranceType: '',
  medicalHistory: '',
  allergyHistory: '',
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
  ],
  emergencyContactName: [
    { required: true, message: '请输入紧急联系人姓名', trigger: 'blur' }
  ],
  emergencyContactPhone: [
    { required: true, message: '请输入紧急联系人电话', trigger: 'blur' },
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

// 获取老人信息
const getElderlyInfo = async () => {
  try {
    loading.value = true
    // 这里应该调用真实的API
    // const { data } = await getElderlyById(elderlyId.value)
    
    // 模拟数据
    const mockData = {
      name: '张三',
      gender: 1,
      idCard: '110101195001011234',
      birthDate: '1950-01-01',
      phone: '13800138001',
      address: '北京市朝阳区某某街道1号',
      emergencyContactName: '张小明',
      emergencyContactPhone: '13900139001',
      healthStatus: 'HEALTHY',
      careLevel: 1,
      insuranceType: '城镇职工医保',
      medicalHistory: '高血压病史10年',
      allergyHistory: '青霉素过敏',
      remarks: ''
    }
    
    Object.assign(form, mockData)
  } catch (error) {
    console.error('获取老人信息失败:', error)
    ElMessage.error('获取老人信息失败')
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
      // await updateElderly({ ...form, id: elderlyId.value })
      ElMessage.success('更新老人档案成功')
    } else {
      // 新增老人信息
      // await addElderly(form)
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
