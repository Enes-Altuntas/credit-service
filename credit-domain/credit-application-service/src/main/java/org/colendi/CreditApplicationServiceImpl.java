package org.colendi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;
import org.colendi.domain.entity.Payment;
import org.colendi.domain.entity.User;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.domain.valueobject.CreditAmount;
import org.colendi.domain.valueobject.CreditId;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.domain.valueobject.InstallmentStatus;
import org.colendi.domain.valueobject.UserId;
import org.colendi.dto.PayInstallmentCommand;
import org.colendi.dto.RegisterCreditCommand;
import org.colendi.dto.RegisterCreditResponse;
import org.colendi.exception.CreditApplicationException;
import org.colendi.mapper.CreditDomainMapper;
import org.colendi.ports.input.service.CreditApplicationService;
import org.colendi.ports.output.repository.CreditPostgresRepository;
import org.colendi.ports.output.repository.InstallmentPostgresRepository;
import org.colendi.ports.output.repository.PaymentPostgresRepository;
import org.colendi.ports.output.repository.UserPostgresRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditApplicationServiceImpl implements CreditApplicationService {

  private final CreditDomainMapper creditDomainMapper;

  private final CreditPostgresRepository creditPostgresRepository;
  private final UserPostgresRepository userPostgresRepository;
  private final InstallmentPostgresRepository installmentPostgresRepository;
  private final PaymentPostgresRepository paymentPostgresRepository;

  @Override
  @Transactional
  public RegisterCreditResponse registerCredit(RegisterCreditCommand command) {
    validateUserExistence(command.getUserId());

    Credit credit = creditDomainMapper.registerCreditCommandMapper(command);
    credit.validateCreditInitialization();
    credit.initializeCredit();
    credit.initializeInstallments();

    creditPostgresRepository.save(credit);
    credit.getInstallments().forEach(installmentPostgresRepository::save);

    return creditDomainMapper.registerCreditResponseMapper(credit);
  }

  @Override
  @Transactional
  public void payInstallment(PayInstallmentCommand command) {
    Credit credit = creditPostgresRepository.findById(UUID.fromString(command.getCreditId()));
    if(credit.getStatus().equals(CreditStatus.CLOSED)) {
      throw new CreditApplicationException("Credit is already closed");
    }

    List<Installment> allInstallmentsOfCredit = installmentPostgresRepository
        .findByCreditId(UUID.fromString(command.getCreditId()));
    credit.getInstallments().addAll(allInstallmentsOfCredit);
    Installment installment = credit.getInstallmentById(
        UUID.fromString(command.getInstallmentId()));
    if(installment.getStatus().equals(InstallmentStatus.PAID)) {
      throw new CreditApplicationException("Installment is already paid");
    }

    List<Payment> payments = paymentPostgresRepository
        .findByInstallmentId(UUID.fromString(command.getInstallmentId()));
    installment.getPayments().addAll(payments);

    installment.validatePaymentAmount(BigDecimal.valueOf(command.getAmount()));
    Payment payment = installment.applyPayment(BigDecimal.valueOf(command.getAmount()));
    installment.applyStatusChange();
    credit.applyStatusChange();

    installmentPostgresRepository.save(installment);
    paymentPostgresRepository.save(payment);
    creditPostgresRepository.save(credit);
  }

  private void validateUserExistence(String userId) {
    userPostgresRepository.findByUserId(UUID.fromString(userId));
  }
}
