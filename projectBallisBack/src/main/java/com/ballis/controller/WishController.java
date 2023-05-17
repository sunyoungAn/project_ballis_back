package com.ballis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.service.MemberService;
import com.ballis.service.ProductService;
import com.ballis.service.WishService;

@RestController
public class WishController {
	
	@Autowired
	private WishService wishService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private  MemberService memberService;
	
	@GetMapping("/api/get/wish/{memberNumber}")
	public ResponseEntity wishProduct(@PathVariable Long memberNumber) {
		
//		Member member = memberService.findByMemberNumber(memberNumber);
//		Product product = productService.findById(id);
//		
//		List<Wish> lists = wishService.findByMemberMemberNumberAndProductId(member.getMemberNumber(), product.getId());
//		
//		return new ResponseEntity<>(lists, HttpStatus.OK);
		
//		 Member member = memberService.findByMemberNumber(memberNumber);
//		 Product product = productService.findById(id);
//		 if (product == null) {
//		   return ResponseEntity.notFound().build();
//		 }
//		 
//		 String productName = product.getProductKorName();
//		 // 가져온 속성값 활용
//		 return ResponseEntity.ok().body(productName);
		Member member = memberService.findByMemberNumber(memberNumber);
	    List<Product> products = productService.findByWishMemberNumber(member);
	    return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@DeleteMapping("/api/delete/wish/{id}")
	public ResponseEntity deleteWish(@PathVariable Long id) {
		wishService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
