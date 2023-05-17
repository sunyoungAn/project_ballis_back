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
@Table(name = "review")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "member_number", nullable = false)
	private Member member;
	
	@Column(length = 50, nullable = false)
	private String name;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(length = 1500, nullable = false)
	private String content;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer dataStatus;
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;

}
