package org.colendi.infrastructure.database.repository;

import java.util.List;
import java.util.UUID;
import org.colendi.infrastructure.database.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditJpaDataRepository extends JpaRepository<CreditEntity, UUID> {
}
