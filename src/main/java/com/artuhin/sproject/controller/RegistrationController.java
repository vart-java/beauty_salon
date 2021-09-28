package com.artuhin.sproject.controller;

import com.artuhin.sproject.model.dto.RegistrationDto;
import com.artuhin.sproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        return "login";
    }

    @PostMapping("/registration")
    public String addUser(@RequestBody RegistrationDto registrationDto, Model model) {
        if (!userService.saveUser(registrationDto)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "login";
        }
        return "main";
    }
}

