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
@Table(name = "inquiry")
public class Inquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "member_number", nullable = false)
	private Member member;

	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer category;
	
	@Column(length = 100, nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column(nullable = false)
	private LocalDateTime inquiryRegistDate;
	
	@Column(columnDefinition = "TEXT")
	private String answer;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer inquiryStatus;
	
	private LocalDateTime answerRegistDate;
	
	private LocalDateTime answerModifiedDate;
	
	public Inquiry update(String title, Integer category, String content) {
		this.title=title;
		this.category=category;
		this.content=content;
		return this;
	}
	
}
