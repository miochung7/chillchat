package com.example.emojify.registration;

import com.example.emojify.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "signup")
@AllArgsConstructor // generates a constructor with 1 parameter for each field in your class.
public class RegistrationController {

    // reference to the RegistrationService
    private final RegistrationService registrationService;


    @PostMapping( produces = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    // method to register person
    public String register(@Valid AppUser appUser, BindingResult result, RegistrationRequest request, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        registrationService.register(request);
        return "redirect:/signup?success";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return "redirect:/signup?tokenSuccess";
    }
}
