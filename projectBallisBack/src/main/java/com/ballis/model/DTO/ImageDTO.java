package com.ballis.model.DTO;

import lombok.Data;

@Data
public class ImageDTO {
	
	private Integer pageDiv;
	private Long targetId;
	private String imagePath;
	private Integer mainImageDiv;

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String uuid;
	private String fileName;
	private String contentType;
	
	public ImageDTO(String uuid, String fileName, String contentType) {
		this.uuid = uuid;
		this.fileName = fileName;
		this.contentType = contentType;
	}

}
