package org.colendi.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.colendi.domain.entity.Installment;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.InstallmentAmount;
import org.colendi.domain.valueobject.InstallmentDueDate;
import org.colendi.domain.valueobject.InstallmentId;
import org.colendi.entity.InstallmentEntity;
import org.springframework.stereotype.Component;

@Component
public class InstallmentDataAccessMapper {

  public InstallmentEntity toEntity(Installment installment) {
    return InstallmentEntity.builder()
        .id(installment.getId().getValue())
        .dueDate(installment.getDueDate().getValue())
        .status(installment.getStatus())
        .amount(String.valueOf(installment.getInstallmentAmount().getValue()))
        .creditId(installment.getCreditId().getValue())
        .build();
  }

  public Installment toDomain(InstallmentEntity installmentEntity) {
    return Installment.builder()
        .installmentId(new InstallmentId(installmentEntity.getId()))
        .dueDate(new InstallmentDueDate(installmentEntity.getDueDate()))
        .status(installmentEntity.getStatus())
        .creditId(new CreditId(installmentEntity.getCreditId()))
        .installmentAmount(new InstallmentAmount(new BigDecimal(installmentEntity.getAmount())))
        .payments(new ArrayList<>())
        .build();
  }

  public List<Installment> toDomainList(List<InstallmentEntity> installmentEntity) {
    return installmentEntity.stream().map(this::toDomain).toList();
  }
}
