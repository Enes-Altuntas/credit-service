package org.colendi.infrastructure.database.repository;

import org.colendi.infrastructure.database.document.CreditDocument;
import org.colendi.infrastructure.database.document.InstallmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentMongoDataRepository extends MongoRepository<InstallmentDocument, String> {
}
