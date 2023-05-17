package com.ballis.model.admin.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AdminInquiryDetailDTO {
	
	private LocalDateTime inquiryRegistDate;
	
	private String inquiryStatusName;
	
	private String name;
	
	private String email;
	
	private String title;
	
	private String content;
	
	private String answer;
	
	private List<String> pathList;

}
