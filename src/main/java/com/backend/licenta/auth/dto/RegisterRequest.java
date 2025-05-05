package com.backend.licenta.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
