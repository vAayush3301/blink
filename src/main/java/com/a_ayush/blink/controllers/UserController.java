package com.a_ayush.blink.controllers;

import com.google.firebase.auth.FirebaseToken;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/users/me")
    public Map<String, Object> currentUser(
            Authentication authentication
    ) {

        FirebaseToken token =
                (FirebaseToken)
                        authentication.getPrincipal();

        return Map.of(
                "uid", token.getUid(),
                "email", token.getEmail(),
                "name", token.getName()
        );
    }
}