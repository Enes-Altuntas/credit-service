package org.colendi.ports.output.repository;

import java.util.UUID;
import org.colendi.domain.entity.User;

public interface UserPostgresRepository {

  User findByUserId(UUID userId);
}
