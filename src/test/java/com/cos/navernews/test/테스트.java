package com.cos.navernews.test;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

public class 테스트 {

//	@Test
//	public void nonBlocking3() throws InterruptedException {
//		int LOOP_COUNT = 100;
//	    final StopWatch stopWatch = new StopWatch();
//	    stopWatch.start();
//	    for (int i = 0; i < LOOP_COUNT; i++) {
//	        this.webClient
//	                .get()
//	                .uri(THREE_SECOND_URL)
//	                .retrieve()
//	                .bodyToMono(String.class)
//	                .subscribe(it -> {
//	                    count.countDown();
//	                    System.out.println(it);
//	                });
//	    }
//
//	    count.await(10, TimeUnit.SECONDS);
//	    stopWatch.stop();
//	    System.out.println(stopWatch.getTotalTimeSeconds());
//	}
}
