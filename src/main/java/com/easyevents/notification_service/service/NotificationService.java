package com.easyevents.notification_service.service;

import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger((NotificationService.class));
    @Autowired
    private JavaMailSender mailSender;
    @Value("${email.sender}")
    private String senderEmail;

    public ResponseEntity<String> sendEmails(EmailRequest emailRequest) {
        for (String recipient : emailRequest.getRecipients()) {
            sendEmail(recipient, emailRequest.getSubject(), emailRequest.getBody());
        }
        return ResponseEntity.ok("Emails enviados com sucesso!");
    }

    private void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        logger.info("Email sent successfully to {}", toEmail);
    }
}