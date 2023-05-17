package com.ballis.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ballis.model.Member;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class MemberSpecification {
	
	// 회원번호와 일치
	public static Specification<Member> equalMemberNumber(Long memberNumber) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("memberNumber"), memberNumber);
			}
		};
	}
	
	// like 이름
	public static Specification<Member> likeName(String name) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("name"), "%" + name + "%");
			}
		};
	}
	
	// like 이메일
	public static Specification<Member> likeEmail(String email) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("email"), "%" + email + "%");
			}
		};
	}
	
	// like 휴대폰번호
	public static Specification<Member> likePhoneNumber(String phoneNumber) {
		return new Specification<Member>() {
			@Override
			public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
			}
		};
	}

}
