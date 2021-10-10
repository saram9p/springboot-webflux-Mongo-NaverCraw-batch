package com.cos.navernews.batch;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cos.navernews.domain.NaverNews;
import com.cos.navernews.domain.NaverNewsRepository;
import com.cos.navernews.util.시간반환;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class NaverNewsCrawBatch {

	private int aid = 277493;
	private final NaverNewsRepository naverNewsRepository;
	private final 시간반환 타임스탬프반환;
	
	//@Scheduled(cron = "* * * * * *", zone = "Asia/Seoul")
	@Scheduled(fixedDelay = 1000*30*1) //1000*60*1
	public void NaverNewsCraw() {
		
		List<NaverNews> naverNewsList = new ArrayList<>();
		System.out.println("통신중");
		for (int i = 0; i < 2; i++) { /*날짜기준*/
			String aidStr = String.format("%010d", aid);
			String url = "https://news.naver.com/main/read.naver?mode=LSD&mid=shm&sid1=103&oid=437&aid=" + aidStr;
			RestTemplate rt = new RestTemplate();
			
			try {
				String html = rt.getForObject(url, String.class);
				Document doc = Jsoup.parse(html);
				Element companyElement = doc.selectFirst(".press_logo img");
				String companyAttr = companyElement.attr("title");
				Element titleElement = doc.selectFirst("#articleTitle");
				Element createdAtElement = doc.selectFirst(".t11");
				
				String company = companyAttr;
				String title = titleElement.text();
				
				String createdAtNatural = createdAtElement.text();

				Timestamp createdAt;
				try {
					createdAt = 시간반환.타임스탬프반환(createdAtNatural);
					
					LocalDateTime lt = LocalDateTime.now().minusDays(1).plusHours(9);
					Timestamp ts = Timestamp.valueOf(lt);
					
					if(createdAt.before(ts)) {
						
						NaverNews naverNews = NaverNews.builder()
								.createdAt(createdAt)
								.company(company)
								.title(title)
								.build();
						
						naverNewsList.add(naverNews);
					} else {
						System.out.println("오늘 날짜의 이전 날짜만 저장할 수 있습니다.");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} catch (Exception e) {
				System.out.println("통신 오류!!");
			}
			
			aid++;
		} // end of For
        Flux.fromIterable(naverNewsList)
        .flatMap(this.naverNewsRepository::save)
        .doOnComplete(() -> System.out.println("Complete"))
        .subscribe();
        //naverNewsRepository.saveAll(naverNewsList);
	} // end of NaverNewsCraw()
	
}
