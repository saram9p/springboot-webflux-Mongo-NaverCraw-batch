package com.cos.navernews.batch;

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
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.cos.navernews.domain.NaverNews;
import com.cos.navernews.domain.NaverNewsRepository;
import com.cos.navernews.domain.TestNaverNews;
import com.cos.navernews.domain.TestNaverNewsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestBatch {
	
	private int aid = 277493;
	//private final TestNaverNewsRepository testNaverNewsRepository;
	
	//@Scheduled(cron = "* * * * * *", zone = "Asia/Seoul")
	@Test
	@Scheduled(fixedDelay = 1000*60*1)
	public void NaverNewsCraw() {
		
		List<TestNaverNews> naverNewsList = new ArrayList<>();
		
		for (int i = 0; i < 10/*날짜기준*/; i++) {
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
				
				TestNaverNews naverNews = TestNaverNews.builder()
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
		System.out.println(naverNewsList.size());
		//naverNewsRepository.saveAll(naverNewsList);
		for ( TestNaverNews items : naverNewsList) {
			System.out.println(String.format("아이디:%s, 신문사명:%s, 제목:%s, 날짜:%s", items.get_id(), items.getCompany(), items.getTitle(), items.getCreatedAt()));
		}
		//testNaverNewsRepository.saveAll(naverNewsList);
	} // end of NaverNewsCraw()
	
}
