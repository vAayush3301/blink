package com.a_ayush.blink.controllers;

import com.a_ayush.blink.CodeGenerator;
import com.a_ayush.blink.models.TextUpload;
import com.google.firebase.database.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
public class TextController {

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody String text) throws ExecutionException, InterruptedException {
        String code;

        DatabaseReference root = FirebaseDatabase.getInstance()
                .getReference("texts");

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
}
