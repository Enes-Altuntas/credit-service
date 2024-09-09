package org.colendi.usecase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.colendi.domain.valueobject.CreditStatus;

@Getter
@Builder
@AllArgsConstructor
public class PageDTO {

  @NotBlank(message = "User Id cannot be blank")
  private String userId;

  private CreditStatus status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  private Date dateFrom;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  private Date dateTo;

  @NotNull(message = "Page number cannot be null")
  @Min(value = 1, message = "Page number cannot be less than 1")
  private int page;

  @NotNull(message = "Page size cannot be null")
  private int size;
}
