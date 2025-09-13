package com.slice.manager;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.slice.config.OssConfig;
import com.slice.enums.ErrorCode;
import com.slice.exception.BusinessException;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 阿里云oss存储服务
 * 提供文件上传、下载、删除等功能
 * @author zhangchuyuan
 */
@Component
@Slf4j
public class OssManager {

    @Resource
    private OssConfig ossConfig;

    @Resource
    private OSS aliOssClient;

    /**
     * 上传文件到OSS
     * @param key 文件路径
     * @param multipartFile 上传的文件
     * @return 文件URL
     */
    public String putObject(String key, MultipartFile multipartFile) {
        try {
            // 检查bucket是否存在，如果不存在则创建
            boolean bucketExist = aliOssClient.doesBucketExist(ossConfig.getBucketName());
            if (!bucketExist) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossConfig.getBucketName());
                createBucketRequest.setCannedACL(CannedAccessControlList.Private);
                aliOssClient.createBucket(createBucketRequest);
                log.info("创建OSS Bucket: {}", ossConfig.getBucketName());
            }
            
            // 上传文件
            try (InputStream inputStream = multipartFile.getInputStream()) {
                PutObjectResult result = aliOssClient.putObject(ossConfig.getBucketName(), key, inputStream);
                String fileUrl = String.format("https://%s.%s/%s", ossConfig.getBucketName(), ossConfig.getEndpoint(), key);
                log.info("文件上传成功: {}, 文件URL: {}, ETag: {}", multipartFile.getOriginalFilename(), fileUrl, result.getETag());
                return fileUrl;
            }
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 从OSS获取文件
     * @param url 文件URL
     * @return 文件输入流
     */
    public InputStream getObject(String url) {
        try {
            // 从URL中提取objectName
            String objectName = extractObjectName(url);
            log.info("从OSS获取文件，objectName: {}", objectName);
            
            // 获取文件
            GetObjectRequest getObjectRequest = new GetObjectRequest(ossConfig.getBucketName(), objectName);
            return aliOssClient.getObject(getObjectRequest).getObjectContent();
        } catch (OSSException e) {
            log.error("OSS服务异常: {}", e.getErrorMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件获取失败: " + e.getErrorMessage());
        } catch (ClientException e) {
            log.error("OSS客户端异常: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件获取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("文件获取失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 从OSS删除文件
     * @param url 文件URL
     * @return 是否删除成功
     */
    public boolean deleteObject(String url) {
        try {
            // 从URL中提取objectName
            String objectName = extractObjectName(url);
            log.info("从OSS删除文件，objectName: {}", objectName);
            
            // 删除文件
            aliOssClient.deleteObject(ossConfig.getBucketName(), objectName);
            log.info("文件删除成功，objectName: {}", objectName);
            return true;
        } catch (OSSException e) {
            log.error("OSS服务异常: {}", e.getErrorMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件删除失败: " + e.getErrorMessage());
        } catch (ClientException e) {
            log.error("OSS客户端异常: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件删除失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 从URL中提取objectName
     * @param url 文件URL
     * @return objectName
     */
    private String extractObjectName(String url) {
        // 假设URL格式为: https://bucketName.endpoint/objectName
        String prefix = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/";
        if (url.startsWith(prefix)) {
            return url.substring(prefix.length());
        }
        throw new IllegalArgumentException("Invalid OSS URL: " + url);
    }
    
    /**
     * 销毁OSS客户端，释放资源
     */
    @PreDestroy
    public void destroy() {
        if (aliOssClient != null) {
            aliOssClient.shutdown();
            log.info("OSS客户端已关闭");
        }
    }
}