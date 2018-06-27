package com.rsm.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class NewUserDto {

    @NotEmpty(message = "{login.need}")
    private String login;

    @NotEmpty(message = "{password.need}")
    private String password;

    @NotEmpty(message = "{passwordRepeat.need}")
    private String passwordRepeat;

    private Long roleId;

    @NotEmpty(message = "{name.need}")
    private String firstName;

    @NotEmpty(message = "{lastname.need}")
    private String lastName;

    private String phone;

    @Email(message = "{valid.email}")
    @NotEmpty(message = "{email.meed}")
    private String email;
}
