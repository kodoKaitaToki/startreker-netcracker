package edu.netcracker.backend.service;

public interface EmailService {
    void sendRegistrationMessage(String to, String token);
    void sendPasswordRecoveryMessage(String to, String username, String password);
}
