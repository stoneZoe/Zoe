package com.woniu.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String status;
    private String salt;
    private String email;

}