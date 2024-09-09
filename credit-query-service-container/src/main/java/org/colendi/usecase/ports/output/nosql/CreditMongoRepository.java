package org.colendi.usecase.ports.output.nosql;

import java.util.List;
import org.colendi.domain.entity.Credit;
import org.colendi.infrastructure.database.document.CreditDocument;
import org.colendi.infrastructure.message.consumer.model.CreditMessage;
import org.colendi.infrastructure.message.consumer.model.InstallmentMessage;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.dto.PageDTO;
import org.springframework.data.domain.Page;

public interface CreditMongoRepository {

  CreditResponse findCreditById(String id);

  Page<CreditResponse> findCreditsByUserId(PageDTO pageDTO);

  void saveCredit(CreditMessage credit);

  void saveInstallment(InstallmentMessage installment);
}
