package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@PropertySource("classpath:mail.properties")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ExecutorService emailExecutor;

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

    @Override
    public void sendRegistrationMessage(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(getSubject(registrationSubject, sourceUrl));
        message.setText(registrationText + " : " + sourceUrl + registrationEndPointUri + "?token=" + token);

        sendSimpleMessage(message, to);
    }

    @Override
    public void sendPasswordRecoveryMessage(String to, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(getSubject(passwordRecoverySubject, username));
        message.setText(passwordRecoveryText + " : " + username + ". New Password " + password);

        sendSimpleMessage(message, to);
    }

    private void sendSimpleMessage(SimpleMailMessage message, String to) {
        message.setFrom("nc-project@mail.ru");
        message.setTo(to);

        emailExecutor.execute(() -> emailSender.send(message));
    }

    private String getSubject(String subject, String line) {
        return subject +
                " : " +
                line;
    }

}