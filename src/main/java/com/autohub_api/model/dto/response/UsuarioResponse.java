package com.autohub_api.model.dto.response;

import com.autohub_api.model.enums.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de saída com os dados públicos de um {@code Usuario}.
 *
 * <p><strong>Regra de segurança absoluta:</strong> o campo {@code senha} NUNCA é incluído
 * neste DTO — nem em texto puro, nem em hash. Qualquer alteração que exponha
 * a senha neste record deve ser rejeitada imediatamente.</p>
 *
 * <p>O campo {@code ativo} indica se o usuário pode fazer login no sistema.</p>
 */
public record UsuarioResponse(

        UUID id,
        UUID tenantId,
        String nome,
        String email,
        UserRole role,
        Boolean ativo,
        OffsetDateTime createdAt

) {}

