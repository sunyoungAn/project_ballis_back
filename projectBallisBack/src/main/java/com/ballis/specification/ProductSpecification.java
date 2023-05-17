package com.ballis.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProductSpecification {
	
	// 인덱스와 일치
	public static Specification<com.ballis.model.Product> equalId(Long id) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// like 상품명(영문)
	public static Specification<Product> likeProductEngName(String name) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("productEngName"), "%" + name + "%");
			}
		};
	}
	
	// like 상품명(한글)
	public static Specification<Product> likeProductKorName(String name) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("productKorName"), "%" + name + "%");
			}
		};
	}
	
	// 브랜드키값과 일치
	public static Specification<Product> equalBrandId(Long brandId) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("brand").get("brandId"), brandId);
			}
		};
	}
	
	// 카테고리와 일치
	public static Specification<Product> equalCategory(Integer category) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("category"), category);
			}
		};
	}
	
	// 데이터상태여부와 일치
	public static Specification<Product> equalDataStatus(Integer status) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("dataStatus"), status);
			}
		};
	}
	
	// 상품명(영문)과 상품명(한글)중에 하나라도 포함되어 있는지
	public static Specification<Product> orLikeName(String name) {
		return new Specification<Product>() {
			@Override
			public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				Predicate equalEngName = criteriaBuilder.like(root.get("productEngName"), "%" + name + "%");
				Predicate equalKorName = criteriaBuilder.like(root.get("productKorName"), "%" + name + "%");
				
				return criteriaBuilder.or(equalEngName, equalKorName);
			}
		};
	}

}
