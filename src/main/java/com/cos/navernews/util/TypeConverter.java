package com.cos.navernews.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeConverter {

	/**
	 * yyyy.MM.dd HH:mm 으로 변환
	 * 
	 * @param Target : Date 로 변환할 String 
	 * @return Date
	 */
	public static Date StringToDate(String Target) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
			Date d =  df.parse(Target);
			
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
