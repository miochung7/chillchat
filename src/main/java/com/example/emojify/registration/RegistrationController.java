package com.example.emojify.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor // generates a constructor with 1 parameter for each field in your class.
public class RegistrationController {

    // reference to the RegistrationService
    private final RegistrationService registrationService;

    @PostMapping
    // method to register person, @RequestBody takes RegistrationRequest
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
