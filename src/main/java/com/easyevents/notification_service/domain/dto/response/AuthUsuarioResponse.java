package com.easyevents.notification_service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUsuarioResponse {
    private String email;
    private String nome;
    private String responseMessage; // O campo que contém a senha temporária
}