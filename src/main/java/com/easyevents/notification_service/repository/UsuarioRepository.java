package com.easyevents.notification_service.repository;

import com.easyevents.notification_service.domain.model.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {

    Optional<UsuarioModel> findByEmail(String email);
}
