package com.autohub_api.repository;

import com.autohub_api.model.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository responsável pelas operações de persistência da entidade {@link Tenant}.
 *
 * <p>Um Tenant representa uma oficina/empresa cadastrada no sistema.
 * Cada tenant é completamente isolado dos outros — todos os dados de clientes,
 * carros e histórico de serviços pertencem a um tenant específico.</p>
 *
 * <p>Métodos derivados (Derived Query Methods) sem @Query são resolvidos
 * automaticamente pelo Spring Data JPA através da análise do nome do método.</p>
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    // ──────────────────────────────────────────────
    // Busca por slug (identificador público único)
    // ──────────────────────────────────────────────

    /**
     * Busca um tenant pelo slug.
     * O slug é o identificador "amigável" de URL, ex: "oficina-sao-paulo".
     */
    Optional<Tenant> findBySlug(String slug);

    /**
     * Verifica se já existe um tenant com o slug informado.
     * Usado para validar unicidade antes de criar um novo tenant.
     */
    boolean existsBySlug(String slug);

    // ──────────────────────────────────────────────
    // Busca por nome
    // ──────────────────────────────────────────────

    /**
     * Busca tenants cujo nome contenha o texto informado (case-insensitive).
     * Útil para busca/autocomplete administrativo.
     */
    List<Tenant> findByNameContainingIgnoreCase(String name);

    /**
     * Busca um tenant pelo nome exato.
     */
    Optional<Tenant> findByName(String name);

    // ──────────────────────────────────────────────
    // Verificações de existência
    // ──────────────────────────────────────────────

    /**
     * Verifica se já existe um tenant com o nome exato informado.
     */
    boolean existsByName(String name);

    // ──────────────────────────────────────────────
    // Queries customizadas com JPQL
    // ──────────────────────────────────────────────

    /**
     * Retorna todos os tenants ordenados por nome (A → Z).
     * Útil para listagens administrativas.
     */
    @Query("SELECT t FROM Tenant t ORDER BY t.name ASC")
    List<Tenant> findAllOrderedByName();
}


