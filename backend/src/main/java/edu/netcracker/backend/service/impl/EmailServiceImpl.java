package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@PropertySource("classpath:mail.properties")
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    private final ExecutorService emailExecutor;

    @Value("${mail.registration.subject}")
    private String registrationSubject;

    @Value("${mail.registration.text}")
    private String registrationText;

    @Value("${mail.registration.endPointUri}")
    private String registrationEndPointUri;

    @Value("${mail.password-recovery.endPointUri}")
    private String passwordRecoveryUri;

    @Value("${mail.password-recovery.subject}")
    private String passwordRecoverySubject;

    @Value("${mail.password-recovery.text}")
    private String passwordRecoveryText;

    @Value("${mail.source.url}")
    private String sourceUrl;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender, ExecutorService emailExecutor) {
        this.emailSender = emailSender;
        this.emailExecutor = emailExecutor;
    }

    @Override
    public void sendRegistrationMessage(String to, String token) {
        log.debug("EmailServiceImpl.sendRegistrationMessage(String to, String token) was invoked");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(getSubject(registrationSubject, sourceUrl));
        message.setText(registrationText + " : " + sourceUrl + registrationEndPointUri + "?token=" + token);

        sendSimpleMessage(message, to);
    }

    @Override
    public void sendPasswordRecoveryMessage(String to, String username, String password) {
        log.debug(
                "EmailServiceImpl.sendPasswordRecoveryMessage(String to, String username, String password) was invoked");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(getSubject(passwordRecoverySubject, username));
        message.setText(passwordRecoveryText + " : " + username + ". New Password " + password);

        sendSimpleMessage(message, to);
    }

    private void sendSimpleMessage(SimpleMailMessage message, String to) {
        log.debug("EmailServiceImpl.sendSimpleMessage(SimpleMailMessage message, String to) was invoked");
        message.setFrom("nc-project@mail.ru");
        message.setTo(to);

        emailExecutor.execute(() -> emailSender.send(message));
        log.debug("Email was sent to email address {}", to);
    }

    private String getSubject(String subject, String line) {
        return subject + " : " + line;
    }

}