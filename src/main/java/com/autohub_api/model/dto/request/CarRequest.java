package com.autohub_api.model.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * DTO de entrada para criação e atualização de um {@code Car}.
 *
 * <p>A placa ({@code plate}) deve ser única dentro do tenant —
 * validação enforçada na camada de Service via {@code CarRepository.existsByTenantIdAndPlate}.</p>
 *
 * <p>O {@code clienteId} vincula o veículo a um cliente existente do mesmo tenant.</p>
 *
 * <p><strong>TODO 6 — Remover {@code tenantId} deste DTO após implementar JWT.</strong><br>
 * Após o TODO 6, o {@code tenantId} será extraído automaticamente do token JWT
 * via {@code TenantContext.get()} na camada de Service, e este campo deve ser removido.</p>
 */
public record CarRequest(

        @NotNull(message = "Tenant é obrigatório")
        UUID tenantId,

        @NotNull(message = "Cliente é obrigatório")
        UUID clienteId,

        @NotBlank(message = "Placa é obrigatória")
        @Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
        String plate,

        @NotBlank(message = "Modelo é obrigatório")
        @Size(max = 120, message = "Modelo deve ter no máximo 120 caracteres")
        String model,

        @NotBlank(message = "Marca é obrigatória")
        @Size(max = 120, message = "Marca deve ter no máximo 120 caracteres")
        String brand,

        @NotBlank(message = "Cor é obrigatória")
        @Size(max = 50, message = "Cor deve ter no máximo 50 caracteres")
        String color,

        @NotNull(message = "Ano é obrigatório")
        @Min(value = 1900, message = "Ano deve ser maior ou igual a 1900")
        @Max(value = 2100, message = "Ano deve ser menor ou igual a 2100")
        Integer year

) {}


