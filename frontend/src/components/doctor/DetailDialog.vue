<template>
  <el-dialog
    v-model="visible"
    title="医生详情"
    width="600px"
    @close="handleClose"
  >
    <div class="doctor-detail" v-if="doctorData">
      <!-- 基本信息 -->
      <div class="detail-section">
        <h4>基本信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="工号">
            {{ doctorData.employeeNumber }}
          </el-descriptions-item>
          <el-descriptions-item label="姓名">
            {{ doctorData.name }}
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            {{ doctorData.gender }}
          </el-descriptions-item>
          <el-descriptions-item label="年龄">
            {{ doctorData.age }}岁
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ doctorData.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ doctorData.email }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 职业信息 -->
      <div class="detail-section">
        <h4>职业信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="科室">
            {{ doctorData.department }}
          </el-descriptions-item>
          <el-descriptions-item label="职称">
            {{ doctorData.title }}
          </el-descriptions-item>
          <el-descriptions-item label="学历">
            {{ doctorData.education }}
          </el-descriptions-item>
          <el-descriptions-item label="工作年限">
            {{ doctorData.workYears }}年
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="doctorData.status === 1 ? 'success' : 'danger'">
              {{ doctorData.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ doctorData.createTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 专业信息 -->
      <div class="detail-section">
        <h4>专业信息</h4>
        <div class="content-box">
          <p><strong>专业特长：</strong></p>
          <p class="content-text">{{ doctorData.specialization || '暂无' }}</p>
        </div>
      </div>

      <!-- 个人简介 -->
      <div class="detail-section" v-if="doctorData.introduction">
        <h4>个人简介</h4>
        <div class="content-box">
          <p class="content-text">{{ doctorData.introduction }}</p>
        </div>
      </div>

      <!-- 联系信息 -->
      <div class="detail-section" v-if="doctorData.address">
        <h4>联系信息</h4>
        <div class="content-box">
          <p><strong>地址：</strong>{{ doctorData.address }}</p>
        </div>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button type="primary" @click="handleEdit">编辑</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  doctorData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:visible', 'edit'])

const handleClose = () => {
  emit('update:visible', false)
}

const handleEdit = () => {
  emit('edit', props.doctorData)
  handleClose()
}
</script>

<style scoped>
.doctor-detail {
  padding: 20px 0;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h4 {
  margin-bottom: 15px;
  color: #333;
  font-weight: bold;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.content-box {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.content-text {
  line-height: 1.6;
  color: #606266;
  margin: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
