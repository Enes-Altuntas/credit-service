package org.colendi.infrastructure.database.mapper;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.colendi.infrastructure.database.document.InstallmentDocument;
import org.colendi.infrastructure.message.consumer.model.InstallmentMessage;
import org.colendi.usecase.dto.InstallmentResponse;
import org.springframework.stereotype.Component;

@Component
public class InstallmentDataAccessMapper {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  public InstallmentDocument toData(InstallmentMessage installment) {
    InstallmentDocument document = new InstallmentDocument();

    document.setInstallmentId(installment.getId());
    document.setStatus(installment.getStatus());
    document.setDueDate(new Date(installment.getDueDate() / 1000L));
    document.setCreditId(installment.getCreditId());
    document.setAmount(installment.getAmount());
    document.setCreatedAt(new Date(installment.getCreatedAt() / 1000L));
    document.setUpdatedAt(new Date(installment.getUpdatedAt() / 1000L));
    return document;
  }

  public InstallmentResponse toResponse(InstallmentDocument installment) {
    return InstallmentResponse.builder()
        .id(installment.getInstallmentId())
        .status(installment.getStatus())
        .amount(installment.getAmount())
        .dueDate(sdf.format(installment.getDueDate()))
        .build();
  }
}
