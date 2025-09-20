package com.slice.vo.tag;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TagQueryVo implements Serializable {

    private static final long serialVersionUID = 3829991449264251652L;

    private String name;
    private  Long parentId;
    private List<TagQueryVo> children;

}
