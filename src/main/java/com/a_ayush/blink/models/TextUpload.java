package com.a_ayush.blink.models;

import java.time.Duration;

public class TextUpload {
    private String code;
    private String text;
    private long expiryTime;

    public TextUpload() {
    }

    public TextUpload(String code, String text) {
        this.code = code;
        this.text = text;
        this.expiryTime = System.currentTimeMillis() + Duration.ofHours(24).toMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expiryTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }
}