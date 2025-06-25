<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑医生' : '新增医生'"
    width="700px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="doctorForm"
      :rules="formRules"
      label-width="100px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工号" prop="employeeNumber">
            <el-input v-model="doctorForm.employeeNumber" placeholder="请输入工号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="doctorForm.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="doctorForm.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄" prop="age">
            <el-input-number v-model="doctorForm.age" :min="20" :max="70" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系电话" prop="phone">
            <el-input v-model="doctorForm.phone" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="doctorForm.email" placeholder="请输入邮箱" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="科室" prop="department">
            <el-select v-model="doctorForm.department" placeholder="请选择科室" style="width: 100%">
              <el-option label="内科" value="内科" />
              <el-option label="外科" value="外科" />
              <el-option label="儿科" value="儿科" />
              <el-option label="妇科" value="妇科" />
              <el-option label="眼科" value="眼科" />
              <el-option label="耳鼻喉科" value="耳鼻喉科" />
              <el-option label="皮肤科" value="皮肤科" />
              <el-option label="神经科" value="神经科" />
              <el-option label="心血管科" value="心血管科" />
              <el-option label="骨科" value="骨科" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职称" prop="title">
            <el-select v-model="doctorForm.title" placeholder="请选择职称" style="width: 100%">
              <el-option label="主任医师" value="主任医师" />
              <el-option label="副主任医师" value="副主任医师" />
              <el-option label="主治医师" value="主治医师" />
              <el-option label="住院医师" value="住院医师" />
              <el-option label="实习医师" value="实习医师" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="学历" prop="education">
            <el-select v-model="doctorForm.education" placeholder="请选择学历" style="width: 100%">
              <el-option label="医学博士" value="医学博士" />
              <el-option label="医学硕士" value="医学硕士" />
              <el-option label="医学学士" value="医学学士" />
              <el-option label="专科" value="专科" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作年限" prop="workYears">
            <el-input-number v-model="doctorForm.workYears" :min="0" :max="50" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="专长" prop="specialization">
        <el-input
          v-model="doctorForm.specialization"
          type="textarea"
          :rows="3"
          placeholder="请输入专业特长"
        />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input v-model="doctorForm.address" placeholder="请输入地址" />
      </el-form-item>

      <el-form-item label="个人简介">
        <el-input
          v-model="doctorForm.introduction"
          type="textarea"
          :rows="4"
          placeholder="请输入个人简介"
        />
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="doctorForm.status">
          <el-radio :label="1">在职</el-radio>
          <el-radio :label="0">离职</el-radio>
        </el-radio-group>
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
  isEdit: {
    type: Boolean,
    default: false
  },
  doctorData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'submit'])

const formRef = ref()
const submitLoading = ref(false)

const doctorForm = reactive({
  id: null,
  employeeNumber: '',
  name: '',
  gender: '男',
  age: null,
  phone: '',
  email: '',
  department: '',
  title: '',
  education: '',
  workYears: null,
  specialization: '',
  address: '',
  introduction: '',
  status: 1
})

const formRules = {
  employeeNumber: [
    { required: true, message: '请输入工号', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请选择科室', trigger: 'change' }
  ],
  title: [
    { required: true, message: '请选择职称', trigger: 'change' }
  ],
  specialization: [
    { required: true, message: '请输入专业特长', trigger: 'blur' }
  ]
}

// 监听doctorData变化，更新表单数据
watch(() => props.doctorData, (newData) => {
  if (newData && Object.keys(newData).length > 0) {
    Object.assign(doctorForm, newData)
  }
}, { immediate: true, deep: true })

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
  Object.assign(doctorForm, {
    id: null,
    employeeNumber: '',
    name: '',
    gender: '男',
    age: null,
    phone: '',
    email: '',
    department: '',
    title: '',
    education: '',
    workYears: null,
    specialization: '',
    address: '',
    introduction: '',
    status: 1
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
    
    // 提交数据
    const submitData = { ...doctorForm }
    emit('submit', submitData)
    
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
