package com.demo.model.dto;

import lombok.Data;

@Data
public class UserDto {

    private Integer id;

    private String email;

    private String username;

    private String password;

    private String fullName;

    private String birthdayStr;

    private String genderStr;

    private String userCode;

    private String[] roleName;
}
