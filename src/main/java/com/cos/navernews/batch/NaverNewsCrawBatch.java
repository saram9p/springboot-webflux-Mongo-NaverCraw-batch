package com.cos.navernews.batch;

import java.time.Duration;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class NaverNewsCrawBatch {

	private int aid = 277493;
	private final NaverNewsRepository naverNewsRepository;
	
	//@Scheduled(cron = "* * * * * *", zone = "Asia/Seoul")
	@Scheduled(fixedDelay = 1000*10*1) //1000*60*1
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
				String result = createdAtNatural.replaceAll("..(?:(오전|오후) ?.[0-9]:?.[0-9])", "");
				DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
				Date d = df.parse(result);
				long time = d.getTime();
				Timestamp ts = new Timestamp(time);
				
				LocalDateTime date = ts.toLocalDateTime().plusHours(9);
				
				Timestamp createdAt = Timestamp.valueOf(date);
				
				NaverNews naverNews = NaverNews.builder()
						.createdAt(createdAt)
						.company(company)
						.title(title)
						.build();
				
				naverNewsList.add(naverNews);
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
