package org.colendi.usecase.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse {

  private String creditId;

  private List<InstallmentResponse> installmentResponses;
}
