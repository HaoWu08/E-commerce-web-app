package com.mmall.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class UserRegisterForm {
    @NotEmpty(message = "account cannot be empty")
    private String loginName;
    @NotEmpty(message = "username cannot be empty")
    private String userName;
    @NotEmpty(message = "password cannot be empty")
    private String password;
    @NotNull(message = "gender cannot be empty")
    private Integer gender;
    @NotEmpty(message = "email cannot be empty")
    private String email;
    @NotEmpty(message = "mobile cannot be empty")
    private String mobile;
}
