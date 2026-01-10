package com.easyevents.notification_service.domain.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvidadoResponse {
    private String message;
    private boolean success;
    private String guestId;
}
