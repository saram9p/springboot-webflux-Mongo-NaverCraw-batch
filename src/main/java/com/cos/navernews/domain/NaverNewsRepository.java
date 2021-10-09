package com.cos.navernews.domain;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.web.bind.annotation.PathVariable;

import reactor.core.publisher.Flux;

public interface NaverNewsRepository extends ReactiveMongoRepository<NaverNews, String> {

	@Tailable
	@Query("{}")
		Flux<NaverNews> mFindAll();		

//	@Tailable
//	@Query("db.test1.remove({'_id':ObjectId(':id')});")
//		Flux<NaverNews> mDeleteById(@PathVariable String id);		
	

	
}
