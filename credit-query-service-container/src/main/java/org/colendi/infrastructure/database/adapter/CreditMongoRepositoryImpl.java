package org.colendi.infrastructure.database.adapter;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.colendi.domain.entity.Credit;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.infrastructure.database.document.CreditDocument;
import org.colendi.infrastructure.database.document.InstallmentDocument;
import org.colendi.infrastructure.database.mapper.CreditDataAccessMapper;
import org.colendi.infrastructure.database.mapper.InstallmentDataAccessMapper;
import org.colendi.infrastructure.database.repository.CreditMongoDataRepository;
import org.colendi.infrastructure.database.repository.InstallmentMongoDataRepository;
import org.colendi.infrastructure.message.consumer.model.CreditMessage;
import org.colendi.infrastructure.message.consumer.model.InstallmentMessage;
import org.colendi.usecase.dto.CreditResponse;
import org.colendi.usecase.dto.PageDTO;
import org.colendi.usecase.ports.output.nosql.CreditMongoRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditMongoRepositoryImpl implements CreditMongoRepository {

  private final MongoTemplate mongoTemplate;

  private final CreditMongoDataRepository creditMongoRepository;

  private final InstallmentMongoDataRepository installmentMongoDataRepository;

  private final CreditDataAccessMapper creditDataAccessMapper;

  private final InstallmentDataAccessMapper installmentDataAccessMapper;

  private final RedissonClient redissonClient;

  @Cacheable(value = "credits", key = "#pageDTO.userId")
  @Override
  public Page<CreditResponse> findCreditsByUserId(PageDTO pageDTO) {
    Pageable pageable = PageRequest.of(pageDTO.getPage() - 1, pageDTO.getSize());

    Query creditQuery = new Query();

    if (pageDTO.getUserId() != null) {
      Criteria userCriteria = new Criteria();
      userCriteria = userCriteria.and("userId").is(pageDTO.getUserId());
      creditQuery.addCriteria(userCriteria);
    }

    if (pageDTO.getStatus() != null) {
      Criteria statusCriteria = new Criteria();
      statusCriteria = statusCriteria.and("status").is(pageDTO.getStatus());
      creditQuery.addCriteria(statusCriteria);
    }

    Criteria dateFromCriteria = new Criteria();
    if (pageDTO.getDateFrom() != null && pageDTO.getDateTo() != null) {
      dateFromCriteria = dateFromCriteria.and("createdAt").gte(pageDTO.getDateFrom()).lte(pageDTO.getDateTo());
    } else if (pageDTO.getDateFrom() != null) {
      dateFromCriteria = dateFromCriteria.and("createdAt").gte(pageDTO.getDateFrom());
    } else if (pageDTO.getDateTo() != null) {
      dateFromCriteria = dateFromCriteria.and("createdAt").lte(pageDTO.getDateTo());
    }

    creditQuery.addCriteria(dateFromCriteria);
    creditQuery.with(pageable);

    List<CreditDocument> creditDocuments = new ArrayList<>();
    
    RLock lock = redissonClient.getLock("userLock:" + pageDTO.getUserId());
    try {
      lock.lock();
      creditDocuments = mongoTemplate.find(creditQuery, CreditDocument.class, "credits");
    } finally {
      lock.unlock();
    }

    List<CreditResponse> creditResponseList = creditDocuments.stream().map(creditDataAccessMapper::toResponse).toList();

    List<String> creditIdList = creditResponseList.stream()
        .map(CreditResponse::getId).toList();

    Query installmentQuery = new Query();
    installmentQuery.addCriteria(Criteria.where("creditId").in(creditIdList));
    List<InstallmentDocument> installments = mongoTemplate.find(installmentQuery,
        InstallmentDocument.class, "installments");

    creditResponseList.forEach(creditResponse -> {
      List<InstallmentDocument> list = installments.stream().filter(
              installmentDocument -> creditResponse.getId().equals(installmentDocument.getCreditId()))
          .toList();
      creditResponse.setInstallments(list.stream().map(installmentDataAccessMapper::toResponse).toList());
    });

    long total = mongoTemplate.count(creditQuery.skip(-1).limit(-1), CreditDocument.class);
    return new PageImpl<>(creditResponseList, pageable, total);
  }


  @CacheEvict(value = "credits", key = "#creditMessage.userId")
  @Override
  public void saveCredit(CreditMessage creditMessage) {
    creditMongoRepository.save(creditDataAccessMapper.toData(creditMessage));
  }

  @Override
  public void saveInstallment(InstallmentMessage installmentMessage) {
    installmentMongoDataRepository.save(installmentDataAccessMapper.toData(installmentMessage));
  }
}
