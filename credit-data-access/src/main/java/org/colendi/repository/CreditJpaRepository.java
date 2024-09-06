package org.colendi.repository;

import java.util.Optional;
import java.util.UUID;
import org.colendi.domain.valueobject.CreditStatus;
import org.colendi.entity.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditJpaRepository extends JpaRepository<CreditEntity, UUID> {

  @Modifying
  @Query("UPDATE CreditEntity c SET c.status = :newStatus " +
      "WHERE c.creditId = :creditId AND " +
      "  (SELECT COUNT(i) FROM InstallmentEntity i " +
      "   WHERE i.creditId = :creditId) = " +
      "  (SELECT COUNT(i) FROM InstallmentEntity i " +
      "   WHERE i.creditId = :creditId AND i.status = 'PAID')")
  void updateCreditStatusIfAllInstallmentsPaid(@Param("creditId") UUID creditId,
      @Param("newStatus") CreditStatus newStatus);
}
