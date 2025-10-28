package com.smartcare.cloud.controller;

import com.smartcare.cloud.util.ResponseResult;
import com.smartcare.cloud.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 * 处理文件上传、删除等操作
 */
@Slf4j
@Tag(name = "文件管理", description = "文件上传、删除接口")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    private static final String KEY_URL = "url";
    private static final String KEY_FILE_NAME = "fileName";
    private static final String KEY_FILE_SIZE = "fileSize";
    private static final String ERROR_UPLOAD_FAILED = "上传失败: ";

    /**
     * 上传老人照片
     */
    @Operation(summary = "上传老人照片", description = "上传老人个人照片,支持jpg/png/gif格式,文件大小限制5MB")
    @PostMapping("/elderly/{elderlyId}/photo")
    public ResponseResult<Map<String, Object>> uploadElderlyPhoto(
            @Parameter(description = "老人ID", example = "1", required = true) 
            @PathVariable Long elderlyId,
            @Parameter(description = "照片文件,支持jpg/png/gif格式,最大5MB", required = true) 
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileStorageService.uploadFile(file, "photo", elderlyId);
            Map<String, Object> result = createFileInfoMap(file, fileUrl);
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("上传老人照片失败,老人ID:{}", elderlyId, e);
            return ResponseResult.error(ERROR_UPLOAD_FAILED + e.getMessage());
        }
    }

    /**
     * 上传健康报告
     */
    @Operation(summary = "上传健康报告", description = "上传老人健康报告,支持pdf/doc/docx/xls/xlsx格式,文件大小限制20MB")
    @PostMapping("/elderly/{elderlyId}/report")
    public ResponseResult<Map<String, Object>> uploadHealthReport(
            @Parameter(description = "老人ID", example = "1", required = true) 
            @PathVariable Long elderlyId,
            @Parameter(description = "报告文件,支持pdf/doc/docx/xls/xlsx格式,最大20MB", required = true) 
            @RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileStorageService.uploadFile(file, "report", elderlyId);
            Map<String, Object> result = createFileInfoMap(file, fileUrl);
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("上传健康报告失败,老人ID:{}", elderlyId, e);
            return ResponseResult.error(ERROR_UPLOAD_FAILED + e.getMessage());
        }
    }

    /**
     * 上传通用文档
     */
    @Operation(summary = "上传通用文档", description = "上传各类文档文件")
    @PostMapping("/document")
    public ResponseResult<Map<String, Object>> uploadDocument(
            @Parameter(description = "文档文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "关联ID(可选)") @RequestParam(required = false) Long relatedId) {
        try {
            String fileUrl = fileStorageService.uploadFile(file, "document", relatedId);
            Map<String, Object> result = createFileInfoMap(file, fileUrl);
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("上传文档失败", e);
            return ResponseResult.error(ERROR_UPLOAD_FAILED + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件", description = "根据文件URL删除文件,物理删除服务器上的文件")
    @DeleteMapping
    public ResponseResult<Void> deleteFile(
            @Parameter(description = "文件URL,完整的文件访问路径", 
                      example = "http://localhost:8080/uploads/photo/1/avatar.jpg",
                      required = true) 
            @RequestParam String fileUrl) {
        try {
            boolean success = fileStorageService.deleteFile(fileUrl);
            if (success) {
                return ResponseResult.success(null);
            } else {
                return ResponseResult.error("文件删除失败或文件不存在");
            }
        } catch (Exception e) {
            log.error("删除文件失败,URL:{}", fileUrl, e);
            return ResponseResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量上传文件
     */
    @Operation(summary = "批量上传文件", description = "一次上传多个文件")
    @PostMapping("/batch")
    public ResponseResult<Map<String, Object>> batchUploadFiles(
            @Parameter(description = "文件列表") @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "文件类型") @RequestParam String fileType,
            @Parameter(description = "关联ID(可选)") @RequestParam(required = false) Long relatedId) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            
            for (MultipartFile file : files) {
                if (uploadSingleFile(file, fileType, relatedId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            }
            
            result.put("total", files.length);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            
            return ResponseResult.success(result);
        } catch (Exception e) {
            log.error("批量上传文件失败", e);
            return ResponseResult.error("批量上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传单个文件(批量上传时使用)
     */
    private boolean uploadSingleFile(MultipartFile file, String fileType, Long relatedId) {
        try {
            fileStorageService.uploadFile(file, fileType, relatedId);
            return true;
        } catch (Exception e) {
            log.error("上传文件失败,文件名:{}", file.getOriginalFilename(), e);
            return false;
        }
    }
    
    /**
     * 创建文件信息Map
     */
    private Map<String, Object> createFileInfoMap(MultipartFile file, String fileUrl) {
        Map<String, Object> result = new HashMap<>();
        result.put(KEY_URL, fileUrl);
        result.put(KEY_FILE_NAME, file.getOriginalFilename());
        result.put(KEY_FILE_SIZE, file.getSize());
        return result;
    }
}
