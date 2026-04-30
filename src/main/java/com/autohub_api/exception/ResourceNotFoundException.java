package com.autohub_api.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada no banco de dados.
 *
 * <p>Capturada pelo {@code GlobalExceptionHandler} e convertida em resposta HTTP {@code 404 Not Found}.</p>
 *
 * <p>Uso na camada de Service:</p>
 * <pre>
 * clienteRepository.findByIdAndTenantId(id, tenantId)
 *     .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Cria a exceção com a mensagem descritiva do recurso não encontrado.
     *
     * @param message descrição do que não foi encontrado (em português)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}


