package org.colendi.usecase.ports.output.repository.rdbms;

import java.util.UUID;
import org.colendi.domain.entity.User;

public interface UserPostgresRepository {

  User findByUserId(UUID userId);
}
