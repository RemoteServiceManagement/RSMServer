package com.rsm.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NewUserFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(NewUserDto.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewUserDto newUserDto = (NewUserDto) o;
        if (!newUserDto.getPasswordRepeat().equals(newUserDto.getPassword())) {
            errors.reject("wrong.repeat");
        }
    }
}
