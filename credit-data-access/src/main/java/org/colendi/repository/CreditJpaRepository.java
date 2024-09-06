package org.colendi.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditJpaRepository extends JpaRepository<CreditEntity, UUID> {

  @Query(value = "SELECT * FROM credits c " +
      "WHERE c.status = 'OPEN' AND " +
      "  c.updated_at < NOW() - INTERVAL '1 DAY'",
      nativeQuery = true)
  List<CreditEntity> findOpenCreditsOlderThanOneDay();
}
