package org.colendi.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.entity.InstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstallmentJpaRepository extends JpaRepository<InstallmentEntity, UUID> {

  List<InstallmentEntity> findByCreditId(UUID creditId);
}
