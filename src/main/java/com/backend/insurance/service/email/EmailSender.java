package com.backend.insurance.service.email;

public interface EmailSender {

    void send(String to, String email);
}
