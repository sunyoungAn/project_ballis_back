package com.ballis.model.admin.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AdminMemberDTO {
	
	private String name;
	
	private String password;
	
	private String phoneNumber;
	
	private Integer gender;
	
	private LocalDate birthDate;
	
	private Integer memberStatus;
	
	private String bankName;
	
	private String accountNumber;
	
	private String depositor;

}
