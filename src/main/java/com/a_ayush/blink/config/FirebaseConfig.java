package com.a_ayush.blink.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            String configPath =
                    System.getenv("SERVICE_ACCOUNT_KEY");

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(
                                    GoogleCredentials.fromStream(
                                            new ByteArrayInputStream(
                                                    configPath.getBytes(
                                                            StandardCharsets.UTF_8
                                                    )
                                            )
                                    )
                            )
                            .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
