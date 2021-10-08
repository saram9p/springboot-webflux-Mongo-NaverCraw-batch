package com.cos.navernews.domain;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Document(collection = "test1")
//@Document(collection = "naver_realtime")
public class NaverNews {
	@Id
	private String _id;
	
	private String company;
	private String title;
	private Timestamp createAt;
}
