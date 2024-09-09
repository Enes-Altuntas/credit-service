package org.colendi.infrastructure.database.document;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "installments")
public class InstallmentDocument {

  @Id
  private String installmentId;

  private String status;

  private String amount;

  private Date dueDate;

  private String creditId;

  private Date createdAt;

  private Date updatedAt;
}
