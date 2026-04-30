package com.autohub_api.model.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de saída com os dados completos de um registro de {@code ServiceHistory}.
 *
 * <p>Retornado nas operações de criação e consulta do histórico de serviços.
 * O {@code carId} permite navegar para os dados do veículo relacionado.</p>
 */
public record ServiceHistoryResponse(

        UUID id,
        UUID carId,
        UUID tenantId,
        String serviceType,
        String description,
        OffsetDateTime serviceDate,
        BigDecimal cost,
        Integer mileage,
        String technicianName,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {}

