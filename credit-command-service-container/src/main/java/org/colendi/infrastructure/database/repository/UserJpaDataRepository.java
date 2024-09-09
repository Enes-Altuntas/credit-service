package org.colendi.infrastructure.database.repository;

import java.util.UUID;
import org.colendi.infrastructure.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaDataRepository extends JpaRepository<UserEntity, UUID> {

}
