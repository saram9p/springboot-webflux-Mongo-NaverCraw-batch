package com.cos.navernews.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class TimestampToStringConverter implements Converter<Timestamp, String> {

	@Override
	public String convert(Timestamp source) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		return sdf.format(source);
	}

}
