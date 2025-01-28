package com.cleartax.training_superheroes.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SQSConsumerRepository extends MongoRepository<SQSConsumerRepository, String> {
//    void consume();
}
