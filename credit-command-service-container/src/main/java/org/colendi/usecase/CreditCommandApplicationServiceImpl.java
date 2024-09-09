package org.colendi.usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.entity.Installment;
import org.colendi.domain.entity.Payment;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.domain.valueobject.InstallmentStatus;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.dto.PayInstallmentCommand;
import org.colendi.usecase.dto.RegisterCreditCommand;
import org.colendi.usecase.exception.CreditApplicationException;
import org.colendi.usecase.mapper.CreditDomainMapper;
import org.colendi.usecase.ports.input.service.CreditCommandApplicationService;
import org.colendi.usecase.ports.output.repository.rdbms.CreditPostgresRepository;
import org.colendi.usecase.ports.output.repository.rdbms.InstallmentPostgresRepository;
import org.colendi.usecase.ports.output.repository.rdbms.PaymentPostgresRepository;
import org.colendi.usecase.ports.output.repository.rdbms.UserPostgresRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCommandApplicationServiceImpl implements CreditCommandApplicationService {

  private final CreditDomainMapper creditDomainMapper;

  private final CreditPostgresRepository creditPostgresRepository;
  private final UserPostgresRepository userPostgresRepository;
  private final InstallmentPostgresRepository installmentPostgresRepository;
  private final PaymentPostgresRepository paymentPostgresRepository;
  private final RedissonClient redissonClient;

  @Override
  @Transactional
  public CreditResponse registerCredit(RegisterCreditCommand command) {
    validateUserExistence(command.getUserId());

    Credit credit = creditDomainMapper.registerCreditCommandMapper(command);
    credit.validateCreditInitialization();
    credit.initializeCredit();
    credit.initializeInstallments();

    RLock lock = redissonClient.getLock("userLock:" + command.getUserId());
    try {
      lock.lock();
      creditPostgresRepository.save(credit);
      credit.getInstallments().forEach(installmentPostgresRepository::save);
    } finally {
      lock.unlock();
    }

    return creditDomainMapper.registerCreditResponseMapper(credit);
  }

  @Override
  @Transactional
  public void payInstallment(PayInstallmentCommand command) {
    Credit credit = creditPostgresRepository.findById(UUID.fromString(command.getCreditId()));
    if(credit.getStatus().equals(CreditStatus.CLOSED)) {
      throw new CreditApplicationException("Credit is already closed");
    }
    CreditStatus previousCreditStatus = credit.getStatus();

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
    if(!Objects.equals(previousCreditStatus, credit.getStatus())) {
      creditPostgresRepository.save(credit);
    }
  }

  private void validateUserExistence(String userId) {
    userPostgresRepository.findByUserId(UUID.fromString(userId));
  }
}
