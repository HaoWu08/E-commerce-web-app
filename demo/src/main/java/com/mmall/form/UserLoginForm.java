package com.mmall.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UserLoginForm {
    @NotEmpty(message = "account cannot be empty")
    private String loginName;
    @NotEmpty(message = "password cannot be empty")
    private String password;
}
