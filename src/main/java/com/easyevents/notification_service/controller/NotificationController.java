package com.easyevents.notification_service.controller;

import com.easyevents.notification_service.domain.dto.request.EmailRequest;
import com.easyevents.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private final NotificationService notificationService;

    @PostMapping("/enviar")
    public ResponseEntity<String> send(@RequestBody EmailRequest emailRequest) {
        notificationService.sendEmails(emailRequest);
        return ResponseEntity.ok("Emails enviados com sucesso!");
    }
}