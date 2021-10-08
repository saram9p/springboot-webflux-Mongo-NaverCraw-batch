package com.cos.navernews.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NaverNewsRepository extends ReactiveMongoRepository<NaverNews, String> {

}
