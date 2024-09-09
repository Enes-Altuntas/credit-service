package org.colendi.domain.entity;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.colendi.domain.valueobject.UserId;

@Getter
@Setter
public class User extends BaseEntity<UserId> {

  private final String firstName;
  private final String lastName;
  private List<Credit> credits;

  private User(Builder builder) {
    super.setId(builder.userId);
    firstName = builder.firstName;
    lastName = builder.lastName;
    credits = builder.credits;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private UserId userId;
    private String firstName;
    private String lastName;
    private List<Credit> credits;

    private Builder() {
    }

    public Builder userId(UserId val) {
      userId = val;
      return this;
    }

    public Builder firstName(String val) {
      firstName = val;
      return this;
    }

    public Builder lastName(String val) {
      lastName = val;
      return this;
    }

    public Builder credits(List<Credit> val) {
      credits = val;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
