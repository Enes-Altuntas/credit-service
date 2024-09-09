package org.colendi.infrastructure.database.adapter;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Credit;
import org.colendi.infrastructure.database.entity.CreditEntity;
import org.colendi.infrastructure.database.mapper.CreditDataAccessMapper;
import org.colendi.infrastructure.database.repository.CreditJpaDataRepository;
import org.colendi.usecase.ports.output.repository.rdbms.CreditPostgresRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditPostgresRepositoryImpl implements CreditPostgresRepository {

  private final CreditJpaDataRepository creditJpaDataRepository;

  private final CreditDataAccessMapper creditDataAccessMapper;

  @Override
  public void save(Credit credit) {
    CreditEntity entity = creditDataAccessMapper.toEntity(credit);
    creditJpaDataRepository.save(entity);
  }

  @Override
  public Credit findById(UUID creditId) {
    return creditDataAccessMapper.toDomain(creditJpaDataRepository.findById(creditId).orElseThrow(() -> new RuntimeException("")));
  }
}
