package com.smartcare.cloud.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件存储服务接口
 * 处理文件上传、下载和删除
 */
public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @param fileType 文件类型(photo/report/document)
     * @param relatedId 关联ID(如老人ID、报告ID等)
     * @return 文件访问URL
     * @throws IOException 文件操作异常
     */
    String uploadFile(MultipartFile file, String fileType, Long relatedId) throws IOException;

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);

    /**
     * 验证文件类型
     *
     * @param file 文件对象
     * @param allowedTypes 允许的文件类型数组
     * @return 是否为允许的类型
     */
    boolean validateFileType(MultipartFile file, String[] allowedTypes);

    /**
     * 验证文件大小
     *
     * @param file 文件对象
     * @param maxSizeMB 最大文件大小(MB)
     * @return 是否在允许范围内
     */
    boolean validateFileSize(MultipartFile file, long maxSizeMB);
}
