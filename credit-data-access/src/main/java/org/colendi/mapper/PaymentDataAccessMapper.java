package org.colendi.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.colendi.domain.entity.Payment;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.domain.valueobject.PaymentAmount;
import org.colendi.domain.valueobject.PaymentId;
import org.colendi.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {

  public PaymentEntity toEntity(Payment payment) {
    return PaymentEntity.builder()
        .id(payment.getId().getValue())
        .amount(String.valueOf(payment.getPaymentAmount().getValue()))
        .installmentId(payment.getInstallmentId().getValue())
        .build();
  }

  public Payment toDomain(PaymentEntity paymentEntity) {
    return Payment.builder()
        .paymentId(new PaymentId(paymentEntity.getId()))
        .paymentAmount(new PaymentAmount(new BigDecimal(paymentEntity.getAmount())))
        .installmentId(new InstallmentId(paymentEntity.getInstallmentId()))
        .build();
  }

  public List<Payment> toDomainList(List<PaymentEntity> paymentEntity) {
    return paymentEntity.stream().map(this::toDomain).toList();
  }
}
