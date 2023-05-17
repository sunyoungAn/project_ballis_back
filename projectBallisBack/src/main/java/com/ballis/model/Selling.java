package com.ballis.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "selling")
public class Selling {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@Column(nullable = false)
	private Integer productSize;
	
	@Column(nullable = false)
	private Integer wishPrice;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "member_number", nullable = false)
	private Member member;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer sellingStatus;
	
	@Column(nullable = false)
	private LocalDateTime expiryDate;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer inventoryDiv;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer dataStatus;
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "selling", fetch=FetchType.LAZY)
	List<Contract> contracts = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "selling", fetch=FetchType.LAZY)
	List<Payment> payments = new ArrayList<>();

}
