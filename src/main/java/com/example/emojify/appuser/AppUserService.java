package com.example.emojify.appuser;


import com.example.emojify.email.EmailSender;
import com.example.emojify.registration.RegistrationService;
import com.example.emojify.registration.token.ConfirmationToken;
import com.example.emojify.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

// Implements an interface for Spring Security - to find users once logged in

@Service // marks as service provider that provides business logic
@AllArgsConstructor // generates a constructor with 1 parameter for each field in your class.
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";


    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    // If email doesn't exist throws error
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    // checks if email already exists
    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
//            if (appUser.getEnabled() == false) {
//                String link = "http://localhost:8080/api/v1/registration/confirm?token=" + appUser;
//                emailSender.send(
//                        appUser.getEmail(),
//                        registrationService.buildEmail(appUser.getFirstName(), link));

            // TODO check if its same user
            // TODO if email not confirmed send confirmation email

            // OTHERWISE this error will be thrown
            String userEmail = appUser.getEmail();
            throw new IllegalStateException(userEmail + " has already been taken");
        }

        // Password encoded is here
        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        // Saves user to db
        appUserRepository.save(appUser);

        // 128-bit long unique value
        String token = UUID.randomUUID().toString();

        // Sends confirmation token to save user
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        // TODO: SEND EMAIL

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    @Transactional
    public void updateAppUser(Long id,
                              String firstName,
                              String lastName,
                              String email) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + id + " does not exist"));


                if (firstName != null &&
                firstName.length() > 0 &&
                !Objects.equals(appUser.getFirstName(), firstName)) {
                    appUser.setFirstName(firstName);
        }
        if (lastName != null &&
                lastName.length() > 0 &&
                !Objects.equals(appUser.getLastName(), lastName)) {
            appUser.setLastName(lastName);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(appUser.getEmail(), email)) {
            Optional<AppUser> appUserOptional = appUserRepository
                    .findByEmail(email);
            if (appUserOptional.isPresent()) {
                throw new IllegalStateException("email already taken");
            }
            appUser.setEmail(email);
        }
        appUserRepository.save(appUser);
    }
}
