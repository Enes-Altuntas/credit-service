package org.colendi.ports.output.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;

public interface InstallmentPostgresRepository {

  Installment save(Installment installment);

  Installment findById(UUID installmentId);

  List<Installment> findByCreditId(UUID creditId);
}
