package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminInquiryInfoDTO {

	private Long id;
	
	private Long memberNumber;
	
	private String name;
	
	private String email;
	
	private String category;
	
	private String title;
	
	private LocalDateTime inquiryRegistDate;
	
	private Integer inquiryStatus;
	
	private String inquiryStatusName;
	
	private LocalDateTime answerRegistDate;
	
}
