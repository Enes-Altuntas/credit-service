package org.colendi.ports.input.service;

import jakarta.validation.Valid;
import org.colendi.dto.PayInstallmentCommand;
import org.colendi.dto.RegisterCreditCommand;
import org.colendi.dto.RegisterCreditResponse;

public interface CreditApplicationService {

  RegisterCreditResponse registerCredit(@Valid RegisterCreditCommand command);

  void payInstallment(@Valid PayInstallmentCommand command);

}
