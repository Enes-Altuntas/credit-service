package org.colendi.ports.output.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Payment;

public interface PaymentPostgresRepository {

  Payment save(Payment payment);

  Payment findById(UUID installmentId);

  List<Payment> findByInstallmentId(UUID creditId);
}
