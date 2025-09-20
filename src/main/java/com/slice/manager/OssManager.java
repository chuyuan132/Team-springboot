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
     * @param uploadPath 文件路径
     * @param multipartFile 上传的文件
     * @return 文件URL
     */
    public String putObject(String uploadPath, MultipartFile multipartFile) {
        try {
            // 检查bucket是否存在，如果不存在则创建
            boolean bucketExist = aliOssClient.doesBucketExist(ossConfig.getBucketName());
            if (!bucketExist) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossConfig.getBucketName());
                createBucketRequest.setCannedACL(CannedAccessControlList.Private);
                aliOssClient.createBucket(createBucketRequest);
                log.info("创建OSS Bucket: {}", ossConfig.getBucketName());
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                PutObjectResult result = aliOssClient.putObject(ossConfig.getBucketName(), uploadPath, inputStream);
                System.out.println(result);
                return String.format("https://%s.%s/%s", ossConfig.getBucketName(), ossConfig.getEndpoint(), uploadPath);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败: ");
        }
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