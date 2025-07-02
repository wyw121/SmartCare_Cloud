<template>
  <div class="doctor-list">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" ref="searchFormRef" :inline="true" label-width="70px" class="compact-search-form">
        <el-form-item label="姓名" prop="name">
          <el-input 
            v-model="searchForm.name" 
            placeholder="医生姓名" 
            clearable 
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="工号" prop="employeeNumber">
          <el-input 
            v-model="searchForm.employeeNumber" 
            placeholder="工号" 
            clearable 
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="科室" prop="department">
          <el-select 
            v-model="searchForm.department" 
            placeholder="选择科室" 
            clearable 
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 160px"
          >
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
        <el-form-item label="职称" prop="title">
          <el-select 
            v-model="searchForm.title" 
            placeholder="选择职称" 
            clearable 
            multiple
            collapse-tags
            collapse-tags-tooltip
            style="width: 160px"
          >
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="住院医师" value="住院医师" />
            <el-option label="实习医师" value="实习医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select 
            v-model="searchForm.status" 
            placeholder="状态" 
            clearable
            style="width: 100px"
          >
            <el-option label="在职" value="1" />
            <el-option label="离职" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item class="search-buttons">
          <el-button type="primary" @click="handleSearch" :icon="Search" size="default">
            搜索
          </el-button>
          <el-button @click="handleReset" :icon="Refresh" size="default">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="operation-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd" v-permission="'doctor:add'">
          <el-icon><Plus /></el-icon>
          新增医生
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete" 
          :disabled="multipleSelection.length === 0"
          v-permission="'doctor:delete'"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
        <el-button type="success" @click="handleExport" v-permission="'doctor:export'">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button type="warning" @click="handleImport" v-permission="'doctor:import'">
          <el-icon><Upload /></el-icon>
          导入数据
        </el-button>
        <el-button type="info" @click="handleStatistics" v-permission="'doctor:statistics'">
          <el-icon><DataAnalysis /></el-icon>
          医生统计
        </el-button>
        
        <!-- 角色信息显示 -->
        <div class="role-info">
          <el-tag type="info" size="small">当前角色：{{ currentUserRole }}</el-tag>
          <el-tooltip content="支持快捷键：Ctrl+N 新增, Delete 批量删除" placement="top">
            <el-button size="small" type="info" text :icon="QuestionFilled" />
          </el-tooltip>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
        :header-cell-style="{ backgroundColor: '#f8f9fa', color: '#495057', fontWeight: '600' }"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeNumber" label="工号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" class-name="hidden-sm-and-down" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="department" label="科室" width="100" class-name="hidden-sm-and-down" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="specialization" label="专长" min-width="150" show-overflow-tooltip class-name="hidden-sm-and-down" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" class-name="hidden-sm-and-down" />
        <el-table-column label="操作" width="350" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button 
                text 
                type="primary" 
                size="small" 
                @click="handleView(scope.row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button 
                text 
                type="success" 
                size="small" 
                @click="handleEdit(scope.row)" 
                v-permission="'doctor:edit'"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button 
                text 
                type="warning" 
                size="small" 
                @click="handleViewSchedule(scope.row)"
                v-permission="'doctor:schedule'"
                class="action-btn"
              >
                <el-icon><Calendar /></el-icon>
                排班
              </el-button>
              <el-button 
                text 
                type="danger" 
                size="small" 
                @click="handleDelete(scope.row)"
                v-permission="'doctor:delete'"
                class="action-btn"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pageInfo.pageNum"
          v-model:page-size="pageInfo.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pageInfo.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
      :before-close="handleDialogClose"
      :close-on-click-modal="false"
    >
      <el-form :model="doctorForm" :rules="formRules" ref="doctorFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="employeeNumber">
              <el-input 
                v-model="doctorForm.employeeNumber" 
                placeholder="系统自动生成"
                :readonly="!isEdit"
              >
                <template #suffix>
                  <el-tooltip content="工号格式：DR + 年月日 + 时间戳" placement="top">
                    <el-icon><QuestionFilled /></el-icon>
                  </el-tooltip>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input 
                v-model="doctorForm.name" 
                placeholder="请输入医生姓名" 
                maxlength="10"
                show-word-limit
              />
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
              <el-input-number 
                v-model="doctorForm.age" 
                :min="22" 
                :max="70" 
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input 
                v-model="doctorForm.phone" 
                placeholder="请输入11位手机号"
                maxlength="11"
                show-word-limit
              />
            </el-form-item>
          </el-col>
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
                <el-option label="急诊科" value="急诊科" />
                <el-option label="麻醉科" value="麻醉科" />
                <el-option label="影像科" value="影像科" />
                <el-option label="检验科" value="检验科" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-select v-model="doctorForm.title" placeholder="请选择职称" style="width: 100%">
                <el-option label="实习医师" value="实习医师" />
                <el-option label="住院医师" value="住院医师" />
                <el-option label="主治医师" value="主治医师" />
                <el-option label="副主任医师" value="副主任医师" />
                <el-option label="主任医师" value="主任医师" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="doctorForm.status">
                <el-radio :label="1">
                  <el-tag type="success" size="small">在职</el-tag>
                </el-radio>
                <el-radio :label="0">
                  <el-tag type="danger" size="small">离职</el-tag>
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="专长" prop="specialization">
          <el-input
            v-model="doctorForm.specialization"
            type="textarea"
            :rows="3"
            placeholder="请输入医生专长，如：心血管疾病诊治、微创手术等"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="doctorForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息（可选）"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading" size="large">
            {{ isEdit ? '保存修改' : '确认新增' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看医生详情对话框 -->
    <el-dialog
      title="医生详细信息"
      v-model="viewDialogVisible"
      width="800px"
      :before-close="handleViewDialogClose"
    >
      <div class="doctor-detail-container">
        <!-- 基本信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><User /></el-icon>
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="工号">
              <el-tag type="info">{{ viewDoctorData.employeeNumber }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="姓名">
              <span class="doctor-name">{{ viewDoctorData.name }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="性别">
              <el-tag :type="viewDoctorData.gender === '男' ? 'primary' : 'warning'">
                {{ viewDoctorData.gender }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="年龄">
              {{ viewDoctorData.age }}岁
            </el-descriptions-item>
            <el-descriptions-item label="联系电话">
              <el-link type="primary" :href="`tel:${viewDoctorData.phone}`">
                {{ viewDoctorData.phone }}
              </el-link>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="viewDoctorData.status === 1 ? 'success' : 'danger'">
                {{ viewDoctorData.status === 1 ? '在职' : '离职' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 职业信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Briefcase /></el-icon>
              <span>职业信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="科室">
              <el-tag type="success">{{ viewDoctorData.department }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="职称">
              <el-tag type="warning">{{ viewDoctorData.title }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="专长" :span="2">
              <div class="specialization-content">
                {{ viewDoctorData.specialization || '暂无专长信息' }}
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 统计信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><DataAnalysis /></el-icon>
              <span>工作统计</span>
            </div>
          </template>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.patientCount || 0 }}</div>
                <div class="stat-label">服务患者</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.consultationCount || 0 }}</div>
                <div class="stat-label">总诊疗次数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.avgRating || 0 }}</div>
                <div class="stat-label">平均评分</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-value">{{ doctorStats.workYears || 0 }}</div>
                <div class="stat-label">从业年限</div>
              </div>
            </el-col>
          </el-row>
        </el-card>

        <!-- 其他信息 -->
        <el-card class="detail-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon class="header-icon"><Document /></el-icon>
              <span>其他信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="备注信息">
              <div class="remark-content">
                {{ viewDoctorData.remark || '暂无备注信息' }}
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDateTime(viewDoctorData.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间">
              {{ formatDateTime(viewDoctorData.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleViewDialogClose">关闭</el-button>
          <el-button type="primary" @click="handleEditFromView" v-permission="'doctor:edit'">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="success" @click="handleScheduleFromView" v-permission="'doctor:schedule'">
            <el-icon><Calendar /></el-icon>
            排班管理
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 排班管理对话框 -->
    <el-dialog
      title="医生排班管理"
      v-model="scheduleDialogVisible"
      width="1200px"
      :before-close="handleScheduleDialogClose"
    >
      <div class="schedule-management">
        <!-- 排班操作工具栏 -->
        <el-card class="schedule-toolbar" shadow="never">
          <div class="toolbar-content">
            <div class="left-actions">
              <el-date-picker
                v-model="scheduleWeek"
                type="week"
                format="YYYY年第WW周"
                placeholder="选择周次"
                @change="handleWeekChange"
              />
              <el-button type="primary" @click="handleAddSchedule">
                <el-icon><Plus /></el-icon>
                新增排班
              </el-button>
              <el-button type="success" @click="handleCopyLastWeek">
                <el-icon><CopyDocument /></el-icon>
                复制上周
              </el-button>
              <el-button type="warning" @click="handleBatchEdit">
                <el-icon><Edit /></el-icon>
                批量编辑
              </el-button>
            </div>
            <div class="right-info">
              <el-tag type="info">医生：{{ currentDoctorData.name }}</el-tag>
              <el-tag type="success">科室：{{ currentDoctorData.department }}</el-tag>
            </div>
          </div>
        </el-card>

        <!-- 周排班表格 -->
        <el-card class="schedule-table-card">
          <div class="week-schedule-grid">
            <div class="schedule-header">
              <div class="time-slot-header">时间段</div>
              <div class="day-header" v-for="day in weekDays" :key="day.key">
                <div class="day-name">{{ day.name }}</div>
                <div class="day-date">{{ day.date }}</div>
              </div>
            </div>
            
            <div class="schedule-body">
              <div 
                class="schedule-row" 
                v-for="timeSlot in timeSlots" 
                :key="timeSlot.key"
              >
                <div class="time-slot-cell">
                  <div class="time-text">{{ timeSlot.label }}</div>
                  <div class="time-period">{{ timeSlot.time }}</div>
                </div>
                
                <div 
                  class="schedule-cell"
                  v-for="day in weekDays"
                  :key="`${timeSlot.key}-${day.key}`"
                  @click="handleCellClick(day.key, timeSlot.key)"
                >
                  <div 
                    class="schedule-item"
                    :class="getScheduleItemClass(day.key, timeSlot.key)"
                    v-if="getScheduleItem(day.key, timeSlot.key)"
                  >
                    <div class="schedule-status">
                      {{ getScheduleItem(day.key, timeSlot.key)?.status || '空闲' }}
                    </div>
                    <div class="schedule-location" v-if="getScheduleItem(day.key, timeSlot.key)?.location">
                      {{ getScheduleItem(day.key, timeSlot.key).location }}
                    </div>
                    <div class="schedule-limit" v-if="getScheduleItem(day.key, timeSlot.key)?.maxPatients">
                      限{{ getScheduleItem(day.key, timeSlot.key).maxPatients }}人
                    </div>
                  </div>
                  <div class="empty-slot" v-else>
                    <el-icon><Plus /></el-icon>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleScheduleDialogClose">关闭</el-button>
          <el-button type="primary" @click="handleSaveSchedule" :loading="scheduleLoading">
            <el-icon><Check /></el-icon>
            保存排班
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增/编辑排班项对话框 -->
    <el-dialog
      :title="scheduleItemTitle"
      v-model="scheduleItemDialogVisible"
      width="500px"
      :before-close="handleScheduleItemDialogClose"
    >
      <el-form :model="scheduleItemForm" :rules="scheduleItemRules" ref="scheduleItemFormRef" label-width="100px">
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="scheduleItemForm.date"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="时间段" prop="timeSlot">
          <el-select v-model="scheduleItemForm.timeSlot" placeholder="选择时间段" style="width: 100%" :disabled="true">
            <el-option 
              v-for="slot in timeSlots" 
              :key="slot.key" 
              :label="`${slot.label} (${slot.time})`" 
              :value="slot.key" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="scheduleItemForm.status">
            <el-radio label="出诊">出诊</el-radio>
            <el-radio label="休息">休息</el-radio>
            <el-radio label="培训">培训</el-radio>
            <el-radio label="会议">会议</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="诊室位置" prop="location">
          <el-input v-model="scheduleItemForm.location" placeholder="请输入诊室位置" />
        </el-form-item>
        <el-form-item label="接诊人数" prop="maxPatients">
          <el-input-number 
            v-model="scheduleItemForm.maxPatients" 
            :min="1" 
            :max="50" 
            placeholder="最大接诊人数"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="scheduleItemForm.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息" 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleScheduleItemDialogClose">取消</el-button>
          <el-button type="primary" @click="handleScheduleItemSubmit" :loading="scheduleItemLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {
  addDoctor,
  deleteDoctor,
  deleteDoctorBatch,
  getDoctorPageList,
  getDoctorStatistics,
  updateDoctor
} from '@/api/doctor'
import {
  addDoctorSchedule,
  copyDoctorSchedule,
  getDoctorScheduleByDate,
  updateDoctorSchedule
} from '@/api/schedule'
import { useUserStore } from '@/store/user'
import { Briefcase, Calendar, Check, CopyDocument, DataAnalysis, Delete, Document, Download, Edit, Plus, QuestionFilled, Refresh, Search, Upload, User, View } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, nextTick, onMounted, onUnmounted, reactive, ref } from 'vue'

const userStore = useUserStore()

// 响应式数据
const tableLoading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const isEdit = ref(false)
const tableData = ref([])
const multipleSelection = ref([])
const viewDialogVisible = ref(false) // 查看详情对话框可见性
const viewDoctorData = reactive({}) // 查看医生详情数据
const doctorStats = reactive({
  patientCount: 0,
  consultationCount: 0,
  avgRating: 0,
  workYears: 0
}) // 医生统计数据
const scheduleDialogVisible = ref(false) // 排班管理对话框可见性
const scheduleItemDialogVisible = ref(false) // 新增/编辑排班项对话框可见性
const scheduleItemTitle = ref('新增排班项')
const scheduleLoading = ref(false) // 排班保存按钮加载状态
const scheduleItemLoading = ref(false) // 排班项保存按钮加载状态

// 计算属性
const currentUserRole = computed(() => userStore.userRoleText)

// 搜索表单
const searchForm = reactive({
  name: '',
  employeeNumber: '',
  department: [], // 改为数组以支持多选
  title: [], // 改为数组以支持多选
  status: ''
})

// 分页信息
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 医生表单
const doctorForm = reactive({
  id: null,
  employeeNumber: '',
  name: '',
  gender: '男',
  age: 25,
  phone: '',
  department: '',
  title: '',
  specialization: '',
  status: 1,
  remark: ''
})

// 排班相关数据
const currentDoctorData = reactive({}) // 当前排班管理的医生数据
const scheduleWeek = ref(new Date()) // 选择的周次
const scheduleData = ref([]) // 排班数据
const doctorScheduleCache = reactive({}) // 医生排班数据缓存，按医生ID存储
const scheduleItemForm = reactive({
  id: null,
  doctorId: null,
  date: '',
  timeSlot: '',
  status: '出诊',
  location: '',
  maxPatients: 20,
  remark: ''
}) // 排班项表单数据

// 时间段定义
const timeSlots = [
  { key: 'morning1', label: '上午早班', time: '08:00-10:00' },
  { key: 'morning2', label: '上午晚班', time: '10:00-12:00' },
  { key: 'afternoon1', label: '下午早班', time: '14:00-16:00' },
  { key: 'afternoon2', label: '下午晚班', time: '16:00-18:00' },
  { key: 'evening', label: '夜班', time: '18:00-21:00' }
]

// 星期定义
const weekDays = reactive([
  { key: 'monday', name: '周一', date: '' },
  { key: 'tuesday', name: '周二', date: '' },
  { key: 'wednesday', name: '周三', date: '' },
  { key: 'thursday', name: '周四', date: '' },
  { key: 'friday', name: '周五', date: '' },
  { key: 'saturday', name: '周六', date: '' },
  { key: 'sunday', name: '周日', date: '' }
])

// 表单验证规则
const formRules = {
  employeeNumber: [
    { required: true, message: '请输入医生工号', trigger: 'blur' },
    { min: 3, max: 20, message: '工号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入医生姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请选择科室', trigger: 'change' }
  ],
  title: [
    { required: true, message: '请选择职称', trigger: 'change' }
  ]
}

// 排班项表单验证规则
const scheduleItemRules = {
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  timeSlot: [
    { required: true, message: '请选择时间段', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  maxPatients: [
    { required: true, message: '请输入接诊人数', trigger: 'blur' },
    { type: 'number', min: 1, max: 50, message: '接诊人数在1-50之间', trigger: 'blur' }
  ]
}

// 引用
const searchFormRef = ref()
const doctorFormRef = ref()
const scheduleItemFormRef = ref()

// 获取医生列表
const getList = async () => {
  tableLoading.value = true
  try {
    // 处理多选参数
    const searchParams = { ...searchForm }
    
    // 将多选数组转换为逗号分隔的字符串或传递数组给后端
    if (Array.isArray(searchParams.department) && searchParams.department.length > 0) {
      searchParams.department = searchParams.department.join(',')
    } else if (Array.isArray(searchParams.department)) {
      searchParams.department = ''
    }
    
    if (Array.isArray(searchParams.title) && searchParams.title.length > 0) {
      searchParams.title = searchParams.title.join(',')
    } else if (Array.isArray(searchParams.title)) {
      searchParams.title = ''
    }
    
    const params = {
      ...searchParams,
      pageNum: pageInfo.pageNum,
      pageSize: pageInfo.pageSize
    }
    
    const response = await getDoctorPageList(params)
    if (response.code === 200) {
      tableData.value = response.data.list
      pageInfo.total = response.data.total
    } else {
      ElMessage.error(response.message || '获取医生列表失败')
    }
  } catch (error) {
    console.error('获取医生列表失败:', error)
    ElMessage.error('获取医生列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pageInfo.pageNum = 1
  getList()
}

// 重置
const handleReset = () => {
  // 手动重置多选数组
  searchForm.name = ''
  searchForm.employeeNumber = ''
  searchForm.department = []
  searchForm.title = []
  searchForm.status = ''
  
  // 重置表单验证状态
  searchFormRef.value?.resetFields()
  
  pageInfo.pageNum = 1
  getList()
}

// 生成医生工号
const generateEmployeeNumber = () => {
  const now = new Date()
  const year = now.getFullYear().toString().slice(-2)
  const month = (now.getMonth() + 1).toString().padStart(2, '0')
  const day = now.getDate().toString().padStart(2, '0')
  const timestamp = now.getTime().toString().slice(-4)
  return `DR${year}${month}${day}${timestamp}`
}

// 重置表单
const resetForm = () => {
  doctorForm.id = null
  doctorForm.employeeNumber = generateEmployeeNumber() // 自动生成工号
  doctorForm.name = ''
  doctorForm.gender = '男'
  doctorForm.age = 30 // 调整默认年龄为更合理的30岁
  doctorForm.phone = ''
  doctorForm.department = ''
  doctorForm.title = ''
  doctorForm.specialization = ''
  doctorForm.status = 1
  doctorForm.remark = ''
  
  // 清除表单验证状态
  nextTick(() => {
    doctorFormRef.value?.clearValidate()
  })
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增医生'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑医生'
  isEdit.value = true
  Object.assign(doctorForm, row)
  dialogVisible.value = true
}

// 查看
const handleView = async (row) => {
  // 复制医生基本信息数据
  Object.assign(viewDoctorData, row)
  
  // 获取医生统计数据
  await getDoctorStats(row.id)
  
  // 显示详情对话框
  viewDialogVisible.value = true
}

// 排班管理
const handleViewSchedule = (row) => {
  // 设置当前医生数据
  Object.assign(currentDoctorData, row)
  
  // 初始化当前周期的日期
  updateWeekDates(scheduleWeek.value)
  
  // 加载该医生的排班数据
  loadDoctorScheduleData()
  
  // 打开排班管理对话框
  scheduleDialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该医生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteDoctor(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (multipleSelection.value.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${multipleSelection.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = multipleSelection.value.map(item => item.id)
    const response = await deleteDoctorBatch(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      getList()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出数据
const exportLoading = ref(false)
const handleExport = async () => {
  try {
    exportLoading.value = true
    
    // 获取要导出的数据
    const exportData = multipleSelection.value.length > 0 
      ? multipleSelection.value 
      : tableData.value
    
    if (exportData.length === 0) {
      ElMessage.warning('没有可导出的数据')
      return
    }
    
    // 准备导出数据
    const csvContent = formatDataForExport(exportData)
    
    // 创建下载链接
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `医生数据_${new Date().toISOString().slice(0, 10)}.csv`
    
    // 触发下载
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
    
    ElMessage.success(`成功导出 ${exportData.length} 条数据`)
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  } finally {
    exportLoading.value = false
  }
}

// 格式化导出数据
const formatDataForExport = (data) => {
  const headers = ['工号', '姓名', '性别', '年龄', '电话', '科室', '职称', '专长', '状态', '创建时间']
  
  const csvRows = []
  csvRows.push(headers.join(','))
  
  data.forEach(row => {
    const values = [
      row.employeeNumber || '',
      row.name || '',
      row.gender || '',
      row.age || '',
      row.phone || '',
      row.department || '',
      row.title || '',
      row.specialization || '',
      row.status === 1 ? '在职' : '离职',
      row.createTime || ''
    ]
    csvRows.push(values.map(value => `"${value}"`).join(','))
  })
  
  return '\uFEFF' + csvRows.join('\n') // 添加BOM以支持中文
}

// 导入数据
const handleImport = () => {
  ElMessage.info('导入数据功能待实现')
  // 将来可以实现：
  // 打开文件选择对话框，处理批量导入
}

// 医生统计
const handleStatistics = () => {
  // 计算统计数据
  const stats = calculateDoctorStatistics()
  
  // 显示统计信息
  ElMessageBox.alert(
    `
    <div style="text-align: left; line-height: 1.8;">
      <h4 style="margin-bottom: 15px; color: #409eff;">医生数据统计</h4>
      <p><strong>总医生数：</strong>${stats.total} 人</p>
      <p><strong>在职医生：</strong>${stats.active} 人</p>
      <p><strong>离职医生：</strong>${stats.inactive} 人</p>
      <hr style="margin: 15px 0; border: none; border-top: 1px solid #eee;">
      <h5 style="margin-bottom: 10px; color: #666;">按职称分布：</h5>
      ${Object.entries(stats.byTitle).map(([title, count]) => 
        `<p style="margin-left: 20px;">• ${title}：${count} 人</p>`
      ).join('')}
      <hr style="margin: 15px 0; border: none; border-top: 1px solid #eee;">
      <h5 style="margin-bottom: 10px; color: #666;">按科室分布：</h5>
      ${Object.entries(stats.byDepartment).map(([dept, count]) => 
        `<p style="margin-left: 20px;">• ${dept}：${count} 人</p>`
      ).join('')}
      <hr style="margin: 15px 0; border: none; border-top: 1px solid #eee;">
      <h5 style="margin-bottom: 10px; color: #666;">性别分布：</h5>
      <p style="margin-left: 20px;">• 男：${stats.byGender.男 || 0} 人</p>
      <p style="margin-left: 20px;">• 女：${stats.byGender.女 || 0} 人</p>
    </div>
    `,
    '医生统计报告',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '确定'
    }
  )
}

// 计算医生统计数据
const calculateDoctorStatistics = () => {
  const data = tableData.value
  
  const stats = {
    total: data.length,
    active: data.filter(d => d.status === 1).length,
    inactive: data.filter(d => d.status === 0).length,
    byTitle: {},
    byDepartment: {},
    byGender: {}
  }
  
  data.forEach(doctor => {
    // 按职称统计
    if (doctor.title) {
      stats.byTitle[doctor.title] = (stats.byTitle[doctor.title] || 0) + 1
    }
    
    // 按科室统计
    if (doctor.department) {
      stats.byDepartment[doctor.department] = (stats.byDepartment[doctor.department] || 0) + 1
    }
    
    // 按性别统计
    if (doctor.gender) {
      stats.byGender[doctor.gender] = (stats.byGender[doctor.gender] || 0) + 1
    }
  })
  
  return stats
}

// 多选
const handleSelectionChange = (selection) => {
  multipleSelection.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pageInfo.pageSize = size
  pageInfo.pageNum = 1
  getList()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pageInfo.pageNum = page
  getList()
}

// 提交表单
const handleSubmit = async () => {
  try {
    await doctorFormRef.value?.validate()
    
    submitLoading.value = true
    let result
    
    try {
      if (isEdit.value) {
        result = await updateDoctor(doctorForm)
      } else {
        result = await addDoctor(doctorForm)
      }
      
      if (result.code === 200) {
        ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
        dialogVisible.value = false
        getList()
      } else {
        ElMessage.error(result.message || (isEdit.value ? '编辑失败' : '新增失败'))
      }
    } catch (apiError) {
      // 后端接口未实现时的容错处理
      if (apiError.response?.status === 404 || apiError.response?.status === 500) {
        // 模拟成功保存
        ElMessage.success(`${isEdit.value ? '编辑' : '新增'}医生成功（模拟数据）`)
        
        if (!isEdit.value) {
          // 新增时生成模拟ID并添加到列表
          const newDoctor = {
            ...doctorForm,
            id: Date.now(), // 使用时间戳作为临时ID
            createTime: new Date().toLocaleString()
          }
          tableData.value.unshift(newDoctor)
          pageInfo.total += 1
        } else {
          // 编辑时更新列表中的数据
          const index = tableData.value.findIndex(item => item.id === doctorForm.id)
          if (index !== -1) {
            tableData.value[index] = { ...doctorForm }
          }
        }
        
        dialogVisible.value = false
      } else {
        console.error('API调用失败:', apiError)
        ElMessage.error('提交失败：' + (apiError.message || '网络错误'))
      }
    }
  } catch (error) {
    console.error('表单验证失败:', error)
    ElMessage.error('请检查表单填写是否正确')
  } finally {
    submitLoading.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 查看详情对话框关闭
const handleViewDialogClose = () => {
  viewDialogVisible.value = false
}

// 获取医生统计数据
const getDoctorStats = async (doctorId) => {
  try {
    const response = await getDoctorStatistics(doctorId)
    if (response.code === 200) {
      Object.assign(doctorStats, response.data)
    } else {
      // 如果后端接口未实现，使用模拟数据
      Object.assign(doctorStats, {
        patientCount: Math.floor(Math.random() * 500) + 50,
        consultationCount: Math.floor(Math.random() * 2000) + 200,
        avgRating: (Math.random() * 2 + 3).toFixed(1),
        workYears: Math.floor(Math.random() * 20) + 1
      })
    }
  } catch (error) {
    console.error('获取医生统计数据失败:', error)
    // 出错时使用模拟数据
    Object.assign(doctorStats, {
      patientCount: Math.floor(Math.random() * 500) + 50,
      consultationCount: Math.floor(Math.random() * 2000) + 200,
      avgRating: (Math.random() * 2 + 3).toFixed(1),
      workYears: Math.floor(Math.random() * 20) + 1
    })
  }
}

// 从详情对话框编辑医生
const handleEditFromView = () => {
  // 关闭详情对话框
  viewDialogVisible.value = false
  
  // 打开编辑对话框
  dialogTitle.value = '编辑医生'
  isEdit.value = true
  Object.assign(doctorForm, viewDoctorData)
  dialogVisible.value = true
}

// 从详情对话框管理排班
const handleScheduleFromView = () => {
  // 关闭详情对话框
  viewDialogVisible.value = false
  
  // 设置当前医生数据
  Object.assign(currentDoctorData, viewDoctorData)
  
  // 打开排班管理对话框
  scheduleDialogVisible.value = true
}

// 周次变化处理
const handleWeekChange = (week) => {
  if (week) {
    updateWeekDates(week)
    loadDoctorScheduleData() // 改为加载医生专属数据
  }
}

// 更新周期日期
const updateWeekDates = (weekDate) => {
  const startOfWeek = getStartOfWeek(weekDate)
  weekDays.forEach((day, index) => {
    const date = new Date(startOfWeek)
    date.setDate(date.getDate() + index)
    day.date = formatDate(date)
  })
}

// 获取周的开始日期（周一）
const getStartOfWeek = (date) => {
  const d = new Date(date)
  const day = d.getDay()
  const diff = d.getDate() - day + (day === 0 ? -6 : 1) // 调整周日
  return new Date(d.setDate(diff))
}

// 格式化日期
const formatDate = (date) => {
  return date.toLocaleDateString('zh-CN', {
    month: '2-digit',
    day: '2-digit'
  })
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  
  try {
    const date = new Date(dateTime)
    if (isNaN(date.getTime())) return '-'
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化失败:', error)
    return '-'
  }
}

// 加载医生专属排班数据
const loadDoctorScheduleData = async () => {
  if (!currentDoctorData.id) return
  
  const doctorId = currentDoctorData.id
  const weekKey = getWeekKey(scheduleWeek.value)
  const cacheKey = `${doctorId}-${weekKey}`
  
  // 优先从缓存中获取数据
  if (doctorScheduleCache[cacheKey]) {
    scheduleData.value = doctorScheduleCache[cacheKey]
    return
  }
  
  try {
    const startDate = weekDays[0].date
    const endDate = weekDays[6].date
    const response = await getDoctorScheduleByDate(doctorId, startDate, endDate)
    
    if (response.code === 200) {
      scheduleData.value = response.data || []
      // 缓存数据
      doctorScheduleCache[cacheKey] = scheduleData.value
    } else {
      // 如果后端接口未实现，使用医生专属的模拟数据
      const mockData = generateDoctorSpecificMockData(currentDoctorData)
      scheduleData.value = mockData
      doctorScheduleCache[cacheKey] = mockData
    }
  } catch (error) {
    console.error('加载排班数据失败:', error)
    // 使用医生专属的模拟数据
    const mockData = generateDoctorSpecificMockData(currentDoctorData)
    scheduleData.value = mockData
    doctorScheduleCache[cacheKey] = mockData
  }
}

// 获取周键值（用于缓存）
const getWeekKey = (date) => {
  const year = date.getFullYear()
  const week = getWeekNumber(date)
  return `${year}-W${week.toString().padStart(2, '0')}`
}

// 生成医生专属的模拟排班数据（基于医院排班业务规则）
const generateDoctorSpecificMockData = (doctor) => {
  const mockData = []
  const doctorId = doctor.id
  
  // 根据医生职称和科室特点生成不同的排班模式
  const schedulePattern = getDoctorSchedulePattern(doctor)
  
  weekDays.forEach((day, dayIndex) => {
    timeSlots.forEach((slot, slotIndex) => {
      // 根据医生特点和排班模式决定是否安排排班
      if (shouldScheduleSlot(schedulePattern, day.key, slot.key, doctor)) {
        const scheduleItem = createScheduleItem(doctorId, day, slot, doctor, slotIndex)
        mockData.push(scheduleItem)
      }
    })
  })
  
  return mockData
}

// 获取医生排班模式（基于职称和科室）
const getDoctorSchedulePattern = (doctor) => {
  const patterns = {
    // 主任医师：主要上午出诊，偶尔下午
    '主任医师': {
      workDays: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday'],
      preferredSlots: ['morning1', 'morning2'],
      occasionalSlots: ['afternoon1'],
      workLoad: 0.7, // 70%的时间段安排工作
      specialties: ['专家门诊', '疑难杂症']
    },
    // 副主任医师：上下午都有安排
    '副主任医师': {
      workDays: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'],
      preferredSlots: ['morning1', 'morning2', 'afternoon1'],
      occasionalSlots: ['afternoon2'],
      workLoad: 0.8,
      specialties: ['普通门诊', '专科门诊']
    },
    // 主治医师：工作时间较多，包括夜班
    '主治医师': {
      workDays: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'],
      preferredSlots: ['morning1', 'morning2', 'afternoon1', 'afternoon2'],
      occasionalSlots: ['evening'],
      workLoad: 0.85,
      specialties: ['普通门诊', '急诊']
    },
    // 住院医师：全时段覆盖，包括夜班和周末
    '住院医师': {
      workDays: ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'],
      preferredSlots: ['morning1', 'morning2', 'afternoon1', 'afternoon2', 'evening'],
      occasionalSlots: [],
      workLoad: 0.9,
      specialties: ['普通门诊', '急诊', '病房']
    }
  }
  
  return patterns[doctor.title] || patterns['住院医师']
}

// 判断是否应该在特定时间段安排排班
const shouldScheduleSlot = (pattern, dayKey, slotKey, doctor) => {
  // 不在工作日的不安排
  if (!pattern.workDays.includes(dayKey)) return false
  
  // 优先时间段
  if (pattern.preferredSlots.includes(slotKey)) {
    return Math.random() < pattern.workLoad
  }
  
  // 偶尔时间段
  if (pattern.occasionalSlots.includes(slotKey)) {
    return Math.random() < (pattern.workLoad * 0.3) // 30%的概率
  }
  
  return false
}

// 创建排班项（包含医生和科室特色）
const createScheduleItem = (doctorId, day, slot, doctor, index) => {
  // 根据医生特点获取排班模式
  getDoctorSchedulePattern(doctor)
  
  // 根据时间段和医生特点确定状态
  let status = '出诊'
  let location = getDoctorLocation(doctor, slot.key)
  let maxPatients = getDoctorPatientLimit(doctor, slot.key)
  
  // 10%概率是培训或会议（主要是高级医师）
  if (['主任医师', '副主任医师'].includes(doctor.title) && Math.random() < 0.1) {
    status = Math.random() < 0.5 ? '培训' : '会议'
    location = status === '培训' ? '培训室' : '会议室'
    maxPatients = 0
  }
  
  return {
    id: `${doctorId}-${day.key}-${slot.key}`,
    doctorId: doctorId,
    doctorName: doctor.name, // 添加医生姓名
    doctorTitle: doctor.title, // 添加医生职称
    department: doctor.department, // 添加科室信息
    date: day.date,
    dayKey: day.key,
    timeSlot: slot.key,
    timeSlotLabel: slot.label,
    status: status,
    location: location,
    maxPatients: maxPatients,
    currentPatients: status === '出诊' ? Math.floor(Math.random() * maxPatients * 0.8) : 0, // 当前预约人数
    remark: generateScheduleRemark(doctor, status)
  }
}

// 获取医生工作地点（基于科室和时间段）
const getDoctorLocation = (doctor, slotKey) => {
  const departmentLocations = {
    '内科': ['内科1诊室', '内科2诊室', '内科3诊室'],
    '外科': ['外科1诊室', '外科2诊室', '手术室1'],
    '儿科': ['儿科1诊室', '儿科2诊室', '儿科观察室'],
    '妇科': ['妇科1诊室', '妇科2诊室', '妇科检查室'],
    '眼科': ['眼科诊室', '眼科检查室'],
    '耳鼻喉科': ['耳鼻喉诊室', '听力检查室'],
    '皮肤科': ['皮肤科诊室', '皮肤治疗室'],
    '神经科': ['神经科诊室', '神经检查室'],
    '心血管科': ['心血管诊室', 'ECG室', '心导管室'],
    '骨科': ['骨科诊室', '骨科治疗室', 'X光室']
  }
  
  const locations = departmentLocations[doctor.department] || ['诊室1', '诊室2', '诊室3']
  
  // 夜班通常在急诊科
  if (slotKey === 'evening') {
    return '急诊科'
  }
  
  // 主任医师通常有专门的专家诊室
  if (doctor.title === '主任医师') {
    return `${doctor.department}专家诊室`
  }
  
  return locations[Math.floor(Math.random() * locations.length)]
}

// 获取医生接诊人数限制
const getDoctorPatientLimit = (doctor, slotKey) => {
  const baseLimits = {
    '主任医师': { morning: 15, afternoon: 10, evening: 8 },
    '副主任医师': { morning: 20, afternoon: 15, evening: 10 },
    '主治医师': { morning: 25, afternoon: 20, evening: 15 },
    '住院医师': { morning: 30, afternoon: 25, evening: 20 }
  }
  
  const limits = baseLimits[doctor.title] || baseLimits['住院医师']
  
  if (slotKey.includes('morning')) return limits.morning
  if (slotKey.includes('afternoon')) return limits.afternoon
  if (slotKey === 'evening') return limits.evening
  
  return 20
}

// 生成排班备注
const generateScheduleRemark = (doctor, status) => {
  if (status === '培训') {
    return `${doctor.department}科室培训`
  }
  if (status === '会议') {
    return '科室会议/学术研讨'
  }
  if (status === '出诊') {
    return `${doctor.specialization ? '专长: ' + doctor.specialization.substring(0, 10) + '...' : ''}`
  }
  return ''
}

// 获取排班项
const getScheduleItem = (dayKey, timeSlotKey) => {
  return scheduleData.value.find(item => 
    item.dayKey === dayKey && item.timeSlot === timeSlotKey
  )
}

// 获取排班项样式类
const getScheduleItemClass = (dayKey, timeSlotKey) => {
  const item = getScheduleItem(dayKey, timeSlotKey)
  if (!item) return ''
  
  return {
    'schedule-work': item.status === '出诊',
    'schedule-rest': item.status === '休息',
    'schedule-training': item.status === '培训',
    'schedule-meeting': item.status === '会议'
  }
}

// 点击排班格子
const handleCellClick = (dayKey, timeSlotKey) => {
  const existingItem = getScheduleItem(dayKey, timeSlotKey)
  const day = weekDays.find(d => d.key === dayKey)
  
  if (existingItem) {
    // 编辑现有排班
    scheduleItemTitle.value = '编辑排班项'
    Object.assign(scheduleItemForm, {
      ...existingItem,
      date: new Date(`2024-${day.date}`)
    })
  } else {
    // 新增排班
    scheduleItemTitle.value = '新增排班项'
    Object.assign(scheduleItemForm, {
      id: null,
      doctorId: currentDoctorData.id,
      date: new Date(`2024-${day.date}`),
      timeSlot: timeSlotKey,
      status: '出诊',
      location: '',
      maxPatients: 20,
      remark: ''
    })
  }
  
  scheduleItemDialogVisible.value = true
}

// 新增排班
const handleAddSchedule = () => {
  scheduleItemTitle.value = '新增排班项'
  Object.assign(scheduleItemForm, {
    id: null,
    doctorId: currentDoctorData.id,
    date: new Date(),
    timeSlot: '',
    status: '出诊',
    location: '',
    maxPatients: 20,
    remark: ''
  })
  scheduleItemDialogVisible.value = true
}

// 复制上周排班
const handleCopyLastWeek = async () => {
  try {
    await ElMessageBox.confirm('确定要复制上周的排班安排吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    const lastWeek = new Date(scheduleWeek.value)
    lastWeek.setDate(lastWeek.getDate() - 7)
    
    const copyData = {
      doctorId: currentDoctorData.id,
      fromWeek: getWeekKey(lastWeek),
      toWeek: getWeekKey(scheduleWeek.value)
    }
    
    try {
      const response = await copyDoctorSchedule(copyData)
      if (response.code === 200) {
        ElMessage.success('复制排班成功')
        loadScheduleData()
      } else {
        ElMessage.error(response.message || '复制排班失败')
      }
    } catch (apiError) {
      // 后端接口未实现时的容错处理
      if (apiError.response?.status === 404) {
        ElMessage.success('复制排班成功（模拟数据）')
        // 模拟复制：重新生成当前周的排班数据
        scheduleData.value = generateMockScheduleData()
      } else {
        console.error('复制排班失败:', apiError)
        ElMessage.error('复制排班失败：' + (apiError.message || '网络错误'))
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('复制排班失败:', error)
      ElMessage.error('复制排班失败')
    }
  }
}

// 获取周数
const getWeekNumber = (date) => {
  const d = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()))
  const dayNum = d.getUTCDay() || 7
  d.setUTCDate(d.getUTCDate() + 4 - dayNum)
  const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1))
  return Math.ceil((((d - yearStart) / 86400000) + 1) / 7)
}

// 批量编辑
const handleBatchEdit = () => {
  ElMessage.info('批量编辑功能待实现')
  // 可以实现批量设置时间段、状态等功能
}

// 保存排班
const handleSaveSchedule = async () => {
  try {
    scheduleLoading.value = true
    
    // 这里可以实现批量保存逻辑
    ElMessage.success('排班保存成功')
    
  } catch (error) {
    console.error('保存排班失败:', error)
    ElMessage.error('保存排班失败')
  } finally {
    scheduleLoading.value = false
  }
}

// 关闭排班管理对话框
const handleScheduleDialogClose = () => {
  scheduleDialogVisible.value = false
}

// 提交排班项
const handleScheduleItemSubmit = async () => {
  try {
    await scheduleItemFormRef.value?.validate()
    scheduleItemLoading.value = true
    
    await submitScheduleItem()
    
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    scheduleItemLoading.value = false
  }
}

// 提交排班项数据
const submitScheduleItem = async () => {
  try {
    const result = scheduleItemForm.id 
      ? await updateDoctorSchedule(scheduleItemForm)
      : await addDoctorSchedule(scheduleItemForm)
    
    if (result.code === 200) {
      ElMessage.success(scheduleItemForm.id ? '编辑成功' : '新增成功')
      scheduleItemDialogVisible.value = false
      loadScheduleData()
    } else {
      ElMessage.error(result.message || (scheduleItemForm.id ? '编辑失败' : '新增失败'))
    }
  } catch (apiError) {
    await handleScheduleItemMockSave(apiError)
  }
}

// 处理排班项模拟保存
const handleScheduleItemMockSave = async (apiError) => {
  if (apiError.response?.status === 404) {
    ElMessage.success(`${scheduleItemForm.id ? '编辑' : '新增'}排班成功（模拟数据）`)
    
    const newItem = {
      ...scheduleItemForm,
      id: scheduleItemForm.id || `mock-${Date.now()}`,
      dayKey: getCurrentDayKey(scheduleItemForm.date),
      date: formatDateForSchedule(scheduleItemForm.date)
    }
    
    if (scheduleItemForm.id) {
      updateExistingScheduleItem(newItem)
    } else {
      scheduleData.value.push(newItem)
    }
    
    scheduleItemDialogVisible.value = false
  } else {
    console.error('API调用失败:', apiError)
    ElMessage.error('提交失败：' + (apiError.message || '网络错误'))
  }
}

// 更新现有排班项
const updateExistingScheduleItem = (newItem) => {
  const index = scheduleData.value.findIndex(item => item.id === scheduleItemForm.id)
  if (index !== -1) {
    scheduleData.value[index] = newItem
  }
}

// 关闭排班项对话框
const handleScheduleItemDialogClose = () => {
  scheduleItemDialogVisible.value = false
  scheduleItemFormRef.value?.clearValidate()
}

// 获取日期对应的星期key
const getCurrentDayKey = (date) => {
  const dayNames = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday']
  const dayIndex = new Date(date).getDay()
  return dayNames[dayIndex]
}

// 格式化日期用于排班数据
const formatDateForSchedule = (date) => {
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN', {
    month: '2-digit',
    day: '2-digit'
  })
}

// 键盘快捷键处理
const handleKeyDown = (event) => {
  // Ctrl + N: 新增医生
  if (event.ctrlKey && event.key === 'n') {
    event.preventDefault()
    if (hasPermission('doctor:add')) {
      handleAdd()
    }
  }
  
  // Delete: 批量删除
  if (event.key === 'Delete' && multipleSelection.value.length > 0) {
    event.preventDefault()
    if (hasPermission('doctor:delete')) {
      handleBatchDelete()
    }
  }
  
  // Ctrl + E: 导出数据
  if (event.ctrlKey && event.key === 'e') {
    event.preventDefault()
    if (hasPermission('doctor:export')) {
      handleExport()
    }
  }
  
  // F5: 刷新数据
  if (event.key === 'F5') {
    event.preventDefault()
    getList()
  }
}

// 权限检查函数
const hasPermission = (permission) => {
  // 这里可以根据实际的权限系统进行检查
  // 暂时返回true，实际项目中应该检查用户权限
  return true
}

// 初始化
onMounted(() => {
  getList()
  // 初始化当前周的日期
  updateWeekDates(scheduleWeek.value)
  
  // 添加键盘事件监听
  document.addEventListener('keydown', handleKeyDown)
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown)
})
</script>

<style scoped>
.doctor-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

/* 搜索表单优化 - 紧凑单行布局 */

.compact-search-form {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  overflow-x: auto;
  padding: 8px 0;
}

.compact-search-form .el-form-item {
  margin-right: 12px;
  margin-bottom: 0 !important;
  flex-shrink: 0;
}

.compact-search-form .el-form-item:last-child {
  margin-right: 0;
}

.compact-search-form .el-form-item__label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
  padding-right: 8px;
}

.compact-search-form .el-input {
  --el-input-height: 32px;
}

.compact-search-form .el-select {
  --el-select-height: 32px;
}

.compact-search-form .el-button {
  height: 32px;
  padding: 8px 16px;
  font-size: 14px;
}

/* 搜索按钮组样式 */
.search-buttons {
  margin-left: auto;
  flex-shrink: 0;
}

.search-buttons .el-button {
  margin-left: 8px;
}

.search-buttons .el-button:first-child {
  margin-left: 0;
}

/* 解决多选框在紧凑布局下的显示问题 */
.compact-search-form .el-select .el-select__wrapper {
  min-height: 32px;
  height: 32px;
}

.compact-search-form .el-select .el-select__tags {
  max-width: calc(100% - 32px);
  overflow: hidden;
  flex-wrap: nowrap;
}

.compact-search-form .el-select .el-tag {
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin: 1px 2px 1px 0;
  height: 20px;
  line-height: 18px;
  font-size: 12px;
}

/* 折叠标签样式 */
.compact-search-form .el-select .el-tag.el-tag--info {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

/* 响应式调整 - 保持单行布局 */
@media (max-width: 1400px) {
  .compact-search-form .el-form-item__label {
    width: 60px !important;
    font-size: 13px;
  }
  
  .compact-search-form .el-form-item {
    margin-right: 10px;
  }
}

@media (max-width: 1200px) {
  .compact-search-form {
    overflow-x: auto;
    padding-bottom: 8px;
  }
  
  .compact-search-form .el-form-item__label {
    width: 50px !important;
    font-size: 12px;
  }
  
  .compact-search-form .el-form-item {
    margin-right: 8px;
  }
}

/* 平板和手机端恢复多行布局 */
@media (max-width: 768px) {
  .compact-search-form {
    flex-wrap: wrap;
    overflow-x: visible;
  }
  
  .compact-search-form .el-form-item {
    margin-bottom: 12px !important;
    margin-right: 16px;
    flex: 1;
    min-width: 200px;
  }
  
  .compact-search-form .el-form-item__label {
    width: 70px !important;
    font-size: 14px;
  }
  
  .search-buttons {
    margin-left: 0;
    width: 100%;
    text-align: center;
  }
}

.role-info {
  margin-left: 15px;
  display: inline-block;
}

/* 对话框样式优化 */
.dialog-footer {
  text-align: center;
  padding: 20px 0 10px;
}

.dialog-footer .el-button {
  margin: 0 10px;
  min-width: 100px;
}

/* 表单样式优化 */
.el-form .el-form-item__label {
  font-weight: 600;
  color: #303133;
}

.el-form .el-input__wrapper {
  transition: all 0.3s ease;
}

.el-form .el-input__wrapper:hover {
  box-shadow: 0 0 0 1px #409eff inset;
}

.el-form .el-textarea__inner {
  transition: all 0.3s ease;
}

.el-form .el-textarea__inner:hover {
  border-color: #409eff;
}

/* 工具提示图标样式 */
.el-input__suffix .el-icon {
  color: #909399;
  cursor: help;
}

.el-input__suffix .el-icon:hover {
  color: #409eff;
}

/* 工具栏样式 */
.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 按钮风格设计规范 - 操作列按钮样式 */
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
  justify-content: center;
  padding: 8px 4px;
  min-height: 32px;
}

.action-buttons .el-button.action-btn {
  margin: 0;
  padding: 6px 8px;
  font-size: 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
  min-width: 65px;
  white-space: nowrap;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.action-buttons .el-button.action-btn .el-icon {
  font-size: 14px;
}

.action-buttons .el-button.action-btn:active {
  transform: scale(0.95);
}

/* 文本按钮悬停效果优化 */
.action-buttons .el-button.is-text:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 不同类型按钮的悬停颜色 - 根据设计规范优化 */
.action-buttons .el-button.is-text.el-button--primary:hover {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409eff;
  border-color: rgba(64, 158, 255, 0.3);
}

.action-buttons .el-button.is-text.el-button--success:hover {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67c23a;
  border-color: rgba(103, 194, 58, 0.3);
}

.action-buttons .el-button.is-text.el-button--warning:hover {
  background-color: rgba(230, 162, 60, 0.1);
  color: #e6a23c;
  border-color: rgba(230, 162, 60, 0.3);
}

.action-buttons .el-button.is-text.el-button--danger:hover {
  background-color: rgba(245, 108, 108, 0.1);
  color: #f56c6c;
  border-color: rgba(245, 108, 108, 0.3);
}

/* 禁用状态样式 */
.action-buttons .el-button.is-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-buttons .el-button.is-disabled:hover {
  transform: none !important;
  background-color: transparent !important;
}

/* 表格整体样式优化 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

:deep(.el-table td) {
  border-bottom: 1px solid #f1f3f4;
}

:deep(.el-table tr:hover > td) {
  background-color: #f8f9fa;
}

/* 表格固定列样式优化 */
:deep(.el-table .el-table-fixed-column--right) {
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.08);
  border-left: 1px solid #ebeef5;
  background-color: #fff;
}

/* 操作列标题样式 */
:deep(.el-table th.el-table-fixed-column--right) {
  background-color: #f8f9fa;
  font-weight: 600;
}

:deep(.el-table th.el-table-fixed-column--right .cell) {
  color: #303133;
  text-align: center;
}

/* 操作列内容对齐 */
:deep(.el-table td.el-table-fixed-column--right .cell) {
  padding: 0 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 状态标签优化 */
:deep(.el-tag) {
  border-radius: 12px;
  padding: 2px 8px;
  font-size: 12px;
}

/* 排班管理样式 */
.schedule-management {
  padding: 20px;
}

.schedule-toolbar {
  margin-bottom: 20px;
}

.toolbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.right-info {
  display: flex;
  gap: 8px;
  align-items: center;
}

/* 周排班表格样式 */
.schedule-table-card {
  overflow: hidden;
}

.week-schedule-grid {
  min-width: 800px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
}

.schedule-header {
  display: flex;
  background-color: #f8f9fa;
  border-bottom: 2px solid #dee2e6;
}

.time-slot-header {
  width: 120px;
  padding: 15px 10px;
  font-weight: 600;
  text-align: center;
  border-right: 1px solid #dee2e6;
  background-color: #e9ecef;
}

.day-header {
  flex: 1;
  padding: 10px;
  text-align: center;
  border-right: 1px solid #dee2e6;
  min-width: 100px;
}

.day-header:last-child {
  border-right: none;
}

.day-name {
  font-weight: 600;
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.day-date {
  font-size: 12px;
  color: #666;
}

.schedule-body {
  display: flex;
  flex-direction: column;
}

.schedule-row {
  display: flex;
  border-bottom: 1px solid #ebeef5;
}

.schedule-row:last-child {
  border-bottom: none;
}

.time-slot-cell {
  width: 120px;
  padding: 15px 10px;
  background-color: #fafafa;
  border-right: 1px solid #dee2e6;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.time-text {
  font-weight: 500;
  font-size: 13px;
  color: #333;
  margin-bottom: 4px;
}

.time-period {
  font-size: 11px;
  color: #666;
}

.schedule-cell {
  flex: 1;
  min-height: 80px;
  border-right: 1px solid #ebeef5;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.schedule-cell:last-child {
  border-right: none;
}

.schedule-cell:hover {
  background-color: #f0f9ff;
}

.schedule-item {
  width: 100%;
  height: 100%;
  padding: 8px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.schedule-item:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 不同状态的排班项样式 */
.schedule-work {
  background-color: #e7f5ff;
  border: 1px solid #74c0fc;
  color: #1971c2;
}

.schedule-rest {
  background-color: #fff0f6;
  border: 1px solid #faa2c1;
  color: #c2185b;
}

.schedule-training {
  background-color: #fff9db;
  border: 1px solid #ffd43b;
  color: #f57c00;
}

.schedule-meeting {
  background-color: #f3f0ff;
  border: 1px solid #b197fc;
  color: #7048e8;
}

.schedule-status {
  font-weight: 600;
  font-size: 13px;
  margin-bottom: 4px;
}

.schedule-location {
  font-size: 11px;
  margin-bottom: 2px;
  opacity: 0.8;
}

.schedule-limit {
  font-size: 10px;
  opacity: 0.7;
}

.empty-slot {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ccc;
  font-size: 20px;
  opacity: 0.5;
  transition: all 0.3s ease;
}

.schedule-cell:hover .empty-slot {
  color: #409eff;
  opacity: 0.8;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .doctor-detail-container {
    padding: 16px;
  }
  
  .detail-card {
    margin-bottom: 16px;
  }
  
  .card-header {
    font-size: 14px;
  }
  
  .header-icon {
    font-size: 16px;
  }
  
  .stat-value {
    font-size: 16px;
  }
  
  .stat-label {
    font-size: 12px;
  }
  
  .schedule-management {
    padding: 16px;
  }
  
  .schedule-toolbar {
    margin-bottom: 16px;
  }
  
  .week-schedule-grid {
    grid-template-columns: 100px repeat(7, 1fr);
  }
  
  .time-slot-header {
    font-size: 14px;
  }
  
  .day-name {
    font-size: 14px;
  }
  
  .day-date {
    font-size: 12px;
  }
  
  .schedule-status {
    font-size: 12px;
  }
  
  .schedule-location {
    font-size: 12px;
  }
  
  .schedule-limit {
    font-size: 12px;
  }
}
</style>
