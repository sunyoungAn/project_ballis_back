package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ballis.model.Wish;

public interface WishRepository extends JpaRepository<Wish, Long> {
	
	List<Wish> findByMemberMemberNumber(Long memberNumber);

	List<Long> findProductIdByMemberMemberNumber(Long memberNumber);

}
