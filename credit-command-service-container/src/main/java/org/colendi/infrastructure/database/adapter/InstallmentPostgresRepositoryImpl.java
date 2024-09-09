package org.colendi.infrastructure.database.adapter;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Installment;
import org.colendi.infrastructure.database.entity.InstallmentEntity;
import org.colendi.infrastructure.database.mapper.InstallmentDataAccessMapper;
import org.colendi.infrastructure.database.repository.InstallmentJpaDataRepository;
import org.colendi.usecase.ports.output.repository.rdbms.InstallmentPostgresRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstallmentPostgresRepositoryImpl implements InstallmentPostgresRepository {

  private final InstallmentJpaDataRepository installmentJpaDataRepository;

  private final InstallmentDataAccessMapper installmentDataAccessMapper;

  @Override
  public Installment save(Installment installment) {
    InstallmentEntity entity = installmentDataAccessMapper.toEntity(installment);
    return installmentDataAccessMapper.toDomain(installmentJpaDataRepository.save(entity));
  }

  @Override
  public List<Installment> findByCreditId(UUID creditId) {
    return installmentDataAccessMapper.toDomainList(installmentJpaDataRepository.findByCreditId(creditId));
  }
}
