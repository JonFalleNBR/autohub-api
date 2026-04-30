package com.autohub_api.model.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de saída com os dados de um {@code Car}.
 *
 * <p>O {@code clienteId} permite consultar o dono do veículo.
 * O {@code tenantId} confirma o isolamento multi-tenant.</p>
 */
public record CarResponse(

        UUID id,
        UUID tenantId,
        UUID clienteId,
        String plate,
        String model,
        String brand,
        String color,
        Integer year,
        OffsetDateTime createdAt

) {}

