package com.cos.navernews.test;

import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import com.cos.navernews.domain.TestNaverNews;
import com.cos.navernews.domain.TestNaverNewsRepository;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class 날짜타입변환 {
	
	private int aid = 277493;
	//private final TestNaverNewsRepository testNaverNewsRepository;
	
	@Test
	public void test1() {
		String createdAt = "2021.10.07. 오전 2:21";

		String result = createdAt.replaceAll(".\\s(오후|오전)", "");
		System.out.println(result);
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		Date d;
		try {
			d = df.parse(result);
			
			// getTime()을 사용하여 java.util.Date를java.sql.Date로 변환
			long time = d.getTime();
			java.sql.Date date1 = new java.sql.Date(time);
			
			// Timestamp로 변환
			Timestamp ts = new Timestamp(date1.getTime());

			LocalDateTime date = ts.toLocalDateTime().plusHours(9);
			System.out.println(date + ": " + date.getClass().getSimpleName()); // 자바 변수 타입 확인
		} catch (ParseException e) {
			e.printStackTrace();
		}

//	   LocalDateTime t = LocalDateTime.now().minusDays(1).plusHours(9);
//	   System.out.println(t);
//	   Timestamp ts = Timestamp.valueOf(t);
//	   System.out.println(ts);
	}
	
	//@Test
	public void test2() {
		// (?:^|[^\w\S])((?!20([0-9]([0-9])\d))(?:\d{4})\.(?:0[^0]|1[012])\.(?:0[^0]|[12]\d|3[01])\.? ?(오후|오전) ?(?:(?!2[4-9])[0-12]?\d:[0-5]\d:?[0-5]?\d?\b))(?:$|[^\w\S])
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
	public void test3() throws ParseException {
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
	
	//@Test
	public void test4() throws ParseException {
		
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
//		testNaverNewsRepository.saveAll(naverNewsList);
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
	
	//@Test
	public void test5() {
		String createdAt = "2021.10.07. 오전 2:21";
		Pattern pt1 = Pattern.compile("오후");
		Pattern pt2 = Pattern.compile("오후\\s12");
		//String resultf = createdAt.replaceAll(".\\s(오후|오전)\\s", "");
		
		Matcher matcher1 = pt1.matcher(createdAt);
		Matcher matcher2 = pt2.matcher(createdAt);
		
		//System.out.println(matcher1.find());
		
		if(matcher1.find() == true) {
			
			if(matcher2.find() == true) {
				System.out.println("오후 12시");
				
				String result2 = createdAt.replaceAll(".\\s(오후|오전)", "");
				System.out.println(result2);
				DateFormat df2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
				Date d2;
				try {
					d2 = df2.parse(result2);
					long time2 = d2.getTime();
					Timestamp ts2 = new Timestamp(time2);
					
					LocalDateTime date2 = ts2.toLocalDateTime().plusHours(9);
					System.out.println( date2+ ": " +date2.getClass().getSimpleName());  // 자바 변수 타입 확인
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			} else {
				System.out.println("오후");
				
				String result = createdAt.replaceAll(".\\s(오후|오전)", "");
				System.out.println(result);
				DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
				Date d;
				try {
					d = df.parse(result);
					long time = d.getTime();
					Timestamp ts = new Timestamp(time);
					
					LocalDateTime date = ts.toLocalDateTime().plusHours(21);
					System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			

			
		} else {
			System.out.println("오전");
			
			String result = createdAt.replaceAll(".\\s(오후|오전)", "");
			System.out.println(result);
			DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			Date d;
			try {
				d = df.parse(result);
				long time = d.getTime();
				Timestamp ts = new Timestamp(time);
				
				LocalDateTime date = ts.toLocalDateTime().plusHours(9);
				System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}

//String createdAtNatural = createdAtElement.text();
//// (?:^|[^\w\S])((?!20([0-9]([0-9])\d))(?:\d{4})\.(?:0[^0]|1[012])\.(?:0[^0]|[12]\d|3[01])\.? ?(오후|오전) ?(?:(?!2[4-9])[0-12]?\d:[0-5]\d:?[0-5]?\d?\b))(?:$|[^\w\S])
//// ..(?:(오전|오후) ?.[0-9]:?.[0-9])
//// (오후|오전) ?(?:(?!2[4-9])[0-12]?\\d:[0-5]\\d:?[0-5]?\\d?\\b)(?:$|[^\\w\\S])
//String result = createdAtNatural.replaceAll("(오후|오전) ?(?:(?!2[4-9])[0-12]?\\d:[0-5]\\d:?[0-5]?\\d?\\b)(?:$|[^\\w\\S])", "");
//DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
//Date d = df.parse(result);
//long time = d.getTime();
//Timestamp ts = new Timestamp(time);
//
//LocalDateTime date = ts.toLocalDateTime(); //.plusHours(9);
//Timestamp createdAt = Timestamp.valueOf(date);
