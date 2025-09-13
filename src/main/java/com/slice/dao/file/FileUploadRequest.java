package com.slice.dao.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadRequest  implements Serializable {

    private static final long serialVersionUID = 4592566508938296031L;
    private String biz;

}
