package org.colendi.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

  List<PaymentEntity> findByInstallmentId(UUID installmentId);
}
