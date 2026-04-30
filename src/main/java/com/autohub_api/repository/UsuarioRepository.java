package com.autohub_api.repository;

import com.autohub_api.model.entity.Usuario;
import com.autohub_api.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository responsável pelas operações de persistência da entidade {@link Usuario}.
 *
 * <p>Um {@code Usuario} é o operador que acessa o sistema (ex: mecânico, recepcionista, gerente).
 * Cada usuário pertence a um único {@link com.autohub_api.model.entity.Tenant}.</p>
 *
 * <p>Roles disponíveis:</p>
 * <ul>
 *   <li>{@code LEITOR} — acesso somente leitura</li>
 *   <li>{@code ESCRITOR} — acesso completo de leitura e escrita</li>
 * </ul>
 *
 * <p>O e-mail é único dentro de cada tenant: dois tenants diferentes podem ter
 * um usuário com o mesmo e-mail, mas dentro do mesmo tenant isso é proibido.</p>
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // ──────────────────────────────────────────────
    // Busca por e-mail (autenticação e validação)
    // ──────────────────────────────────────────────

    /**
     * Busca um usuário pelo tenant e e-mail.
     * Principal método usado no processo de autenticação (login).
     */
    Optional<Usuario> findByTenantIdAndEmail(UUID tenantId, String email);

    /**
     * Verifica se já existe um usuário com o e-mail informado no tenant.
     * Usado para validar unicidade antes de criar ou atualizar um usuário.
     */
    boolean existsByTenantIdAndEmail(UUID tenantId, String email);

    // ──────────────────────────────────────────────
    // Listagem por tenant
    // ──────────────────────────────────────────────

    /**
     * Retorna todos os usuários de um tenant.
     */
    List<Usuario> findByTenantId(UUID tenantId);

    /**
     * Retorna todos os usuários ATIVOS de um tenant.
     * Útil para listar quem pode fazer login no sistema.
     */
    List<Usuario> findByTenantIdAndAtivoTrue(UUID tenantId);

    /**
     * Retorna todos os usuários INATIVOS de um tenant.
     * Útil para auditoria ou reativação de contas.
     */
    List<Usuario> findByTenantIdAndAtivoFalse(UUID tenantId);

    // ──────────────────────────────────────────────
    // Filtro por role
    // ──────────────────────────────────────────────

    /**
     * Retorna todos os usuários de um tenant filtrados por role.
     * Ex: buscar todos os {@code ESCRITOR} de uma oficina.
     */
    List<Usuario> findByTenantIdAndRole(UUID tenantId, UserRole role);

    /**
     * Retorna usuários ativos de um tenant com uma role específica.
     * Combinação dos filtros de ativo e role para uso em autorização.
     */
    List<Usuario> findByTenantIdAndRoleAndAtivoTrue(UUID tenantId, UserRole role);

    // ──────────────────────────────────────────────
    // Verificações de existência
    // ──────────────────────────────────────────────

    /**
     * Busca um usuário pelo ID garantindo que ele pertence ao tenant informado.
     * Método principal para GET por ID na camada de Service — evita que um tenant
     * acesse dados de outro tenant mesmo conhecendo o UUID.
     */
    Optional<Usuario> findByIdAndTenantId(UUID id, UUID tenantId);

    /**
     * Verifica se um usuário específico pertence a um tenant.
     * Usado para validação de acesso (segurança multi-tenant).
     */
    boolean existsByIdAndTenantId(UUID id, UUID tenantId);

    // ──────────────────────────────────────────────
    // Queries customizadas com JPQL
    // ──────────────────────────────────────────────

    /**
     * Conta quantos usuários ativos existem em um tenant.
     * Útil para billing ou limites de plano.
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tenant.id = :tenantId AND u.ativo = true")
    long countActiveByTenantId(@Param("tenantId") UUID tenantId);

    /**
     * Busca usuários de um tenant cujo nome contenha o texto informado.
     * Útil para busca de usuários em painéis administrativos.
     */
    List<Usuario> findByTenantIdAndNomeContainingIgnoreCase(UUID tenantId, String nome);
}
