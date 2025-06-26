package com.smartcare.cloud.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 医生统计信息VO
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
public class DoctorStatisticsVO {

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 科室
     */
    private String department;

    /**
     * 职称
     */
    private String title;

    /**
     * 负责老人数量
     */
    private Integer elderlyCount;

    /**
     * 本月诊疗次数
     */
    private Integer monthlyConsultations;

    /**
     * 本月健康记录数
     */
    private Integer monthlyHealthRecords;

    /**
     * 本月处理预警数
     */
    private Integer monthlyWarningsHandled;

    /**
     * 满意度评分
     */
    private Double satisfactionScore;

    /**
     * 本月工作时长
     */
    private Double monthlyWorkingHours;

    /**
     * 最近活跃时间
     */
    private LocalDateTime lastActiveTime;

    /**
     * 本周排班列表
     */
    private List<WeeklySchedule> weeklySchedules;

    /**
     * 负责的重点老人列表
     */
    private List<KeyElderly> keyElderlyList;

    // 内部类：周排班
    public static class WeeklySchedule {
        private String dayOfWeek;
        private String timeSlot;
        private String scheduleType;
        private Integer appointmentCount;
        private Integer maxAppointments;

        // Getter and Setter methods
        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public String getScheduleType() {
            return scheduleType;
        }

        public void setScheduleType(String scheduleType) {
            this.scheduleType = scheduleType;
        }

        public Integer getAppointmentCount() {
            return appointmentCount;
        }

        public void setAppointmentCount(Integer appointmentCount) {
            this.appointmentCount = appointmentCount;
        }

        public Integer getMaxAppointments() {
            return maxAppointments;
        }

        public void setMaxAppointments(Integer maxAppointments) {
            this.maxAppointments = maxAppointments;
        }
    }

    // 内部类：重点老人
    public static class KeyElderly {
        private Long elderlyId;
        private String elderlyName;
        private String healthStatus;
        private Integer warningCount;
        private LocalDateTime lastCheckTime;

        // Getter and Setter methods
        public Long getElderlyId() {
            return elderlyId;
        }

        public void setElderlyId(Long elderlyId) {
            this.elderlyId = elderlyId;
        }

        public String getElderlyName() {
            return elderlyName;
        }

        public void setElderlyName(String elderlyName) {
            this.elderlyName = elderlyName;
        }

        public String getHealthStatus() {
            return healthStatus;
        }

        public void setHealthStatus(String healthStatus) {
            this.healthStatus = healthStatus;
        }

        public Integer getWarningCount() {
            return warningCount;
        }

        public void setWarningCount(Integer warningCount) {
            this.warningCount = warningCount;
        }

        public LocalDateTime getLastCheckTime() {
            return lastCheckTime;
        }

        public void setLastCheckTime(LocalDateTime lastCheckTime) {
            this.lastCheckTime = lastCheckTime;
        }
    }

    // 主类的 Getter and Setter methods
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getElderlyCount() {
        return elderlyCount;
    }

    public void setElderlyCount(Integer elderlyCount) {
        this.elderlyCount = elderlyCount;
    }

    public Integer getMonthlyConsultations() {
        return monthlyConsultations;
    }

    public void setMonthlyConsultations(Integer monthlyConsultations) {
        this.monthlyConsultations = monthlyConsultations;
    }

    public Integer getMonthlyHealthRecords() {
        return monthlyHealthRecords;
    }

    public void setMonthlyHealthRecords(Integer monthlyHealthRecords) {
        this.monthlyHealthRecords = monthlyHealthRecords;
    }

    public Integer getMonthlyWarningsHandled() {
        return monthlyWarningsHandled;
    }

    public void setMonthlyWarningsHandled(Integer monthlyWarningsHandled) {
        this.monthlyWarningsHandled = monthlyWarningsHandled;
    }

    public Double getSatisfactionScore() {
        return satisfactionScore;
    }

    public void setSatisfactionScore(Double satisfactionScore) {
        this.satisfactionScore = satisfactionScore;
    }

    public Double getMonthlyWorkingHours() {
        return monthlyWorkingHours;
    }

    public void setMonthlyWorkingHours(Double monthlyWorkingHours) {
        this.monthlyWorkingHours = monthlyWorkingHours;
    }

    public LocalDateTime getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(LocalDateTime lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public List<WeeklySchedule> getWeeklySchedules() {
        return weeklySchedules;
    }

    public void setWeeklySchedules(List<WeeklySchedule> weeklySchedules) {
        this.weeklySchedules = weeklySchedules;
    }

    public List<KeyElderly> getKeyElderlyList() {
        return keyElderlyList;
    }

    public void setKeyElderlyList(List<KeyElderly> keyElderlyList) {
        this.keyElderlyList = keyElderlyList;
    }
}
