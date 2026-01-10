package com.easyevents.notification_service.service;

import com.easyevents.notification_service.domain.dto.request.DeletedGuestEmailRequest;
import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import com.easyevents.notification_service.domain.dto.request.EventEmailRequest;
import com.easyevents.notification_service.domain.dto.response.AuthUsuarioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender mailSender;
    private final WebClient webClient;

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
        log.info("Email sent successfully to {}", toEmail);
    }

    public ResponseEntity<String> sendEventEmails(EventEmailRequest eventEmailRequest) {
        if (eventEmailRequest.getGuestList() == null || eventEmailRequest.getGuestList().isEmpty()) {
            log.warn("A lista de emails está vazia ou nula.");
            return ResponseEntity.badRequest().body("A lista de emails está vazia ou nula.");
        }

        if (eventEmailRequest.getEventoId() == null || eventEmailRequest.getEventoId().isEmpty()) {
            log.warn("O ID do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O ID do evento está vazio ou nulo.");
        }

        if (eventEmailRequest.getEventTitle() == null || eventEmailRequest.getEventTitle().isEmpty()) {
            log.warn("O título do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O título do evento está vazio ou nulo.");
        }

        if (eventEmailRequest.getOrganizerName() == null || eventEmailRequest.getOrganizerName().isEmpty()) {
            log.warn("O nome do organizador está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O nome do organizador está vazio ou nulo.");
        }

        if (eventEmailRequest.getEventDateTime() == null || eventEmailRequest.getEventDateTime().isEmpty()) {
            log.warn("A data e hora do evento estão vazias ou nulas.");
            return ResponseEntity.badRequest().body("A data e hora do evento estão vazias ou nulas.");
        }

        if (eventEmailRequest.getEventLocation() == null || eventEmailRequest.getEventLocation().isEmpty()) {
            log.warn("O local do evento está vazio ou nulo.");
            return ResponseEntity.badRequest().body("O local do evento está vazio ou nulo.");
        }

        String subject = "Convite para " + eventEmailRequest.getEventTitle();
        String eventoId = eventEmailRequest.getEventoId();
        String eventTitle = eventEmailRequest.getEventTitle();
        String organizerName = eventEmailRequest.getOrganizerName();
        String eventDateTime = eventEmailRequest.getEventDateTime();
        String eventLocation = eventEmailRequest.getEventLocation();

        for (String recipient : eventEmailRequest.getGuestList()) {
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

    public ResponseEntity<String> sendDeletedGuestEmail(DeletedGuestEmailRequest request) {
        try {
            if (request.getEventTitle() == null || request.getEventTitle().isEmpty()) {
                log.warn("O título do evento está vazio ou nulo.");
                return ResponseEntity.badRequest().body("O título do evento está vazio ou nulo.");
            }

            if (request.getGuestEmail() == null || request.getGuestEmail().isEmpty()) {
                log.warn("A lista de emails dos convidados está vazia ou nula.");
                return ResponseEntity.badRequest().body("A lista de emails dos convidados está vazia ou nula.");
            }

            if (request.getOrganizerEmail() == null || request.getOrganizerEmail().isEmpty()) {
                log.warn("O email do organizador está vazio ou nulo.");
                return ResponseEntity.badRequest().body("O email do organizador está vazio ou nulo.");
            }

            String subject = "Você foi removido do evento: " + request.getEventTitle();
            String body = String.format("""
            Olá,
            
            Este email é para informar que você foi removido do evento "%s".
            
            Caso queira discutir esta decisão, por favor, entre em contato com o organizador do evento através do email:
            %s
            
            Atenciosamente,
            Equipe Easy Events
            """,
                    request.getEventTitle(),
                    request.getOrganizerEmail());

            for (String guestEmail : request.getGuestEmail()) {
                sendEmail(guestEmail, subject, body);
            }

            String successMessage = String.format(
                    "Emails de remoção enviados com sucesso para %d convidado(s)",
                    request.getGuestEmail().size()
            );

            return ResponseEntity.ok(successMessage);

        } catch (Exception e) {
            log.error("Erro ao enviar emails de remoção: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar emails de remoção: " + e.getMessage());
        }
    }

    public ResponseEntity<String> sendPasswordReset(String email) {
        try {
            log.info("Iniciando processo de recuperação de senha para: {}", email);
            
            AuthUsuarioResponse authResponse = webClient
                    .get()
                    .uri("/senha-temp/" + email)
                    .retrieve()
                    .bodyToMono(AuthUsuarioResponse.class) // <<< MUDANÇA PRINCIPAL AQUI
                    .block(); // .block() é usado para esperar a resposta síncronamente

            // Verificação de nulidade da resposta
            if (authResponse == null) {
                log.error("Resposta nula do serviço de autenticação para o email: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao gerar nova senha: resposta nula do serviço");
            }

            String senhaTemporaria = authResponse.getResponseMessage();

            if (senhaTemporaria == null || senhaTemporaria.isEmpty()) {
                log.error("Senha temporária não gerada para o email: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao gerar nova senha: senha não gerada");
            }

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setRecipients(List.of(email));
            emailRequest.setSubject("Easy Events - Recuperação de Senha");

            // CORREÇÃO: Obter o nome do campo correto
            String nomeUsuario = authResponse.getNome() != null ? authResponse.getNome() : "Usuário";

            // CORREÇÃO: Usar a variável correta com a senha
            emailRequest.setBody(String.format("""
                Olá %s,
                
                Recebemos sua solicitação de recuperação de senha.
                
                Aqui está sua nova senha temporária:
                
                %s
                
                Por questões de segurança, recomendamos que você:
                1. Acesse o sistema imediatamente usando esta senha
                2. Altere para uma nova senha de sua preferência
                
                Acesse o sistema através do link: http://localhost:5173/login
                
                Esta senha temporária expirará em 24 horas por motivos de segurança.
                
                Se você não solicitou esta recuperação de senha, por favor, entre em contato
                com nossa equipe de suporte imediatamente.
                
                Atenciosamente,
                Equipe Easy Events
                """,
                    nomeUsuario,
                    senhaTemporaria)); // <<< MUDANÇA AQUI

            sendEmails(emailRequest);

            log.info("Senha temporária enviada com sucesso para: {}", email);
            return ResponseEntity.ok("Email com a senha temporária enviado com sucesso!");

        } catch (WebClientResponseException e) {
            log.error("Erro na comunicação com o serviço de autenticação: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode())
                    .body("Erro ao processar a recuperação de senha: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Erro inesperado ao processar recuperação de senha: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar a recuperação de senha: " + e.getMessage());
        }
    }
}