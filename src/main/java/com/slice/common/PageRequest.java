package com.slice.common;

import lombok.Data;

@Data
public class PageRequest {
    private int pageSize = 10;

    private int pageNo = 1;
}
