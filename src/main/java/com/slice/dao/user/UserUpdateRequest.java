package com.slice.dao.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserUpdateRequest implements Serializable {
    private Long id;
    private String password;
    private String username;
    private String avatar;
    private String profile;
    private String email;
    private List<String> tagList;
}
