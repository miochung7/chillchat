package com.example.emojify.controller;

import com.example.emojify.appuser.AppUser;
import com.example.emojify.appuser.AppUserRepository;
import com.example.emojify.appuser.AppUserService;
import com.example.emojify.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class TemplateController {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @GetMapping("/signup")
    public String getSignup(AppUser appUser) {
        return "signup";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/edit")
    public String showUpdateForm(Principal principal, Model model) {
        AppUser appUser = appUserRepository.findByEmail(principal.getName()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + principal.getName()));
        model.addAttribute("appUser", appUser);
        return "update-user";
    }


    @PostMapping("/update")
    public String updateUser( @Valid AppUser appUser, BindingResult result) {
        if (result.hasErrors()) {
            return "update-user";
        }

        appUserService.updateAppUser(appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail());

        return "redirect:/";
    }


    @GetMapping("/chillchat")
    public String getSpotify() {
        return "chillchat";
    }







}
