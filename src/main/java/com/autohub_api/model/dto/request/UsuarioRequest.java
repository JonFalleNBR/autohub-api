package com.autohub_api.model.dto.request;

import com.autohub_api.model.enums.UserRole;
import jakarta.validation.constraints.*;

import java.util.UUID;

/**
 * DTO de entrada para criação de um {@code Usuario}.
 *
 * <p>O e-mail deve ser único dentro do tenant — validação enforçada na camada de Service
 * via {@code UsuarioRepository.existsByTenantIdAndEmail}.</p>
 *
 * <p><strong>Atenção:</strong> a senha recebida aqui ainda está em texto puro.
 * A camada de Service é responsável por aplicar o hash BCrypt antes de persistir
 * (implementação pendente no TODO 5 — Spring Security).</p>
 *
 * <p>Roles disponíveis:</p>
 * <ul>
 *   <li>{@code LEITOR} — acesso somente leitura</li>
 *   <li>{@code ESCRITOR} — acesso completo de leitura e escrita</li>
 * </ul>
 */
public record UsuarioRequest(

        @NotNull(message = "Tenant é obrigatório")
        UUID tenantId,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "E-mail deve ter no máximo 120 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String senha,

        @NotNull(message = "Role é obrigatória")
        UserRole role

) {}

