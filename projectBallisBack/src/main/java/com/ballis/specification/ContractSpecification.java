package com.ballis.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Contract;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ContractSpecification {
	
	// 인덱스와 일치
	public static Specification<Contract> equalId(Long id) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("id"), id);
			}
		};
	}
	
	// 상품아이디와 일치
	public static Specification<Contract> equalProductId(Long productId) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("product").get("id"), productId);
			}
		};
	}
	
	// 구매자회원번호와 일치
	public static Specification<Contract> equalBuyerNumber(Long buyerNumber) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("buyerNumber"), buyerNumber);
			}
		};
	}
	
	// 판매자회원번호와 일치
	public static Specification<Contract> equalSellerNumber(Long sellerNumber) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("sellerNumber"), sellerNumber);
			}
		};
	}
	
	// 판매상태와 일치
	public static Specification<Contract> equalSellingStatus(Integer sellingStatus) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("sellingStatus"), sellingStatus);
			}
		};
	}
	
	// 구매상태와 일치
	public static Specification<Contract> equalBuyingStatus(Integer buyingStatus) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("buyingStatus"), buyingStatus);
			}
		};
	}
	
	// 거래체결일(검색기준시작)보다 크거나 같다
	public static Specification<Contract> greaterThanOrEqualToContractDateStart(LocalDateTime contractDateStart) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("contractDate"), contractDateStart);
			}
		};
	}
	
	// 거래체결일(검색기준끝)보다 작거나 같다 
	public static Specification<Contract> lessThanOrEqualToContractDateEnd(LocalDateTime contractDateEnd) {
		return new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("contractDate"), contractDateEnd);
			}
		};
	}

}
