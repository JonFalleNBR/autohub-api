package com.autohub_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

/**
 * Tratamento global de exceções da API.
 *
 * <p>Intercepta todas as exceções lançadas pelos Controllers e Services,
 * convertendo-as em respostas HTTP padronizadas no formato {@link ErrorResponse}.</p>
 *
 * <p>Hierarquia de tratamento:</p>
 * <ul>
 *   <li>{@link ResourceNotFoundException} → {@code 404 Not Found}</li>
 *   <li>{@link BusinessException} → {@code 409 Conflict} ou {@code 422 Unprocessable Entity}</li>
 *   <li>{@link MethodArgumentNotValidException} → {@code 400 Bad Request} (falhas de Bean Validation)</li>
 *   <li>{@link Exception} → {@code 500 Internal Server Error} (fallback genérico)</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata {@link ResourceNotFoundException} — entidade não encontrada.
     * Retorna {@code 404 Not Found}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Trata {@link BusinessException} — regra de negócio violada.
     * Retorna o status HTTP definido na própria exceção (409 ou 422).
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getStatus(), ex.getMessage(), request);
    }

    /**
     * Trata falhas de Bean Validation ({@code @Valid}, {@code @NotBlank}, {@code @NotNull}, etc.).
     * Retorna {@code 400 Bad Request} com a lista de campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    /**
     * Fallback genérico — captura qualquer exceção não tratada acima.
     * Retorna {@code 500 Internal Server Error} sem expor detalhes internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                request
        );
    }

    // ──────────────────────────────────────────────
    // Métodos privados de suporte
    // ──────────────────────────────────────────────

    /**
     * Monta o {@link ErrorResponse} padronizado e o encapsula em um {@link ResponseEntity}.
     */
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }
}

