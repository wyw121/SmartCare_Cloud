/**
 * 智慧医养平台 - 基础测试数据生成脚本
 * 生成: 机构、用户、医生、护工、社工、老人、关联关系
 * 运行: node generate_basic_data.js > basic_data.sql
 */

// ==================== 配置 ====================
const CONFIG = {
  organizations: 4,      // 机构数量
  admins: 3,            // 管理员数量
  doctors: 10,          // 医生数量
  nurses: 20,           // 护工数量
  socialWorkers: 5,     // 社工数量
  elderly: 100,         // 老人数量
  familyMembers: 50     // 家属数量
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

function formatDateTime(date) {
  return formatDate(date) + ' ' + date.toTimeString().split(' ')[0];
}

function escape(str) {
  if (str === null || str === undefined) return 'NULL';
  return "'" + String(str).replace(/'/g, "''") + "'";
}

// ==================== 数据生成 ====================

console.log('-- =====================================================');
console.log('-- 智慧医养平台 - 基础测试数据');
console.log('-- 生成时间:', new Date().toISOString());
console.log('-- =====================================================\n');

console.log('USE smartcare_cloud;\n');
console.log('SET FOREIGN_KEY_CHECKS = 0;\n');

// 1. 机构数据
console.log('-- =====================================================');
console.log('-- 1. 机构信息数据');
console.log('-- =====================================================');

const organizations = [
  { id: 1, code: 'ORG001', name: '智慧医养综合服务中心', type: 'nursing_home', capacity: 200 },
  { id: 2, code: 'ORG002', name: '附属康复医院', type: 'hospital', capacity: 100 },
  { id: 3, code: 'ORG003', name: '阳光养老院', type: 'nursing_home', capacity: 150 },
  { id: 4, code: 'ORG004', name: '社区健康服务中心', type: 'community', capacity: 50 }
];

console.log('TRUNCATE TABLE organization_info;');
console.log('INSERT INTO organization_info (id, org_code, org_name, org_type, parent_org_id, contact_person, contact_phone, address, capacity, current_occupancy, status) VALUES');
organizations.forEach((org, index) => {
  const isLast = index === organizations.length - 1;
  console.log(`(${org.id}, ${escape(org.code)}, ${escape(org.name)}, ${escape(org.type)}, NULL, '李经理', '13800138000', '北京市朝阳区XX路XX号', ${org.capacity}, ${randomInt(Math.floor(org.capacity * 0.6), Math.floor(org.capacity * 0.9))}, 1)${isLast ? ';' : ','}`);
});
console.log('');

// 2. 系统用户数据
console.log('-- =====================================================');
console.log('-- 2. 系统用户数据');
console.log('-- =====================================================');

const users = [];
let userId = 1;

// 系统管理员
console.log('-- 2.1 系统管理员');
console.log('TRUNCATE TABLE sys_user;');
console.log('INSERT INTO sys_user (id, username, password, real_name, gender, phone, email, role_code, role_name, organization_id, status) VALUES');

for (let i = 0; i < CONFIG.admins; i++) {
  users.push({
    id: userId++,
    username: i === 0 ? 'admin' : `admin${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', // 123456
    realName: i === 0 ? '系统管理员' : `管理员${i + 1}`,
    gender: randomChoice([0, 1]),
    phone: `1380013800${i}`,
    email: `admin${i + 1}@smartcare.com`,
    roleCode: 'system_admin',
    roleName: '系统管理员',
    organizationId: randomChoice(organizations).id
  });
}

// 业务管理员
console.log('-- 2.2 业务管理员');
for (let i = 0; i < 3; i++) {
  users.push({
    id: userId++,
    username: `business_admin${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
    realName: `业务管理员${i + 1}`,
    gender: randomChoice([0, 1]),
    phone: `1380013801${i}`,
    email: `business${i + 1}@smartcare.com`,
    roleCode: 'business_admin',
    roleName: '业务管理员',
    organizationId: organizations[i % organizations.length].id
  });
}

// 医生用户
console.log('-- 2.3 医生用户');
const doctorNames = ['张伟', '李娜', '王芳', '刘强', '陈敏', '杨洋', '赵丽', '黄刚', '周静', '吴军'];
for (let i = 0; i < CONFIG.doctors; i++) {
  users.push({
    id: userId++,
    username: `doctor${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
    realName: doctorNames[i % doctorNames.length] + '医生',
    gender: randomChoice([0, 1]),
    phone: `1380013802${String(i).padStart(2, '0')}`,
    email: `doctor${i + 1}@smartcare.com`,
    roleCode: 'doctor',
    roleName: '医生',
    organizationId: randomChoice(organizations).id
  });
}

// 护工用户
console.log('-- 2.4 护工用户');
for (let i = 0; i < CONFIG.nurses; i++) {
  users.push({
    id: userId++,
    username: `nurse${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
    realName: `护工${i + 1}`,
    gender: randomChoice([0, 1]),
    phone: `1380013803${String(i).padStart(2, '0')}`,
    email: `nurse${i + 1}@smartcare.com`,
    roleCode: 'nurse',
    roleName: '护工',
    organizationId: randomChoice(organizations).id
  });
}

// 社工用户
console.log('-- 2.5 社工用户');
for (let i = 0; i < CONFIG.socialWorkers; i++) {
  users.push({
    id: userId++,
    username: `social_worker${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
    realName: `社工${i + 1}`,
    gender: randomChoice([0, 1]),
    phone: `1380013804${String(i).padStart(2, '0')}`,
    email: `social${i + 1}@smartcare.com`,
    roleCode: 'social_worker',
    roleName: '社工',
    organizationId: randomChoice(organizations).id
  });
}

// 家属用户
console.log('-- 2.6 家属用户');
for (let i = 0; i < CONFIG.familyMembers; i++) {
  users.push({
    id: userId++,
    username: `family${i + 1}`,
    password: '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
    realName: `家属${i + 1}`,
    gender: randomChoice([0, 1]),
    phone: `1380013805${String(i).padStart(2, '0')}`,
    email: `family${i + 1}@smartcare.com`,
    roleCode: 'family',
    roleName: '家属',
    organizationId: null
  });
}

users.forEach((user, index) => {
  const isLast = index === users.length - 1;
  console.log(`(${user.id}, ${escape(user.username)}, ${escape(user.password)}, ${escape(user.realName)}, ${user.gender}, ${escape(user.phone)}, ${escape(user.email)}, ${escape(user.roleCode)}, ${escape(user.roleName)}, ${user.organizationId || 'NULL'}, 1)${isLast ? ';' : ','}`);
});
console.log('');

// 3. 医生信息数据
console.log('-- =====================================================');
console.log('-- 3. 医生信息数据');
console.log('-- =====================================================');

const doctorUsers = users.filter(u => u.roleCode === 'doctor');
const departments = ['内科', '外科', '康复科', '中医科', '老年科'];
const titles = ['主任医师', '副主任医师', '主治医师', '医师'];

console.log('TRUNCATE TABLE t_doctor;');
console.log('INSERT INTO t_doctor (id, user_id, organization_id, employee_number, name, gender, phone, email, department, title, specialization, experience_years, max_patients, status) VALUES');

doctorUsers.forEach((user, index) => {
  const isLast = index === doctorUsers.length - 1;
  const gender = user.gender === 1 ? '男' : '女';
  console.log(`(${index + 1}, ${user.id}, ${user.organizationId}, 'DOC${String(index + 1).padStart(4, '0')}', ${escape(user.realName)}, ${escape(gender)}, ${escape(user.phone)}, ${escape(user.email)}, ${escape(randomChoice(departments))}, ${escape(randomChoice(titles))}, '老年慢性病管理', ${randomInt(5, 30)}, ${randomInt(40, 60)}, 1)${isLast ? ';' : ','}`);
});
console.log('');

// 4. 护工信息数据
console.log('-- =====================================================');
console.log('-- 4. 护工信息数据');
console.log('-- =====================================================');

const nurseUsers = users.filter(u => u.roleCode === 'nurse');
const workAreas = ['一病区', '二病区', '三病区', '四病区'];

console.log('TRUNCATE TABLE nurse_info;');
console.log('INSERT INTO nurse_info (id, user_id, organization_id, employee_number, name, gender, phone, work_area, max_care_count, status) VALUES');

nurseUsers.forEach((user, index) => {
  const isLast = index === nurseUsers.length - 1;
  console.log(`(${index + 1}, ${user.id}, ${user.organizationId}, 'NUR${String(index + 1).padStart(4, '0')}', ${escape(user.realName)}, ${user.gender}, ${escape(user.phone)}, ${escape(randomChoice(workAreas))}, ${randomInt(3, 6)}, 1)${isLast ? ';' : ','}`);
});
console.log('');

// 5. 社工信息数据
console.log('-- =====================================================');
console.log('-- 5. 社工信息数据');
console.log('-- =====================================================');

const socialWorkerUsers = users.filter(u => u.roleCode === 'social_worker');

console.log('TRUNCATE TABLE social_worker_info;');
console.log('INSERT INTO social_worker_info (id, user_id, organization_id, employee_number, name, gender, phone, specialization, status) VALUES');

socialWorkerUsers.forEach((user, index) => {
  const isLast = index === socialWorkerUsers.length - 1;
  console.log(`(${index + 1}, ${user.id}, ${user.organizationId}, 'SW${String(index + 1).padStart(4, '0')}', ${escape(user.realName)}, ${user.gender}, ${escape(user.phone)}, '心理咨询与康复辅导', 1)${isLast ? ';' : ','}`);
});
console.log('');

// 6. 老人档案数据
console.log('-- =====================================================');
console.log('-- 6. 老人档案数据');
console.log('-- =====================================================');

const surnames = ['王', '李', '张', '刘', '陈', '杨', '黄', '赵', '吴', '周', '徐', '孙', '马', '朱', '胡', '郭', '何', '高', '林', '罗'];
const maleNames = ['明', '强', '军', '伟', '勇', '刚', '华', '国', '建', '文'];
const femaleNames = ['芳', '秀', '丽', '静', '敏', '娟', '兰', '红', '梅', '霞'];
const healthStatuses = ['good', 'fair', 'poor'];
const careLevels = [1, 2, 3];

console.log('TRUNCATE TABLE elderly;');
console.log('INSERT INTO elderly (id, organization_id, name, gender, birth_date, phone, address, room_number, admission_date, admission_type, health_status, care_level, disability_level, payment_method, responsible_doctor_id, responsible_nurse_id, status) VALUES');

for (let i = 0; i < CONFIG.elderly; i++) {
  const isLast = i === CONFIG.elderly - 1;
  const gender = randomChoice([0, 1]);
  const name = randomChoice(surnames) + (gender === 1 ? randomChoice(maleNames) : randomChoice(femaleNames));
  const birthDate = formatDate(randomDate(new Date(1930, 0, 1), new Date(1960, 11, 31)));
  const admissionDate = formatDate(randomDate(new Date(2022, 0, 1), new Date(2024, 9, 1)));
  const roomNumber = `${randomInt(1, 5)}${String(randomInt(1, 20)).padStart(2, '0')}`;
  const doctorId = randomInt(1, doctorUsers.length);
  const nurseId = randomInt(1, nurseUsers.length);
  const orgId = randomChoice(organizations).id;
  
  console.log(`(${i + 1}, ${orgId}, ${escape(name)}, ${gender}, ${escape(birthDate)}, '138001380${String(i).padStart(2, '0')}', '北京市朝阳区XX街道XX号', ${escape(roomNumber)}, ${escape(admissionDate)}, 'long_term', ${escape(randomChoice(healthStatuses))}, ${randomChoice(careLevels)}, ${randomChoice(careLevels)}, ${escape(randomChoice(['self', 'insurance', 'government']))}, ${doctorId}, ${nurseId}, 1)${isLast ? ';' : ','}`);
}
console.log('');

// 7. 医生-老人关联数据
console.log('-- =====================================================');
console.log('-- 7. 医生-老人关联数据');
console.log('-- =====================================================');

console.log('TRUNCATE TABLE doctor_elderly_relation;');
console.log('INSERT INTO doctor_elderly_relation (doctor_id, elderly_id, relationship_type, start_date, status) VALUES');

let relationId = 1;
for (let i = 1; i <= CONFIG.elderly; i++) {
  const isLast = i === CONFIG.elderly;
  const doctorId = ((i - 1) % doctorUsers.length) + 1;
  const startDate = formatDate(randomDate(new Date(2023, 0, 1), new Date(2024, 9, 1)));
  console.log(`(${doctorId}, ${i}, 'primary', ${escape(startDate)}, 1)${isLast ? ';' : ','}`);
}
console.log('');

// 8. 护工-老人关联数据
console.log('-- =====================================================');
console.log('-- 8. 护工-老人关联数据');
console.log('-- =====================================================');

console.log('TRUNCATE TABLE nurse_elderly_relation;');
console.log('INSERT INTO nurse_elderly_relation (nurse_id, elderly_id, care_level, start_date, status) VALUES');

for (let i = 1; i <= CONFIG.elderly; i++) {
  const isLast = i === CONFIG.elderly;
  const nurseId = ((i - 1) % nurseUsers.length) + 1;
  const startDate = formatDate(randomDate(new Date(2023, 0, 1), new Date(2024, 9, 1)));
  console.log(`(${nurseId}, ${i}, ${randomChoice([1, 2, 3])}, ${escape(startDate)}, 1)${isLast ? ';' : ','}`);
}
console.log('');

// 9. 家属-老人关联数据
console.log('-- =====================================================');
console.log('-- 9. 家属-老人关联数据');
console.log('-- =====================================================');

const relationships = ['儿子', '女儿', '儿媳', '女婿', '孙子', '孙女'];
const accessLevels = ['basic', 'health', 'full'];

console.log('TRUNCATE TABLE family_elderly_relation;');
console.log('INSERT INTO family_elderly_relation (family_id, elderly_id, relationship, access_level, is_primary_contact, status) VALUES');

const familyUsers = users.filter(u => u.roleCode === 'family');
for (let i = 0; i < CONFIG.familyMembers; i++) {
  const isLast = i === CONFIG.familyMembers - 1;
  const familyUserId = familyUsers[i].id;
  const elderlyId = (i % CONFIG.elderly) + 1; // 每个家属关联一个老人
  const isPrimary = i < CONFIG.elderly ? 1 : 0; // 前100个设为主要联系人
  
  console.log(`(${familyUserId}, ${elderlyId}, ${escape(randomChoice(relationships))}, ${escape(randomChoice(accessLevels))}, ${isPrimary}, 1)${isLast ? ';' : ','}`);
}
console.log('');

console.log('SET FOREIGN_KEY_CHECKS = 1;\n');
console.log('-- =====================================================');
console.log('-- 基础数据生成完成!');
console.log('-- 总计:');
console.log(`--   机构: ${organizations.length}`);
console.log(`--   用户: ${users.length}`);
console.log(`--   医生: ${doctorUsers.length}`);
console.log(`--   护工: ${nurseUsers.length}`);
console.log(`--   社工: ${socialWorkerUsers.length}`);
console.log(`--   老人: ${CONFIG.elderly}`);
console.log(`--   家属: ${CONFIG.familyMembers}`);
console.log('-- =====================================================');
