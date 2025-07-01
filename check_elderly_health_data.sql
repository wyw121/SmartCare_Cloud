-- 查询数据库中老人的健康状态分布
-- 检查health_status字段的实际数据
-- 1. 查看所有老人的健康状态分布
SELECT health_status,
  COUNT(*) as count,
  ROUND(
    COUNT(*) * 100.0 / (
      SELECT COUNT(*)
      FROM elderly
    ),
    2
  ) as percentage
FROM elderly
GROUP BY health_status
ORDER BY count DESC;
-- 2. 查看健康状态字段的详细情况（包括空值）
SELECT CASE
    WHEN health_status IS NULL THEN '[NULL]'
    WHEN health_status = '' THEN '[EMPTY]'
    ELSE health_status
  END as health_status_display,
  COUNT(*) as count
FROM elderly
GROUP BY health_status
ORDER BY count DESC;
-- 3. 查看各年龄段的数据分布
SELECT CASE
    WHEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 60 AND 69 THEN '60-70岁'
    WHEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 70 AND 79 THEN '70-80岁'
    WHEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) BETWEEN 80 AND 89 THEN '80-90岁'
    WHEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) >= 90 THEN '90岁以上'
    ELSE '其他'
  END as age_range,
  health_status,
  COUNT(*) as count
FROM elderly
WHERE birth_date IS NOT NULL
GROUP BY age_range,
  health_status
ORDER BY age_range,
  count DESC;
-- 4. 查看具体的老人数据样例
SELECT id,
  name,
  birth_date,
  TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) as age,
  health_status,
  care_level,
  created_time
FROM elderly
ORDER BY created_time DESC
LIMIT 20;