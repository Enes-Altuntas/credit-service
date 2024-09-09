package org.colendi.usecase.ports.output.repository.rdbms;

import java.util.List;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Payment;

public interface PaymentPostgresRepository {

  Payment save(Payment payment);

  List<Payment> findByInstallmentId(UUID creditId);
}
