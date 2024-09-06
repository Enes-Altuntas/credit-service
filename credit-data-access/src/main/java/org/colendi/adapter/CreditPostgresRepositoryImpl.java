package org.colendi.adapter;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.entity.CreditEntity;
import org.colendi.mapper.CreditDataAccessMapper;
import org.colendi.ports.output.repository.CreditPostgresRepository;
import org.colendi.repository.CreditJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditPostgresRepositoryImpl implements CreditPostgresRepository {

  private final CreditJpaRepository creditJpaRepository;

  private final CreditDataAccessMapper creditDataAccessMapper;

  @Override
  public void save(Credit credit) {
    CreditEntity entity = creditDataAccessMapper.toEntity(credit);
    creditJpaRepository.save(entity);
  }

  @Override
  public Credit findById(UUID creditId) {
    return creditDataAccessMapper.toDomain(creditJpaRepository.findById(creditId).orElseThrow(() -> new RuntimeException("")));
  }
}
