package com.vti.todo.dto.request;

import com.vti.todo.validation.Password;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {
    @NotNull
    @NotBlank
    private String otp;

    @NotNull
    @Password
    private String password;

    @Email
    @NotNull
    private String email;

}
