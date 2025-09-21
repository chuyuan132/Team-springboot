package com.slice.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageRequest {
    @JsonProperty(value = "page_size")
    public Long pageSize;
    @JsonProperty(value = "page_no")
    public Long pageNo;
}
