package org.colendi.usecase.ports.output.repository.rdbms;

import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.valueobject.CreditStatus;

public interface CreditPostgresRepository {

  void save(Credit credit);

  Credit findById(UUID installmentId);
}
