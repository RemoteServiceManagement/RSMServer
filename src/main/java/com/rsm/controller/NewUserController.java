package com.rsm.controller;

import com.rsm.role.RoleService;
import com.rsm.user.NewUserDto;
import com.rsm.user.NewUserFormValidator;
import com.rsm.user.User;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/newUser")
@RequiredArgsConstructor
public class NewUserController {

    private final NewUserFormValidator newUserFormValidator;
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public String get(Model model) {
        model.addAttribute("newUser", new NewUserDto());
        model.addAttribute("roles", roleService.findAll());
        return "/user/newUser";
    }

    @PostMapping
    public String post(@ModelAttribute(name = "newUser") @Valid NewUserDto newUser,
                       BindingResult result,
                       Errors errors,
                       Model model) {
        newUserFormValidator.validate(newUser, errors);
        if (result.hasErrors()) {
            model.addAttribute("newUser", newUser);
            model.addAttribute("roles", roleService.findAll());
            return "/user/newUser";
        }
        User user = userService.createByNewUserDto(newUser);
        model.addAttribute("user", user);
        return "userDetails";
    }
}
