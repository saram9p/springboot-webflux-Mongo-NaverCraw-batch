package com.cos.navernews.test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class 시간반환 {

	public static Timestamp 타임스탬프반환(String createdAt) {
		
		Pattern pt1 = Pattern.compile("오전");
		Pattern pt2 = Pattern.compile("오후\\s12");
		//String resultf = createdAt.replaceAll(".\\s(오후|오전)\\s", "");
		
		Matcher matcher1 = pt1.matcher(createdAt);
		Matcher matcher2 = pt2.matcher(createdAt);
		
		//System.out.println(matcher1.find());
		
		if(matcher1.find() == true) {
			
			System.out.println("오전");
			
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
				System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
				
				Timestamp tcreatedAt = Timestamp.valueOf(date);
				
				return tcreatedAt;
			} catch (ParseException e) {
				e.printStackTrace();
			}
					
		} else {
			
			if(matcher2.find() == true) {
				System.out.println("오후 12시");
				
				String result = createdAt.replaceAll(".\\s(오후|오전)", "");
				System.out.println(result);
				DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
				Date d;
				try {
					d =  df.parse(result);
					
					// getTime()을 사용하여 java.util.Date를java.sql.Date로 변환
					long time = d.getTime();
					java.sql.Date date1 = new java.sql.Date(time);
					
					// Timestamp로 변환
					Timestamp ts = new Timestamp(date1.getTime());
					
					LocalDateTime date = ts.toLocalDateTime().plusHours(9);
					System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
					
					Timestamp tcreatedAt = Timestamp.valueOf(date);
					
					return tcreatedAt;
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
					
					// getTime()을 사용하여 java.util.Date를java.sql.Date로 변환
					long time = d.getTime();
					java.sql.Date date1 = new java.sql.Date(time);
					
					// Timestamp로 변환
					Timestamp ts = new Timestamp(date1.getTime());
					
					LocalDateTime date = ts.toLocalDateTime().plusHours(21);
					System.out.println( date+ ": " +date.getClass().getSimpleName());  // 자바 변수 타입 확인
					
					Timestamp tcreatedAt = Timestamp.valueOf(date);
					
					return tcreatedAt;
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		return null;
	}
	
}
