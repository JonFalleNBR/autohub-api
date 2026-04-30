package com.autohub_api.model.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de entrada para registro de um serviço no histórico de um veículo.
 *
 * <p>Campos obrigatórios: {@code carId}, {@code tenantId}, {@code serviceType} e {@code serviceDate}.
 * Os demais campos são opcionais e enriquecem o registro do serviço.</p>
 *
 * <p>Exemplos de {@code serviceType}: "Troca de óleo", "Revisão completa", "Alinhamento e balanceamento".</p>
 */
public record ServiceHistoryRequest(

        @NotNull(message = "Carro é obrigatório")
        UUID carId,

        @NotNull(message = "Tenant é obrigatório")
        UUID tenantId,

        @NotBlank(message = "Tipo de serviço é obrigatório")
        @Size(max = 100, message = "Tipo de serviço deve ter no máximo 100 caracteres")
        String serviceType,

        String description,

        @NotNull(message = "Data do serviço é obrigatória")
        OffsetDateTime serviceDate,

        @DecimalMin(value = "0.0", inclusive = true, message = "Custo deve ser maior ou igual a 0")
        @Digits(integer = 8, fraction = 2, message = "Custo deve ter no máximo 8 dígitos inteiros e 2 casas decimais")
        BigDecimal cost,

        @Min(value = 0, message = "Quilometragem deve ser maior ou igual a 0")
        Integer mileage,

        @Size(max = 120, message = "Nome do técnico deve ter no máximo 120 caracteres")
        String technicianName,

        String notes

) {}

