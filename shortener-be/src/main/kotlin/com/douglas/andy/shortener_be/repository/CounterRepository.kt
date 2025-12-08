package com.douglas.andy.shortener_be.repository;

import com.douglas.andy.shortener_be.model.Counter
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository

@Repository
interface CounterRepository : MongoRepository<Counter, Int>