/**
 * 智慧医养平台 - 业务测试数据生成脚本
 * 生成: 健康记录、健康预警、护理记录、用药记录、巡诊记录
 * 运行: node generate_business_data.js > business_data.sql
 */

// ==================== 配置 ====================
const CONFIG = {
  elderly: 100,              // 老人数量
  doctors: 10,               // 医生数量
  nurses: 20,                // 护工数量
  healthRecordsPerElderly: 30,  // 每个老人的健康记录数
  nursingRecordsPerElderly: 20, // 每个老人的护理记录数
  medicationsPerElderly: 2,     // 每个老人的用药记录数
  visitsPerDoctor: 15,          // 每个医生的巡诊记录数
  warningsCount: 50             // 健康预警总数
};

// ==================== 工具函数 ====================
function randomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randomChoice(array) {
  return array[randomInt(0, array.length - 1)];
}

function randomDate(start, end) {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
}

function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

function formatTime(date) {
  return date.toTimeString().split(' ')[0];
}

function formatDateTime(date) {
  return formatDate(date) + ' ' + formatTime(date);
}

function escape(str) {
  if (str === null || str === undefined) return 'NULL';
  return "'" + String(str).replace(/'/g, "''") + "'";
}

// ==================== 数据生成 ====================

console.log('-- =====================================================');
console.log('-- 智慧医养平台 - 业务测试数据');
console.log('-- 生成时间:', new Date().toISOString());
console.log('-- =====================================================\n');

console.log('USE smartcare_cloud;\n');
console.log('SET FOREIGN_KEY_CHECKS = 0;\n');

// 1. 健康记录数据
console.log('-- =====================================================');
console.log('-- 1. 健康记录数据');
console.log('-- =====================================================');

console.log('TRUNCATE TABLE health_record;');
console.log('INSERT INTO health_record (elderly_id, record_date, record_time, record_type, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, blood_oxygen, recorder_id) VALUES');

const startDate = new Date(2024, 7, 1); // 2024-08-01
const endDate = new Date(2024, 9, 28);  // 2024-10-28

let recordId = 1;
for (let elderlyId = 1; elderlyId <= CONFIG.elderly; elderlyId++) {
  for (let j = 0; j < CONFIG.healthRecordsPerElderly; j++) {
    const isLast = elderlyId === CONFIG.elderly && j === CONFIG.healthRecordsPerElderly - 1;
    const recordDate = formatDate(randomDate(startDate, endDate));
    const recordTime = `${String(randomInt(6, 20)).padStart(2, '0')}:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
    const bpHigh = randomInt(110, 160);
    const bpLow = randomInt(60, 95);
    const heartRate = randomInt(60, 100);
    const temperature = (36 + Math.random() * 1.5).toFixed(1);
    const bloodSugar = (4 + Math.random() * 6).toFixed(2);
    const bloodOxygen = randomInt(95, 100);
    const recorderId = randomInt(1, CONFIG.nurses);
    
    console.log(`(${elderlyId}, ${escape(recordDate)}, ${escape(recordTime)}, 'vital_signs', ${bpHigh}, ${bpLow}, ${heartRate}, ${temperature}, ${bloodSugar}, ${bloodOxygen}, ${recorderId})${isLast ? ';' : ','}`);
    recordId++;
  }
}
console.log('');

// 2. 健康预警数据
console.log('-- =====================================================');
console.log('-- 2. 健康预警数据');
console.log('-- =====================================================');

const warningTypes = ['blood_pressure', 'blood_sugar', 'heart_rate', 'temperature', 'blood_oxygen'];
const warningLevels = ['low', 'medium', 'high', 'critical'];
const warningStatuses = ['pending', 'assigned', 'processing', 'completed'];

console.log('TRUNCATE TABLE health_warning;');
console.log('INSERT INTO health_warning (elderly_id, warning_type, warning_level, warning_time, indicator_name, indicator_value, normal_range, warning_message, status, assigned_doctor_id, handle_time, handler_name, handle_method) VALUES');

for (let i = 0; i < CONFIG.warningsCount; i++) {
  const isLast = i === CONFIG.warningsCount - 1;
  const elderlyId = randomInt(1, CONFIG.elderly);
  const warningType = randomChoice(warningTypes);
  const warningLevel = randomChoice(warningLevels);
  const warningTime = formatDateTime(randomDate(startDate, endDate));
  const status = randomChoice(warningStatuses);
  const doctorId = ((elderlyId - 1) % CONFIG.doctors) + 1;
  
  let indicatorName, indicatorValue, normalRange, warningMessage;
  
  switch (warningType) {
    case 'blood_pressure':
      indicatorName = '血压';
      indicatorValue = `${randomInt(160, 180)}/${randomInt(95, 110)}`;
      normalRange = '90-140/60-90 mmHg';
      warningMessage = '血压偏高，请注意监测';
      break;
    case 'blood_sugar':
      indicatorName = '血糖';
      indicatorValue = `${(10 + Math.random() * 5).toFixed(1)}`;
      normalRange = '3.9-6.1 mmol/L';
      warningMessage = '血糖偏高，建议复查';
      break;
    case 'heart_rate':
      indicatorName = '心率';
      indicatorValue = `${randomInt(105, 130)}`;
      normalRange = '60-100 次/分';
      warningMessage = '心率异常，请及时处理';
      break;
    case 'temperature':
      indicatorName = '体温';
      indicatorValue = `${(37.5 + Math.random() * 1.5).toFixed(1)}`;
      normalRange = '36.0-37.2 ℃';
      warningMessage = '体温偏高，疑似发热';
      break;
    case 'blood_oxygen':
      indicatorName = '血氧';
      indicatorValue = `${randomInt(85, 93)}`;
      normalRange = '95-100 %';
      warningMessage = '血氧饱和度偏低';
      break;
  }
  
  const handleTime = status === 'completed' ? formatDateTime(new Date(new Date(warningTime).getTime() + randomInt(1, 24) * 3600000)) : 'NULL';
  const handleMethod = status === 'completed' ? escape('已通知医生处理，建议调整用药') : 'NULL';
  
  console.log(`(${elderlyId}, ${escape(warningType)}, ${escape(warningLevel)}, ${escape(warningTime)}, ${escape(indicatorName)}, ${escape(indicatorValue)}, ${escape(normalRange)}, ${escape(warningMessage)}, ${escape(status)}, ${doctorId}, ${handleTime}, '张医生', ${handleMethod})${isLast ? ';' : ','}`);
}
console.log('');

// 3. 护理记录数据
console.log('-- =====================================================');
console.log('-- 3. 护理记录数据');
console.log('-- =====================================================');

const careTypes = ['morning_care', 'evening_care', 'feeding', 'bathing', 'turning', 'toileting', 'medication', 'exercise'];
const careTypeNames = {
  'morning_care': '晨间护理',
  'evening_care': '晚间护理',
  'feeding': '喂食',
  'bathing': '洗浴',
  'turning': '翻身',
  'toileting': '如厕协助',
  'medication': '用药协助',
  'exercise': '活动协助'
};

console.log('TRUNCATE TABLE nursing_record;');
console.log('INSERT INTO nursing_record (elderly_id, nurse_id, record_date, record_time, care_type, care_content, elderly_status, duration_minutes) VALUES');

recordId = 1;
for (let elderlyId = 1; elderlyId <= CONFIG.elderly; elderlyId++) {
  const nurseId = ((elderlyId - 1) % CONFIG.nurses) + 1;
  
  for (let j = 0; j < CONFIG.nursingRecordsPerElderly; j++) {
    const isLast = elderlyId === CONFIG.elderly && j === CONFIG.nursingRecordsPerElderly - 1;
    const recordDate = formatDate(randomDate(startDate, endDate));
    const careType = randomChoice(careTypes);
    
    let recordTime, careContent;
    if (careType === 'morning_care') {
      recordTime = `07:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助起床、洗漱、整理床铺';
    } else if (careType === 'evening_care') {
      recordTime = `20:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助洗漱、更换衣物、准备就寝';
    } else if (careType === 'feeding') {
      recordTime = `${String(randomChoice([8, 12, 18])).padStart(2, '0')}:${String(randomInt(0, 30)).padStart(2, '0')}:00`;
      careContent = '协助进餐，饮食正常';
    } else if (careType === 'bathing') {
      recordTime = `14:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助洗浴，清洁身体';
    } else if (careType === 'turning') {
      recordTime = `${String(randomInt(10, 16)).padStart(2, '0')}:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助翻身，预防压疮';
    } else if (careType === 'toileting') {
      recordTime = `${String(randomInt(9, 17)).padStart(2, '0')}:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助如厕';
    } else if (careType === 'medication') {
      recordTime = `${String(randomChoice([8, 12, 18])).padStart(2, '0')}:00:00`;
      careContent = '协助服药，已按时服用';
    } else {
      recordTime = `${String(randomInt(9, 16)).padStart(2, '0')}:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
      careContent = '协助活动，散步30分钟';
    }
    
    const duration = randomInt(10, 60);
    
    console.log(`(${elderlyId}, ${nurseId}, ${escape(recordDate)}, ${escape(recordTime)}, ${escape(careType)}, ${escape(careContent)}, '精神状态良好，配合护理', ${duration})${isLast ? ';' : ','}`);
    recordId++;
  }
}
console.log('');

// 4. 用药记录数据
console.log('-- =====================================================');
console.log('-- 4. 用药记录数据');
console.log('-- =====================================================');

const medications = [
  { name: '阿司匹林肠溶片', type: '抗血小板药', dosage: '100mg', frequency: '每日1次', route: 'oral', purpose: '预防心脑血管疾病' },
  { name: '硝苯地平缓释片', type: '降压药', dosage: '30mg', frequency: '每日2次', route: 'oral', purpose: '控制高血压' },
  { name: '二甲双胍片', type: '降糖药', dosage: '500mg', frequency: '每日3次', route: 'oral', purpose: '控制血糖' },
  { name: '阿托伐他汀钙片', type: '调脂药', dosage: '20mg', frequency: '每晚1次', route: 'oral', purpose: '降低血脂' },
  { name: '复方丹参滴丸', type: '中成药', dosage: '10粒', frequency: '每日3次', route: 'oral', purpose: '活血化瘀' }
];

console.log('TRUNCATE TABLE medication_record;');
console.log('INSERT INTO medication_record (elderly_id, doctor_id, medication_name, medication_type, dosage, frequency, start_date, administration_route, purpose, status) VALUES');

recordId = 1;
for (let elderlyId = 1; elderlyId <= CONFIG.elderly; elderlyId++) {
  const doctorId = ((elderlyId - 1) % CONFIG.doctors) + 1;
  
  for (let j = 0; j < CONFIG.medicationsPerElderly; j++) {
    const isLast = elderlyId === CONFIG.elderly && j === CONFIG.medicationsPerElderly - 1;
    const med = randomChoice(medications);
    const startDate = formatDate(randomDate(new Date(2024, 0, 1), new Date(2024, 7, 1)));
    
    console.log(`(${elderlyId}, ${doctorId}, ${escape(med.name)}, ${escape(med.type)}, ${escape(med.dosage)}, ${escape(med.frequency)}, ${escape(startDate)}, ${escape(med.route)}, ${escape(med.purpose)}, 1)${isLast ? ';' : ','}`);
    recordId++;
  }
}
console.log('');

// 5. 巡诊记录数据
console.log('-- =====================================================');
console.log('-- 5. 巡诊记录数据');
console.log('-- =====================================================');

console.log('TRUNCATE TABLE visit_record;');
console.log('INSERT INTO visit_record (elderly_id, doctor_id, visit_date, visit_time, chief_complaint, symptoms, diagnosis, treatment_plan, next_visit_date) VALUES');

recordId = 1;
for (let doctorId = 1; doctorId <= CONFIG.doctors; doctorId++) {
  // 每个医生负责的老人范围
  const eldersPerDoctor = Math.ceil(CONFIG.elderly / CONFIG.doctors);
  const startElderlyId = (doctorId - 1) * eldersPerDoctor + 1;
  const endElderlyId = Math.min(doctorId * eldersPerDoctor, CONFIG.elderly);
  
  for (let j = 0; j < CONFIG.visitsPerDoctor; j++) {
    const isLast = doctorId === CONFIG.doctors && j === CONFIG.visitsPerDoctor - 1;
    const elderlyId = randomInt(startElderlyId, endElderlyId);
    const visitDate = formatDate(randomDate(startDate, endDate));
    const visitTime = `${String(randomInt(9, 17)).padStart(2, '0')}:${String(randomInt(0, 59)).padStart(2, '0')}:00`;
    
    const complaints = [
      '血压控制不佳',
      '血糖波动',
      '睡眠质量差',
      '食欲不振',
      '关节疼痛',
      '头晕乏力',
      '咳嗽咳痰',
      '腹胀便秘'
    ];
    
    const chiefComplaint = randomChoice(complaints);
    const symptoms = '患者诉' + chiefComplaint + '，精神尚可';
    const diagnosis = '老年慢性病，建议继续观察';
    const treatmentPlan = '调整用药剂量，加强监测，注意休息';
    
    // 下次巡诊日期为7-14天后
    const nextVisitDate = formatDate(new Date(new Date(visitDate).getTime() + randomInt(7, 14) * 86400000));
    
    console.log(`(${elderlyId}, ${doctorId}, ${escape(visitDate)}, ${escape(visitTime)}, ${escape(chiefComplaint)}, ${escape(symptoms)}, ${escape(diagnosis)}, ${escape(treatmentPlan)}, ${escape(nextVisitDate)})${isLast ? ';' : ','}`);
    recordId++;
  }
}
console.log('');

console.log('SET FOREIGN_KEY_CHECKS = 1;\n');
console.log('-- =====================================================');
console.log('-- 业务数据生成完成!');
console.log('-- 总计:');
console.log(`--   健康记录: ${CONFIG.elderly * CONFIG.healthRecordsPerElderly}`);
console.log(`--   健康预警: ${CONFIG.warningsCount}`);
console.log(`--   护理记录: ${CONFIG.elderly * CONFIG.nursingRecordsPerElderly}`);
console.log(`--   用药记录: ${CONFIG.elderly * CONFIG.medicationsPerElderly}`);
console.log(`--   巡诊记录: ${CONFIG.doctors * CONFIG.visitsPerDoctor}`);
console.log('-- =====================================================');
