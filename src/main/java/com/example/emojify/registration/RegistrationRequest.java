package com.example.emojify.registration;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
// When client sends req, we want to capture these things
public class RegistrationRequest {

    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;
}
