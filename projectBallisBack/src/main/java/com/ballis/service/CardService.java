package com.ballis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Card;
import com.ballis.repository.CardRepository;

@Service
public class CardService {

	@Autowired
	private CardRepository cardRepository;

	public Card save(Card addcard) {
		return cardRepository.save(addcard);
	}

	public void delete(Long id) {
		cardRepository.deleteById(id);
	}
}
