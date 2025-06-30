package com.easyevents.notification_service.service;

import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import com.easyevents.notification_service.domain.dto.request.EventEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseEntity<String> sendEventEmails(EventEmailRequest eventEmailRequest) {
        String subject = "Convite para " + eventEmailRequest.getEventTitle();
        String eventoId = eventEmailRequest.getEventoId();
        String eventTitle = eventEmailRequest.getEventTitle();
        String organizerName = eventEmailRequest.getOrganizerName();
        String eventDateTime = eventEmailRequest.getEventDateTime();
        String eventLocation = eventEmailRequest.getEventLocation();

        for (String recipient : eventEmailRequest.getEmailList()) {
            String body = String.format(
                    "Olá,\n\nVocê foi convidado para participar do evento \"%s\", organizado por %s.\n\n" +
                            "Detalhes do evento:\n" +
                            "Data e Hora: %s\n" +
                            "Local: %s\n\n" +
                            "Por favor, confirme ou recuse sua presença ao clickar nos links abaixo:\n\n" +
                            "Confirmar presença: http://localhost:3002/guest/confirmar/%s/%s\n" +
                            "Negar presença: http://localhost:3002/guest/negar/%s/%s\n\n" +
                            "Aguardamos ansiosamente sua resposta!",
                    eventTitle, organizerName, eventDateTime, eventLocation, eventoId, recipient, eventoId, recipient
            );
            sendEmail(recipient, subject, body);
        }
        return ResponseEntity.ok("Emails enviados com sucesso para os convidados!");
    }
}