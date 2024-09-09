package org.colendi.infrastructure.controller.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.dto.PageDTO;
import org.colendi.usecase.dto.UserCreditsResponse;
import org.colendi.usecase.ports.input.service.CreditQueryApplicationService;
import org.colendi.usecase.ports.output.nosql.CreditMongoRepository;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
public class CreditQueryController {

  private final CreditMongoRepository creditMongoRepository;
  @PostMapping ("/list")
  public ResponseEntity<Page<CreditResponse>> listCreditsOfUser(@Valid @RequestBody PageDTO pageDTO) {
    return ResponseEntity.ok(creditMongoRepository.findCreditsByUserId(pageDTO));
  }
}
