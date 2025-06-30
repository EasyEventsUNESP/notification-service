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
        if (eventEmailRequest.getEmailList() == null || eventEmailRequest.getEmailList().isEmpty()) {
            logger.warn("A lista de emails está vazia ou nula.");
            return ResponseEntity.badRequest().body("A lista de emails está vazia ou nula.");
        }

        if (eventEmailRequest.getEventoId() == null || eventEmailRequest.getEventoId().isEmpty()) {
            logger.warn("O ID do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O ID do evento está vazio ou nulo.");
        }

        if (eventEmailRequest.getEventTitle() == null || eventEmailRequest.getEventTitle().isEmpty()) {
            logger.warn("O título do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O título do evento está vazio ou nulo.");
        }

        if (eventEmailRequest.getOrganizerName() == null || eventEmailRequest.getOrganizerName().isEmpty()) {
            logger.warn("O nome do organizador está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O nome do organizador está vazio ou nulo.");
        }

        if (eventEmailRequest.getEventDateTime() == null || eventEmailRequest.getEventDateTime().isEmpty()) {
            logger.warn("A data e hora do evento estão vazias ou nulas.");
            return ResponseEntity.badRequest().body("A data e hora do evento estão vazias ou nulas.");
        }

        if (eventEmailRequest.getEventLocation() == null || eventEmailRequest.getEventLocation().isEmpty()) {
            logger.warn("O local do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O local do evento está vazio ou nulo.");
        }
        String subject = "Convite para " + eventEmailRequest.getEventTitle();
        String eventoId = eventEmailRequest.getEventoId();
        String eventTitle = eventEmailRequest.getEventTitle();
        String organizerName = eventEmailRequest.getOrganizerName();
        String eventDateTime = eventEmailRequest.getEventDateTime();
        String eventLocation = eventEmailRequest.getEventLocation();

        for (String recipient : eventEmailRequest.getEmailList()) {
            String body = String.format(
                    """
                    Olá,
                
                    Você foi convidado para participar do evento "%s", organizado por %s.
                
                    Detalhes do evento:
                    Data e Hora: %s
                    Local: %s
                
                    Por favor, confirme ou recuse sua presença ao clickar nos links abaixo:
                
                    Confirmar presença: http://localhost:3002/guest/confirmar/%s/%s
                    Negar presença: http://localhost:3002/guest/negar/%s/%s
                
                    Aguardamos ansiosamente sua resposta!""",
                    eventTitle, organizerName, eventDateTime, eventLocation, eventoId, recipient, eventoId, recipient
            );
            sendEmail(recipient, subject, body);
        }
        return ResponseEntity.ok("Emails enviados com sucesso para os convidados!");
    }
}