package com.slice.dao.tag;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1463672933474333769L;

    private long id;

    private String name;

    private int parentId = 0;

}
