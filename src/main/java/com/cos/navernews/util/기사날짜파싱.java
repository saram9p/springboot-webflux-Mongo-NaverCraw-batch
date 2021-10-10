package com.cos.navernews.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class 기사날짜파싱 {

	public Date 파싱후Date반환(String createdAt) {
		
		// 기사 날짜가 오후/오전으로 나누어져서 조건을 검
		Pattern pt1 = Pattern.compile("오전");
		Pattern pt2 = Pattern.compile("오후\\s12");
		
		Matcher matcher1 = pt1.matcher(createdAt);
		Matcher matcher2 = pt2.matcher(createdAt);
		

			
		// 기사 날짜에 오후 12시가 들어갈 경우 
		if(matcher2.find() == true) {
			System.out.println("오후 12시");
			
			String createdAtParse = createdAt.replaceAll(".\\s(오후|오전)", "");
			
			try {
				Date d = TypeConverter.StringToDate(createdAtParse);
				
				// 오전이라서 12시간을 감소 // Calendar 클래스를 쓰면 노란색 에러가 뜨지 않을수도
				d.setHours(d.getHours() - 12);
				return d;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(matcher1.find() == true) {
			System.out.println("오전");
			
			String createdAtParse = createdAt.replaceAll(".\\s(오후|오전)", "");
			
			try {
				Date d = TypeConverter.StringToDate(createdAtParse);
				
				return d;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 기사 날짜에 오후가 들어갈 경우

			System.out.println("오후");
			
			String createdAtParse = createdAt.replaceAll(".\\s(오후|오전)", "");
			
			try {
				Date d = TypeConverter.StringToDate(createdAtParse);
					
				// 오후라서 12시간을 추가 // Calendar 클래스를 쓰면 노란색 에러가 뜨지 않을수도
				d.setHours(d.getHours() + 12);
				
				return d;
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
			

							

			
		return null;
	} // end of 데이트반환
	
}
