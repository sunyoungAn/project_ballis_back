package com.ballis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ballis.model.Card;

public interface CardRepository  extends JpaRepository<Card, Long> {

}
