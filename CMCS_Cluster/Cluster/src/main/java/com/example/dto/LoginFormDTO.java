package com.example.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    private String phone;
    private String email;
    private String code;
    private String password;
}
