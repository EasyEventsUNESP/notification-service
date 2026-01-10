package com.easyevents.notification_service.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class EmailRequest {
    private List<String> recipients;
    private String subject;
    private String body;
}
