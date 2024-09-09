package org.colendi.infrastructure.database.mapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.domain.valueobject.UserId;
import org.colendi.infrastructure.database.document.CreditDocument;
import org.colendi.infrastructure.message.consumer.model.CreditMessage;
import org.colendi.usecase.dto.CreditResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditDataAccessMapper {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public CreditDocument toData(CreditMessage credit) {
    CreditDocument document = new CreditDocument();

    document.setCreditId(credit.getCreditId());
    document.setStatus(credit.getStatus());
    document.setAmount(credit.getAmount());
    document.setInstallmentsCount(credit.getInstallmentsCount().toString());
    document.setUserId(credit.getUserId());
    document.setCreatedAt(new Date(credit.getCreatedAt()/ 1000L));
    document.setUpdatedAt(new Date(credit.getUpdatedAt() / 1000L));
    return document;
  }

  public CreditResponse toResponse(CreditDocument credit) {
    return CreditResponse.builder()
        .id(credit.getCreditId())
        .status(credit.getStatus())
        .amount(credit.getAmount())
        .installmentsCount(credit.getInstallmentsCount())
        .createdAt(sdf.format(credit.getCreatedAt()))
        .build();
  }
}
