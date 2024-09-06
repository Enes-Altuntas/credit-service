package org.colendi.adapter;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;
import org.colendi.entity.InstallmentEntity;
import org.colendi.mapper.InstallmentDataAccessMapper;
import org.colendi.ports.output.repository.CreditPostgresRepository;
import org.colendi.ports.output.repository.InstallmentPostgresRepository;
import org.colendi.repository.CreditJpaRepository;
import org.colendi.repository.InstallmentJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstallmentPostgresRepositoryImpl implements InstallmentPostgresRepository {

  private final InstallmentJpaRepository installmentJpaRepository;

  private final InstallmentDataAccessMapper installmentDataAccessMapper;

  @Override
  public Installment save(Installment installment) {
    InstallmentEntity entity = installmentDataAccessMapper.toEntity(installment);
    return installmentDataAccessMapper.toDomain(installmentJpaRepository.save(entity));
  }

  @Override
  public Installment findById(UUID installmentId) {
    return installmentDataAccessMapper.toDomain(installmentJpaRepository.findById(installmentId).orElseThrow(() -> new RuntimeException("")));
  }

  @Override
  public List<Installment> findByCreditId(UUID creditId) {
    return installmentDataAccessMapper.toDomainList(installmentJpaRepository.findByCreditId(creditId));
  }
}
