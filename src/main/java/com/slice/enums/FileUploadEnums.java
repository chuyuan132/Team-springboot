package com.slice.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传枚举
 */
@Getter
public enum FileUploadEnums {

    IMAGE_TYPE("image","图片",  10 * FileUploadEnums.MB, Arrays.asList("jpeg", "jpg", "svg", "png", "webp"));

    private final String type;
    private final String text;
    private final long maxSize;
    private final List<String> typeList;
    private static final long MB = 1024L * 1024;


    FileUploadEnums(String type,String text, Long maxSize, List<String> typeList) {
        this.text = text;
        this.type = type;
        this.maxSize = maxSize;
        this.typeList = typeList;

    }


    public static FileUploadEnums getFileUploadEnumByValue(String value) {
        for(FileUploadEnums fileUploadEnum : FileUploadEnums.values()) {
            if(fileUploadEnum.getType().equals(value)) {
                return fileUploadEnum;
            }
        }
        return null;
    }


}
