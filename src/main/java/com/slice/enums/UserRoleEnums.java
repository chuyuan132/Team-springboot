package com.slice.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnums {
    ADMIN("管理员","admin"),
    BAN("封禁","ban");

    private final String text;
    private final String value;

    UserRoleEnums(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static UserRoleEnums getEnumByValue(String value) {
        if(value == null) {
            return null;
        }
        for(UserRoleEnums userRoleEnum: UserRoleEnums.values()) {
            if(userRoleEnum.getValue().equals(value)) {
                return userRoleEnum;
            }
        }
        return null;

    }


}
