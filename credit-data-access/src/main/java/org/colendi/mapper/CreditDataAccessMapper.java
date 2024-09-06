package org.colendi.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.UserId;
import org.colendi.entity.CreditEntity;
import org.springframework.stereotype.Component;

@Component
public class CreditDataAccessMapper {

  public CreditEntity toEntity(Credit credit) {
    return CreditEntity.builder()
        .creditId(credit.getId().getValue())
        .amount(String.valueOf(credit.getCreditAmount().getValue()))
        .installmentsCount(credit.getInstallmentsCount())
        .status(credit.getStatus())
        .userId(credit.getUserId().getValue())
        .build();
  }

  public Credit toDomain(CreditEntity creditEntity) {
    return Credit.builder()
        .creditAmount(new CreditAmount(new BigDecimal(creditEntity.getAmount())))
        .userId(new UserId(creditEntity.getUserId()))
        .installmentsCount(creditEntity.getInstallmentsCount())
        .status(creditEntity.getStatus())
        .creditId(new CreditId(creditEntity.getCreditId()))
        .installments(new ArrayList<>())
        .build();
  }
}
