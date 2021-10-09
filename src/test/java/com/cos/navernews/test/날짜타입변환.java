package com.cos.navernews.test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.cos.navernews.domain.TestNaverNews;
import com.cos.navernews.domain.TestNaverNewsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class 날짜타입변환 {
	
	private int aid = 277493;
	private final TestNaverNewsRepository testNaverNewsRepository;
	
	//@Test
	public void test1() {
		String createdAt = "2021.10.04. 오후 4:44"; //2021.10.04. 오후 4:44
		
		String resultf = createdAt.replaceAll("(?:(오전|오후)\\s*([0-9]+:+[0-9][0-9])\\s*)", "");
		String result = resultf.replaceAll(".(?!..)", "");
		System.out.println(result);
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
//		System.out.println(sdf.format(result));
		
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(result));
//		Timestamp ts = Timestamp.valueOf(localDateTime);
		
		System.out.println(localDateTime);

		
//	   LocalDateTime t = LocalDateTime.now().minusDays(1).plusHours(9);
//	   System.out.println(t);
//	   Timestamp ts = Timestamp.valueOf(t);
//	   System.out.println(ts);
	}
	
	//@Test
	public void test2() throws ParseException {
		//문자열
		String createdAt = "2021.10.07. 오후 1:4"; //2021.10.04. 오후 4:44
		
		String resultf = createdAt.replaceAll("(?:(오전|오후)\\s*([0-9]+:+[0-9][0-9]))", "");
		System.out.println(resultf);
		String result = resultf.replaceAll(".(?!..)", "");
		System.out.println(result);
		
//		// 포맷터
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//		System.out.println(formatter + ": " + formatter.getClass().getSimpleName());  // 자바 변수 타입 확인
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		Date d = df.parse(result);

		
		
		// 타임스탬프 타입으로 변경
//		Timestamp timestamp = Timestamp.valueOf(createdAt);  // 반드시 yyyy-mm-dd hh:mm:ss의 형식을 따라야함
		long time = d.getTime();
		Timestamp ts = new Timestamp(time);
		
		// 문자열 -> Date
		//LocalDateTime date = LocalDateTime.parse(createdAt, formatter);
		LocalDateTime date = ts.toLocalDateTime().plusHours(9);
		System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
		
		Timestamp good = Timestamp.valueOf(date);
		System.out.println(good);
	}
	
	@Test
	public void test3() throws ParseException {
		
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
		} // end of for
		
		System.out.println(naverNewsList.size());
		testNaverNewsRepository.saveAll(naverNewsList);
//		String createdAt = "2021.10.07. 오후 1:4"; //2021.10.04. 오후 4:44
//		
//		String result = createdAt.replaceAll("..(?:(오전|오후) ?.[0-9]:?.[0-9])", "");
//		System.out.println(result);
//		
//		DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
//		Date d = df.parse(result);
//		
//		long time = d.getTime();
//		Timestamp ts = new Timestamp(time);
//		
//		LocalDateTime date = ts.toLocalDateTime().plusHours(9);
//		System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
//		
//		Timestamp good = Timestamp.valueOf(date);
//		System.out.println(good);
//		
//		TestNaverNews naverNews = TestNaverNews.builder()
//				.createdAt(createdAt)
//				.company(company)
//				.title(title)
//				.build();
		
	}
	
}
