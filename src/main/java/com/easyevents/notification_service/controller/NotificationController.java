package com.easyevents.notification_service.controller;

import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import com.easyevents.notification_service.domain.dto.request.EventEmailRequest;
import com.easyevents.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    
    private final NotificationService notificationService;

    @PostMapping("/enviar")
    public ResponseEntity<String> send(@RequestBody EmailRequest emailRequest) {
        return notificationService.sendEmails(emailRequest);
    }

    @PostMapping("/enviar/criacao-evento")
    public ResponseEntity<String> sendEventEmails(@RequestBody EventEmailRequest eventEmailRequest) {
        return notificationService.sendEventEmails(eventEmailRequest);
    }
}