package org.colendi.usecase.ports.input.service;

import jakarta.validation.Valid;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.dto.PayInstallmentCommand;
import org.colendi.usecase.dto.RegisterCreditCommand;

public interface CreditCommandApplicationService {

  CreditResponse registerCredit(@Valid RegisterCreditCommand command);

  void payInstallment(@Valid PayInstallmentCommand command);

}
