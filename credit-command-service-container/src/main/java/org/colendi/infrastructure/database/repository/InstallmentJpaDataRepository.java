package org.colendi.infrastructure.database.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.infrastructure.database.entity.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentJpaDataRepository extends JpaRepository<InstallmentEntity, UUID> {

  List<InstallmentEntity> findByCreditId(UUID creditId);
}
