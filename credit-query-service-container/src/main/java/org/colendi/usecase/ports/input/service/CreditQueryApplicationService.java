package org.colendi.usecase.ports.input.service;

import org.colendi.domain.entity.Credit;
import org.colendi.usecase.dto.PageDTO;
import org.colendi.usecase.dto.UserCreditsResponse;
import org.springframework.data.domain.Page;

public interface CreditQueryApplicationService {

  Page<UserCreditsResponse> listCreditsOfUser(PageDTO pageDTO);

  void saveCredit(Credit credit);

}
