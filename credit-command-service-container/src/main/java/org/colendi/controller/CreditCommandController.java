package org.colendi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.dto.PayInstallmentCommand;
import org.colendi.dto.RegisterCreditCommand;
import org.colendi.dto.RegisterCreditResponse;
import org.colendi.ports.input.service.CreditApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
public class CreditCommandController {

  private final CreditApplicationService creditApplicationService;

  @PostMapping ("/register")
  public ResponseEntity<RegisterCreditResponse> registerCredit(@Valid @RequestBody RegisterCreditCommand command) {
    return ResponseEntity.ok(creditApplicationService.registerCredit(command));
  }

  @PostMapping ("/pay")
  public ResponseEntity<Void> payInstallment(@Valid @RequestBody PayInstallmentCommand command) {
    creditApplicationService.payInstallment(command);
    return ResponseEntity.ok().build();
  }
}
