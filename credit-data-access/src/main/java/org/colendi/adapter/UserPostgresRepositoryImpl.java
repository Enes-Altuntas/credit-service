package org.colendi.adapter;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.colendi.domain.entity.User;
import org.colendi.entity.UserEntity;
import org.colendi.mapper.UserDataAccessMapper;
import org.colendi.ports.output.repository.UserPostgresRepository;
import org.colendi.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPostgresRepositoryImpl implements UserPostgresRepository {

  private final UserJpaRepository userJpaRepository;

  private final UserDataAccessMapper userDataAccessMapper;

  @Override
  public User findByUserId(UUID userId) {
    return userDataAccessMapper.toDomain(userJpaRepository.findById(userId).orElseThrow(() -> new RuntimeException("")));
  }
}
