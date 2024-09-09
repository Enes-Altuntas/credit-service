package org.colendi.infrastructure.controller.endpoints;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.usecase.dto.PayInstallmentCommand;
import org.colendi.usecase.dto.RegisterCreditCommand;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.ports.input.service.CreditCommandApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditCommandController {

  private final CreditCommandApplicationService creditCommandApplicationService;

  @PostMapping ("/register")
  public ResponseEntity<CreditResponse> registerCredit(@Valid @RequestBody RegisterCreditCommand command) {
    return ResponseEntity.ok(creditCommandApplicationService.registerCredit(command));
  }

  @PostMapping ("/pay")
  public ResponseEntity<Void> payInstallment(@Valid @RequestBody PayInstallmentCommand command) {
    creditCommandApplicationService.payInstallment(command);
    return ResponseEntity.ok().build();
  }
}
