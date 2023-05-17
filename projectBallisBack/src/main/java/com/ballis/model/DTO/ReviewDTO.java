package com.ballis.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	
	private Long reviewId;
	private String content;
	
	private String name;
	
	private String imagePath;
	private Integer mainImageDiv;
	private Long imageId;
	
	private Long productId;

}
