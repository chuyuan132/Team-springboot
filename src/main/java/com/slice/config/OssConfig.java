package com.slice.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 阿里云oss存储配置
 */
@Configuration
@Data
@ConfigurationProperties(prefix="oss")
public class OssConfig {

    private String endpoint;

    private String bucketName;
    
    private String accessKey;
    
    private String accessSecret;

    private Long expireTime;

    @Bean
    public OSS aliOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKey, accessSecret);
    }
}
