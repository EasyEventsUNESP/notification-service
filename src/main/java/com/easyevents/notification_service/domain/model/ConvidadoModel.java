package com.easyevents.notification_service.domain.model;

import com.easyevents.notification_service.domain.enumerator.StatusConfirmacao;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "convidado")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConvidadoModel extends PessoaModel {

    private String eventoId;
    private com.easyevents.notification_service.domain.enumerator.StatusConfirmacao statusConfirmacao;

}
