package com.ballis.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Card;
import com.ballis.model.Member;
import com.ballis.service.CardService;
import com.ballis.service.MemberService;

@RestController
public class CardController {

	@Autowired
	private CardService cardService;
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping("/api/add/card/{memberNumber}")
	public ResponseEntity addCard(@PathVariable Long memberNumber, @RequestBody Map<String, String> params) {
		Member member = memberService.findByMemberNumber(memberNumber);
		
		if (member == null) {
	        return ResponseEntity.notFound().build();
	    }
		
		String cardNumber = params.get("cardNumber");
		String expiryYear = params.get("expiryYear");
		String expiryMonth = params.get("expiryMonth");
		String name = params.get("name");
		
		Card addcard = new Card();
		addcard.setMember(member);
		addcard.setCardNumber(cardNumber);
		addcard.setExpiryYear(expiryYear);
		addcard.setExpiryMonth(expiryMonth);
		addcard.setName(name);
		addcard.setDataStatus(1);
		addcard.setRegistDate(LocalDateTime.now());
		
		Card result = cardService.save(addcard);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/api/delete/card/{id}")
	public ResponseEntity deleteCard(@PathVariable Long id) {
		cardService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
