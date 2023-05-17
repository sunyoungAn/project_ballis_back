package com.ballis.model.admin.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AdminMemberInfoDTO {
	
	private Long memberNumber;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String phoneNumber;
	
	private Integer gender;
	
	private LocalDate birthDate;
	
	private Integer memberStatus;
	
	private LocalDateTime registDate;
	
	// 주소정보
	private List<AdminAddressDTO> addressDtoList;
	
	// 카드정보
	private List<AdminCardDTO> cardDtoList;
	
	private String bankName;
	
	private String accountNumber;
	
	private String depositor;

}
