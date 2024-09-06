package org.colendi.adapter;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Payment;
import org.colendi.entity.PaymentEntity;
import org.colendi.mapper.PaymentDataAccessMapper;
import org.colendi.ports.output.repository.CreditPostgresRepository;
import org.colendi.ports.output.repository.PaymentPostgresRepository;
import org.colendi.repository.CreditJpaRepository;
import org.colendi.repository.PaymentJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPostgresRepositoryImpl implements PaymentPostgresRepository {

  private final PaymentJpaRepository paymentJpaRepository;

  private final PaymentDataAccessMapper paymentDataAccessMapper;

  @Override
  public Payment save(Payment payment) {
    PaymentEntity entity = paymentDataAccessMapper.toEntity(payment);
    return paymentDataAccessMapper.toDomain(paymentJpaRepository.save(entity));
  }

  @Override
  public Payment findById(UUID installmentId) {
    return paymentDataAccessMapper.toDomain(paymentJpaRepository.findById(installmentId).orElseThrow(() -> new RuntimeException("")));
  }

  @Override
  public List<Payment> findByInstallmentId(UUID creditId) {
    List<PaymentEntity> byInstallmentId = paymentJpaRepository.findByInstallmentId(creditId);
    return paymentDataAccessMapper.toDomainList(byInstallmentId);
  }
}
