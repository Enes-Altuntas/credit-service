package org.colendi.infrastructure.database.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.infrastructure.database.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaDataRepository extends JpaRepository<PaymentEntity, UUID> {

  List<PaymentEntity> findByInstallmentId(UUID installmentId);
}
