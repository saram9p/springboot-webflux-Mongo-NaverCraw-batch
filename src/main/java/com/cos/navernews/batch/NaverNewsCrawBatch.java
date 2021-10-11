package com.cos.navernews.batch;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cos.navernews.domain.NaverNews;
import com.cos.navernews.domain.NaverNewsRepository;
import com.cos.navernews.util.TypeConverter;
import com.cos.navernews.util.기사날짜파싱;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class NaverNewsCrawBatch {

	private int aid = 277726; //277493
	private int aidSave = aid;
	private final NaverNewsRepository naverNewsRepository;
	private final 기사날짜파싱 기사날짜파싱;
	
	@Scheduled(cron = "0/1 * 1 * * *", zone = "Asia/Seoul")
	//@Scheduled(fixedDelay = 1000*2*1) //1000*60*1
	public void NaverNewsCraw() {
		System.out.println("통신중");

		this.Craw();

        //naverNewsRepository.saveAll(naverNewsList);
	} // end of NaverNewsCraw()
	
	public void Craw() {
		
		List<NaverNews> naverNewsList = new ArrayList<>();
			
		// 오늘 날짜의 이전 날짜를 설정
		LocalDateTime ldt = LocalDateTime.now().minusDays(1);
		
		for (int i = 0; i < 2; i++) { /*날짜기준*/
			
			String aidStr = String.format("%010d", aidSave);
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
				
				Date createdAt = 기사날짜파싱.파싱후Date반환(createdAtNatural);
				
				String stringStandardTime = "2021.10.07 00:00"; 
				Date standardTime = TypeConverter.StringToDate(stringStandardTime);
				
				// 기사 데이터를 받는 기준 날짜 설정
				int compare = createdAt.compareTo(standardTime);
				// 기사의 날짜가 기준 날짜 보다 크면 조건을 만족
				if(compare > 0) {
					
					// LocalDateTime을 Date 타입으로 변환
					Date today = Timestamp.valueOf(ldt);
					System.out.println("기준 날짜: " + createdAt + " // today: " + today + " aid: " + aidStr);
					int compare2 = createdAt.compareTo(today);
					// 가져온 기사 날짜와 오늘 날짜의 이전 날짜와 비교
					// 비교해서 가져온 기사 날짜가 작으면 데이터를 저장, 크면 메시지 출력
					if(compare2 < 0) {
						
						NaverNews naverNews = NaverNews.builder()
								.createdAt(createdAt)
								.company(company)
								.title(title)
								.build();
						
						naverNewsList.add(naverNews);
						
					} else {
						System.out.println("오늘 날짜의 이전 날짜만 저장할 수 있습니다.");
						break;
					}
					
				} else {
					System.out.println("기사의 날짜가 기준 날짜보다 작으면 저장할 수 없습니다.");
					break;
				}
				
				
			} catch (Exception e) {
				System.out.println("통신 오류!! : " + e.getMessage());
			}
			
			aid++;
			
			aidSave = aid;
			System.out.println("현재 저장된 aid는 " + aidSave + "입니다.");
			
		} // end of For
		Flux.fromIterable(naverNewsList)
		.delayElements(Duration.ofSeconds(2))
		.flatMap(this.naverNewsRepository::save)
		.doOnComplete(() -> System.out.println("데이터 저장 완료"))
		.subscribe();
	}
	
}
