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
@Document(collection = "credits")
public class CreditDocument {

  @Id
  private String creditId;

  private String status;

  private String amount;

  private String installmentsCount;

  private String userId;

  private Date createdAt;

  private Date updatedAt;
}
