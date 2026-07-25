package com.a_ayush.blink.controllers;

import com.a_ayush.blink.CodeGenerator;
import com.a_ayush.blink.models.TextUpload;
import com.google.firebase.database.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class TextController {
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("texts");

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody String text) throws ExecutionException, InterruptedException {
        String code;

        while (true) {
            code = CodeGenerator.generateCode();
            DatabaseReference ref = root.child(code);

            if (!exists(ref)) {
                TextUpload textUpload = new TextUpload(code, text);
                ref.setValueAsync(textUpload).get();
                return ResponseEntity.ok(code);
            }
        }
    }

    private boolean exists(DatabaseReference ref) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean exists = new AtomicBoolean(false);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                exists.set(snapshot.exists());
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        latch.await();
        return exists.get();
    }

    @GetMapping("/retrieve/{code}")
    public ResponseEntity<?> retrieve(@PathVariable String code) throws InterruptedException {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("texts")
                .child(code);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<TextUpload> uploadRef = new AtomicReference<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    uploadRef.set(snapshot.getValue(TextUpload.class));
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                latch.countDown();
            }
        });

        latch.await();

        TextUpload upload = uploadRef.get();

        if (upload == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", "Code not found"
                    ));
        }

        if (upload.isExpired()) {
            ref.removeValueAsync();

            return ResponseEntity.status(HttpStatus.GONE)
                    .body(Map.of(
                            "success", false,
                            "message", "Code has expired"
                    ));
        }

        return ResponseEntity.ok(upload);
    }
}
