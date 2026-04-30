package com.autohub_api.model.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * DTO de entrada para criação e atualização de um {@code Cliente}.
 *
 * <p>O CPF deve ser único dentro do tenant — validação enforçada na camada de Service.</p>
 */
public record ClienteRequest(

        @NotNull(message = "Tenant é obrigatório")
        UUID tenantId,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(max = 14, message = "CPF deve ter no máximo 14 caracteres")
        String cpf,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "E-mail deve ter no máximo 120 caracteres")
        String email,

        String endereco

) {}

