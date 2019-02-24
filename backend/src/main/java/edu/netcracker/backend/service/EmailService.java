package edu.netcracker.backend.service;

public interface EmailService {
    void sendRegistrationMessage(String to, String contextPath, String token);
    void sendPasswordRecoveryMessage(String to, String username, String password);
}
