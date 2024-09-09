package org.colendi.usecase.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditResponse implements Serializable {

  private String id;

  private String status;

  private String amount;

  private String installmentsCount;

  private List<InstallmentResponse> installments;

  private String createdAt;
}
