package org.colendi.infrastructure.database.repository;

import org.colendi.infrastructure.database.document.CreditDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditMongoDataRepository extends MongoRepository<CreditDocument, String> {
}
