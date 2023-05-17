package com.ballis.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "target_selling_id")
	private Selling selling;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "target_contract_id")
	private Contract contract;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "member_number", nullable = false)
	private Member member;
	
	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(length = 255, nullable = false)
	private String address;
	
	@Column(length = 255, nullable = false)
	private String subAddress;
	
	@Column(length = 5, nullable = false)
	private String zipCode;
	
	@Column(length = 11, nullable = false)
	private String phoneNumber;
	
	@Column(length = 255)
	private String message;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer paymentType;
	
	@Column(length = 255, nullable = false)
	private Integer price;
	
	@Column(length = 255, nullable = false)
	private String impUid; // 결제번호

	@Column(length = 255, nullable = false)
	private String merchantUid; // 주문번호
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;

}
