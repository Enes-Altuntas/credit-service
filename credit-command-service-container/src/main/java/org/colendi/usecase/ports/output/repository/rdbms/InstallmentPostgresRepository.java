package org.colendi.usecase.ports.output.repository.rdbms;

import java.util.List;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;

public interface InstallmentPostgresRepository {

  Installment save(Installment installment);

  List<Installment> findByCreditId(UUID creditId);
}
