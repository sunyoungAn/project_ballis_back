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
@Table(name = "card")
public class Card {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "member_number", nullable = false)
	private Member member;

	@Column(length = 16, nullable = false)
	private String cardNumber;
	
	@Column(length = 4, nullable = false)
	private String expiryYear;
	
	@Column(length = 2, nullable = false)
	private String expiryMonth;
	
	@Column(length = 50, nullable = false)
	private String name;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer dataStatus;
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;

}
