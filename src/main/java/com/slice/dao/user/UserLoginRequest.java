package com.slice.dao.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8817109689618333401L;

    private String phone;

    private String password;


}
