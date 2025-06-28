package com.easyevents.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class NotificationController {

//    private final AuthService authService;

    @GetMapping
    public String endpointTest() {

        return "ERALDO VS MARIO!";
    }
}
