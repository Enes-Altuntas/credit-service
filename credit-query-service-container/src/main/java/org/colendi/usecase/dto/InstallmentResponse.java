package org.colendi.usecase.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentResponse implements Serializable {

  private String id;

  private String status;

  private String amount;

  private String dueDate;
}
