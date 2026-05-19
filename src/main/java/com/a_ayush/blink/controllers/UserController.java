package com.a_ayush.blink.controllers;

import com.a_ayush.blink.config.FirebaseConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseToken;

import com.google.firebase.cloud.FirestoreClient;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

        Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> data = new HashMap<>();
        data.put("name", token.getName());
        data.put("uid", token.getUid());
        data.put("email", token.getEmail());

        ApiFuture<WriteResult> result = db.collection("users/user_data").document(token.getEmail()).set(data);

        return data;
    }
}