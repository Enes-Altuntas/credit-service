package org.colendi.infrastructure.database.adapter;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Payment;
import org.colendi.infrastructure.database.entity.PaymentEntity;
import org.colendi.infrastructure.database.mapper.PaymentDataAccessMapper;
import org.colendi.infrastructure.database.repository.PaymentJpaDataRepository;
import org.colendi.usecase.ports.output.repository.rdbms.PaymentPostgresRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPostgresRepositoryImpl implements PaymentPostgresRepository {

  private final PaymentJpaDataRepository paymentJpaDataRepository;

  private final PaymentDataAccessMapper paymentDataAccessMapper;

  @Override
  public Payment save(Payment payment) {
    PaymentEntity entity = paymentDataAccessMapper.toEntity(payment);
    return paymentDataAccessMapper.toDomain(paymentJpaDataRepository.save(entity));
  }

  @Override
  public List<Payment> findByInstallmentId(UUID creditId) {
    List<PaymentEntity> byInstallmentId = paymentJpaDataRepository.findByInstallmentId(creditId);
    return paymentDataAccessMapper.toDomainList(byInstallmentId);
  }
}
