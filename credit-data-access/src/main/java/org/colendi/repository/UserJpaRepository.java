package org.colendi.repository;

import java.util.UUID;
import org.colendi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

}
