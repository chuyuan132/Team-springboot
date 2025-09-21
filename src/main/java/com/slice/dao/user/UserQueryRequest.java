package com.slice.dao.user;
import com.slice.common.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class UserQueryRequest extends PageRequest {

    private List<String> tagList;

    private String username;

}
