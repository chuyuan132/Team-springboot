package com.slice.controller;

import cn.hutool.core.io.FileUtil;
import com.slice.common.BaseResponse;
import com.slice.utils.ResultUtils;
import com.slice.dao.file.FileUploadRequest;
import com.slice.enums.ErrorCode;
import com.slice.enums.FileUploadEnums;
import com.slice.exception.BusinessException;
import com.slice.manager.OssManager;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 文件服务
 * @author zhangchuyuan
 */
@RestController
@RequestMapping("/file")
@Tag(name="文件服务")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Resource
    private OssManager ossManager;

    @PostMapping(value="/upload")
    @Operation(summary = "文件上传")
    public BaseResponse<String> fileUpload(@RequestParam("file") MultipartFile multipartFile, FileUploadRequest params) {
        if(multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"文件为空");
        }
        if(params == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(StringUtils.isAnyBlank(params.getBiz())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        FileUploadEnums fileUploadEnum = FileUploadEnums.getFileUploadEnumByValue(params.getBiz());

        // 1、是否支持该业务类型的文件处理
        if(fileUploadEnum == null) {
            throw new BusinessException(ErrorCode.NO_SUPPORT_ERROR,"无效业务类型");
        }

        // 2、根据业务类型区分，校验文件
        if(fileUploadEnum.getMaxSize() < multipartFile.getSize()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"文件大小超出限制");
        }

        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if(!fileUploadEnum.getTypeList().contains(suffix)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持该类型文件");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String format = LocalDate.now().format(formatter);
        String uploadKey = String.format("%s/%s", fileUploadEnum.getType(),format);
        
        try {
            String url = ossManager.putObject(uploadKey, multipartFile);
            log.info("文件上传成功，业务类型：{}", params.getBiz());
            return ResultUtils.success(url);
        } catch (Exception e) {
            log.error("文件上传失败：", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }

}
