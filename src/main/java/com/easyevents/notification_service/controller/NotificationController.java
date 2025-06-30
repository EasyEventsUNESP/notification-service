package com.easyevents.notification_service.controller;

import com.easyevents.notification_service.domain.dto.request.*;
import com.easyevents.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    
    private final NotificationService notificationService;

    @PostMapping("/enviar")
    public ResponseEntity<String> sendEmails(@RequestBody EmailRequest request) {
        return notificationService.sendEmails(request);
    }

    //
    @PostMapping("/enviar/criacao-evento")
    public ResponseEntity<String> sendEventEmails(@RequestBody EventEmailRequest request) {
        return notificationService.sendEventEmails(request);
    }

    @PostMapping("enviar/delecao-convidado")
    public ResponseEntity<String> sendDeletedGuestEmail(@RequestBody DeletedGuestEmailRequest request) {
        return notificationService.sendDeletedGuestEmail(request);
    }

    @PostMapping("enviar/senha-reset/{email}")
    public ResponseEntity<String> sendPasswordReset(@PathVariable String email) {
        return notificationService.sendPasswordReset(email);
    }

}