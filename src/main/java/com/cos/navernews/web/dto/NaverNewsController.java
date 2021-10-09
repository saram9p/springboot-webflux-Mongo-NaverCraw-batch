package com.cos.navernews.web.dto;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.navernews.domain.NaverNews;
import com.cos.navernews.domain.NaverNewsRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@RestController
public class NaverNewsController {

	private final NaverNewsRepository naverNewsRepository;
	
	@CrossOrigin
	@GetMapping(value = "/news", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<NaverNews> findAll() {
		return naverNewsRepository.mFindAll()
				.repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(1)))
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	@PostMapping("/news")
	public Mono<NaverNews> save(@RequestBody NaverNews naverNews) {
		return naverNewsRepository.save(naverNews);
	}
	
	@DeleteMapping("/news/{id}")
	public int deleteById(@PathVariable String id) {
		naverNewsRepository.deleteById(id);
		return 1;
		
	}
}
