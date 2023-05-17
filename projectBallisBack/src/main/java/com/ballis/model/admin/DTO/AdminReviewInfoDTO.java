package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminReviewInfoDTO {
	
	private Long id;
	
	private Long productId;
	
	private String productEngName;
	
	private String productKorName;
	
	private Long memberNumber;
	
	private String name;
	
	private String content;
	
	private LocalDateTime registDate;

}
