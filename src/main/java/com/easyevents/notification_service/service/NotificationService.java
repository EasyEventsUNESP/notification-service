package com.easyevents.notification_service.service;

import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmails(EmailRequest emailRequest) {
        for (String recipient : emailRequest.getRecipients()) {
            sendEmail(recipient, emailRequest.getSubject(), emailRequest.getBody());
        }
    }

    private void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("felipemelchior112@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Email sent successfully to " + toEmail);
    }
}