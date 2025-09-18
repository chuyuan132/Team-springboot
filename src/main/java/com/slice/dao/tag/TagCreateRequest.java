package com.slice.dao.tag;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagCreateRequest implements Serializable {

    private static final long serialVersionUID = 1463672933474333769L;

    private String name;

    private long parentId = 0L;

}
