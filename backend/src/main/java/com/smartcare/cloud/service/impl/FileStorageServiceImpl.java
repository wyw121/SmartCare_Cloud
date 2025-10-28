package com.smartcare.cloud.service.impl;

import com.smartcare.cloud.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件存储服务实现类
 * 提供本地文件存储功能
 */
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload.base-path:./uploads}")
    private String basePath;

    @Value("${file.upload.max-size-mb:10}")
    private long maxSizeMB;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String serverPort;

    // 允许的图片文件类型
    private static final String[] ALLOWED_IMAGE_TYPES = {
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    };

    // 允许的文档文件类型
    private static final String[] ALLOWED_DOCUMENT_TYPES = {
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    };

    @Override
    public String uploadFile(MultipartFile file, String fileType, Long relatedId) throws IOException {
        // 验证文件
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 验证文件大小
        if (!validateFileSize(file, maxSizeMB)) {
            throw new IllegalArgumentException("文件大小不能超过" + maxSizeMB + "MB");
        }

        // 根据文件类型验证文件格式
        String[] allowedTypes;
        if ("photo".equals(fileType)) {
            allowedTypes = ALLOWED_IMAGE_TYPES;
        } else if ("report".equals(fileType) || "document".equals(fileType)) {
            allowedTypes = ALLOWED_DOCUMENT_TYPES;
        } else {
            // 其他类型允许所有文件
            allowedTypes = new String[]{};
        }

        if (allowedTypes.length > 0 && !validateFileType(file, allowedTypes)) {
            throw new IllegalArgumentException("不支持的文件类型: " + file.getContentType());
        }

        // 生成文件存储路径
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 按日期和类型组织文件夹结构: uploads/fileType/relatedId/yyyyMMdd/uuid.ext
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String relatedFolder = relatedId != null ? relatedId.toString() : "common";
        String fileName = UUID.randomUUID().toString() + extension;
        
        String relativePath = fileType + File.separator + 
                             relatedFolder + File.separator + 
                             dateFolder + File.separator + 
                             fileName;

        // 创建完整路径
        Path uploadPath = Paths.get(basePath, fileType, relatedFolder, dateFolder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 生成访问URL
        String fileUrl = "/files/" + relativePath.replace(File.separator, "/");
        
        log.info("文件上传成功: {}, 大小: {} bytes, 类型: {}, 关联ID: {}", 
                fileName, file.getSize(), fileType, relatedId);

        return fileUrl;
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                return false;
            }

            // 从URL提取文件路径
            String relativePath = fileUrl.replace("/files/", "");
            Path filePath = Paths.get(basePath, relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("文件删除成功: {}", fileUrl);
                return true;
            } else {
                log.warn("文件不存在: {}", fileUrl);
                return false;
            }
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public boolean validateFileType(MultipartFile file, String[] allowedTypes) {
        if (file == null || allowedTypes == null || allowedTypes.length == 0) {
            return true;
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        for (String allowedType : allowedTypes) {
            if (contentType.equals(allowedType)) {
                return true;
            }
        }

        log.warn("不支持的文件类型: {}, 允许的类型: {}", contentType, String.join(", ", allowedTypes));
        return false;
    }

    @Override
    public boolean validateFileSize(MultipartFile file, long maxSizeMB) {
        if (file == null) {
            return false;
        }

        long maxSizeBytes = maxSizeMB * 1024 * 1024;
        boolean valid = file.getSize() <= maxSizeBytes;

        if (!valid) {
            log.warn("文件大小超过限制: {} bytes, 最大允许: {} bytes", 
                    file.getSize(), maxSizeBytes);
        }

        return valid;
    }
}
