package com.easyevents.notification_service.domain.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class EventEmailRequest {
    private String eventoId;
    private String eventTitle;
    private String organizerName;
    private String eventDateTime; // Format: "dd/MM/yyyy HH:mm"
    private String eventLocation;
    private List<String> emailList;
}
