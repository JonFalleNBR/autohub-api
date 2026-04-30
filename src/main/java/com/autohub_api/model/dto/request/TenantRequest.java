package com.autohub_api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação e atualização de um {@code Tenant}.
 *
 * <p>Um Tenant representa uma oficina/empresa cadastrada no sistema.
 * O {@code slug} é o identificador público de URL (ex: "oficina-sao-paulo")
 * e deve ser único em todo o sistema.</p>
 */
public record TenantRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String name,

        @NotBlank(message = "Slug é obrigatório")
        @Size(max = 60, message = "Slug deve ter no máximo 60 caracteres")
        String slug

) {}

