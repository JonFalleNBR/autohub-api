package com.autohub_api.repository;

import com.autohub_api.model.entity.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, UUID> {

    // Buscar histórico completo de um carro (ordenado do mais recente ao mais antigo)
    List<ServiceHistory> findByCarIdOrderByServiceDateDesc(UUID carId);

    // Buscar histórico de um carro por período
    List<ServiceHistory> findByCarIdAndServiceDateBetween(
        UUID carId,
        OffsetDateTime startDate,
        OffsetDateTime endDate
    );

    // Buscar serviços por tipo
    List<ServiceHistory> findByCarIdAndServiceType(UUID carId, String serviceType);

    // Buscar todos os serviços de um tenant
    List<ServiceHistory> findByTenantIdOrderByServiceDateDesc(UUID tenantId);

    // Buscar últimos N serviços de um carro
    List<ServiceHistory> findTop5ByCarIdOrderByServiceDateDesc(UUID carId);
}


