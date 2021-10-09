package com.cos.navernews.domain;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;

import reactor.core.publisher.Flux;

public interface TestNaverNewsRepository extends ReactiveMongoRepository<TestNaverNews, String> {

	@Tailable
	@Query("{}")
		Flux<NaverNews> mFindAll();	
	
}
