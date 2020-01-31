package com.cargosmart.mongodemo.repository;

import com.cargosmart.mongodemo.entity.Bubble;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BubbleRepository extends MongoRepository<Bubble, String> {
}
