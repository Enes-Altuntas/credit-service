package org.colendi.mapper;

import org.colendi.domain.entity.User;
import org.colendi.domain.valueobject.UserId;
import org.colendi.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDataAccessMapper {

  public UserEntity toEntity(User user) {
    return UserEntity.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .id(user.getId().getValue())
        .build();
  }

  public User toDomain(UserEntity userEntity) {
    return User.builder()
        .userId(new UserId(userEntity.getId()))
        .firstName(userEntity.getFirstName())
        .lastName(userEntity.getLastName())
        .build();
  }
}
