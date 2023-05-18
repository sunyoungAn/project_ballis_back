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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "address")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer defaultAddress;
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;

}
