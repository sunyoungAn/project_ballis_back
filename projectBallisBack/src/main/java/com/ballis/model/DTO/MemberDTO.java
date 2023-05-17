package com.ballis.model.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MemberDTO {
	
	private String password;
	private String name;
	private String phoneNumber;
	private String bankName;
	private String accountNumber;
	private String depositor;
	private LocalDateTime modifiedDate = LocalDateTime.now();

}
