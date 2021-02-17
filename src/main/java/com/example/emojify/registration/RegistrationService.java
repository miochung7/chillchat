package com.example.emojify.registration;

import com.example.emojify.appuser.AppUser;
import com.example.emojify.appuser.AppUserRole;
import com.example.emojify.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    // checks if email is valid
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator
                .test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}


