package com.autohub_api.repository;

import com.autohub_api.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    List<Cliente> findByTenantId(UUID tenantId);

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    List<Cliente> findByTenantIdAndNomeContainingIgnoreCase(UUID tenantId, String nome);

    Optional<Cliente> findByTenantIdAndCpf(UUID tenantId, String cpf);
}

