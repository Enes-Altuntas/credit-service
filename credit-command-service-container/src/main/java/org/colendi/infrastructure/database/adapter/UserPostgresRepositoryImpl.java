package org.colendi.infrastructure.database.adapter;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.User;
import org.colendi.infrastructure.database.mapper.UserDataAccessMapper;
import org.colendi.infrastructure.database.repository.UserJpaDataRepository;
import org.colendi.usecase.ports.output.repository.rdbms.UserPostgresRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPostgresRepositoryImpl implements UserPostgresRepository {

  private final UserJpaDataRepository userJpaDataRepository;

  private final UserDataAccessMapper userDataAccessMapper;

  @Override
  public User findByUserId(UUID userId) {
    return userDataAccessMapper.toDomain(userJpaDataRepository.findById(userId).orElseThrow(() -> new RuntimeException("")));
  }
}
