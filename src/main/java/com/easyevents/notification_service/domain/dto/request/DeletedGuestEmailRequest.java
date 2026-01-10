package com.easyevents.notification_service.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DeletedGuestEmailRequest {
    private String eventTitle;
    private List<String> guestEmail;
    private String organizerEmail;
}

