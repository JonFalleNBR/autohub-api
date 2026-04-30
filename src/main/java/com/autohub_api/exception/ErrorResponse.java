package com.autohub_api.exception;

import java.time.OffsetDateTime;

/**
 * DTO imutável que representa o corpo padronizado de toda resposta de erro da API.
 *
 * <p>Formato retornado em todos os erros:</p>
 * <pre>
 * {
 *   "timestamp": "2026-04-29T10:00:00Z",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Cliente não encontrado para o tenant informado",
 *   "path": "/api/v1/clientes/abc-123"
 * }
 * </pre>
 */
public record ErrorResponse(

        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path

) {}

