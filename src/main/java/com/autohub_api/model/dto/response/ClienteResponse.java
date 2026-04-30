package com.autohub_api.model.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de saída com os dados de um {@code Cliente}.
 *
 * <p>Retornado nas operações de criação, consulta e atualização.
 * O campo {@code tenantId} permite ao consumidor confirmar o isolamento multi-tenant.</p>
 */
public record ClienteResponse(

        UUID id,
        UUID tenantId,
        String nome,
        String cpf,
        String telefone,
        String email,
        String endereco,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {}

