package com.ballis.model.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO {
	
	private Long id;
	private Long member;
	private Long memberNumber;
	private Integer category;
	private String title;
	private String content;
	private Integer inquiryStatus;
	private LocalDateTime inquiryRegistDate = LocalDateTime.now();

}
