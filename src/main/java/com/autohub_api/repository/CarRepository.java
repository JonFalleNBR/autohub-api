package com.autohub_api.repository;

import com.autohub_api.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {

    List<Car> findByTenantId(UUID tenantId);

    List<Car> findByClienteId(UUID clienteId);

    Optional<Car> findByTenantIdAndPlate(UUID tenantId, String plate);

    boolean existsByTenantIdAndPlate(UUID tenantId, String plate);

    List<Car> findByTenantIdAndBrand(UUID tenantId, String brand);

    List<Car> findByTenantIdAndYear(UUID tenantId, Integer year);

    List<Car> findByClienteIdOrderByCreatedAtDesc(UUID clienteId);
}


