package org.colendi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.colendi.domain.entity.Installment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCreditResponse {

  private String creditId;

  private List<InstallmentResponse> installmentResponses;
}
