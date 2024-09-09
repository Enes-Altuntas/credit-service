package org.colendi.domain.entity;

import java.util.Objects;

public abstract class BaseEntity<T> {

  private T id;

  public T getId() {
    return this.id;
  }

  public void setId(T id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    BaseEntity<?> other = (BaseEntity<?>) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
