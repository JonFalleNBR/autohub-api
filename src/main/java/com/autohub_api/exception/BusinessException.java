package com.autohub_api.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 *
 * <p>Capturada pelo {@code GlobalExceptionHandler} e convertida em resposta HTTP
 * {@code 409 Conflict} (padrão) ou {@code 422 Unprocessable Entity}, dependendo do contexto.</p>
 *
 * <p>Exemplos de uso na camada de Service:</p>
 * <pre>
 * // CPF duplicado no tenant → 409 Conflict
 * if (clienteRepository.existsByTenantIdAndCpf(tenantId, cpf)) {
 *     throw new BusinessException("CPF já cadastrado para este tenant");
 * }
 *
 * // Regra de negócio diferente → 422 Unprocessable Entity
 * throw new BusinessException("Serviço não pode ser registrado em data futura",
 *         HttpStatus.UNPROCESSABLE_ENTITY);
 * </pre>
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Cria a exceção com status padrão {@code 409 Conflict}.
     * Usado para violações de unicidade (CPF, placa, e-mail duplicados).
     *
     * @param message descrição da regra de negócio violada (em português)
     */
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    /**
     * Cria a exceção com status HTTP customizado.
     * Usado quando o erro não é um conflito, mas outra violação de regra.
     *
     * @param message descrição da regra de negócio violada (em português)
     * @param status  código HTTP adequado para o contexto do erro
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    /**
     * Retorna o status HTTP associado a esta exceção.
     *
     * @return status HTTP a ser retornado na resposta
     */
    public HttpStatus getStatus() {
        return status;
    }
}


