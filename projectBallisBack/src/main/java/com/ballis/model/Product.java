package com.ballis.model;

import java.time.LocalDate;
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
@Table(name = "product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100, nullable = false)
	private String productEngName;
	
	@Column(length = 100, nullable = false)
	private String productKorName;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer category;
	
	@Column(length = 50)
	private String modelNumber;
	
	private LocalDate launchingDate;
	
	@Column(length = 100)
	private String color;
	
	@Column(nullable = false)
	private Integer launchingPrice;
	
	@Column(columnDefinition = "TINYINT")
	private Integer gender;
	
	@Column(length = 255)
	private String explanation;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer resellTarget;
	
	@Column(columnDefinition = "TINYINT", nullable = false)
	private Integer dataStatus;
	
	@Column(nullable = false)
	private Integer sizeMin;
	
	@Column(nullable = false)
	private Integer sizeMax;
	
	@Column(nullable = false)
	private Integer sizeUnit;
	
	@Column(nullable = false)
	private LocalDateTime registDate;
	
	private LocalDateTime modifiedDate;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product", fetch=FetchType.LAZY)
	List<Wish> wishs = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product", fetch=FetchType.LAZY)
	List<Review> reviews = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product", fetch=FetchType.LAZY)
	List<Selling> sellings = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product", fetch=FetchType.LAZY)
	List<Buying> buyings = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy = "product", fetch=FetchType.LAZY)
	List<Contract> contracts = new ArrayList<>();

}
