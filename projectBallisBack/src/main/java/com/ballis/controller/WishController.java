package com.ballis.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ballis.model.Image;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Wish;
import com.ballis.service.ImageService;
import com.ballis.service.MemberService;
import com.ballis.service.ProductService;
import com.ballis.service.WishService;

@RestController
public class WishController {
	
	@Value("${projectBallisBack.upload.path}")
	private String uploadPath;
	
	@Autowired
	private WishService wishService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ImageService imageService;
	
	
	//위시리스트 출력
	@GetMapping("/api/get/wish/{memberNumber}")
	public ResponseEntity wishProduct(@PathVariable Long memberNumber) {
		List<Map<String, Object>> List = new ArrayList<>();
		
		List<Wish> wishList = wishService.findByMemberMemberNumber(memberNumber);
		for (Wish wish : wishList) {
			Map<String, Object> wishMap = new HashMap<>();
			wishMap.put("wish", wish);
			
		    Member member = wish.getMember();
		    Long memberid = member.getMemberNumber();
		    wishMap.put("memberid", memberid);
		    
		    Product product = wish.getProduct();
		    String productName = product.getProductKorName();
		    Long productId = product.getId();
		    wishMap.put("productName", productName);
		    wishMap.put("productId", productId);
		    
		    List<Image> imagelist = imageService.findByTargetIdAndPageDiv(productId, 1); 
		    wishMap.put("imagelist", imagelist);
		    List.add(wishMap);		    
		    }
		
		return new ResponseEntity<>(List, HttpStatus.OK);
//	    List<Product> products = productService.findByWishMemberNumber(member);   
//	    List<Image> images = imageService.findByTargetId(memberNumber);
////	    return new ResponseEntity<>(products, HttpStatus.OK);
//	    
//	    
//	    return new ResponseEntity<>(Map.of("product", products, "image", images), HttpStatus.OK);
	}
	
	@PostMapping("/api/add/wish/{memberNumber}")
	public ResponseEntity addWish(@PathVariable Long memberNumber, @RequestParam Long productid ) {
		
		Member member = memberService.findByMemberNumber(memberNumber);
		Product product = productService.findById(productid);
		
		Wish wish = new Wish();
		wish.setMember(member);
		wish.setProduct(product);
		wish.setRegistDate(LocalDateTime.now());
		
		Wish result = wishService.save(wish);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/api/wish/display/image")
	public ResponseEntity<Resource> displayImage(@ModelAttribute Image image){
		String folder = "";
//		Resource resource = new FileSystemResource(path + folder + dto.getUuid()+"_"+dto.getFileName());
		Resource resource = new FileSystemResource(uploadPath + folder + image.getImagePath());
		if(!resource.exists()) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders header = new HttpHeaders();
		Path imagePath = null;
		try {
//			imagePath = Paths.get(path + folder + dto.getUuid()+"_"+dto.getFileName());
			imagePath = Paths.get(uploadPath + folder + image.getImagePath());
			header.add("Content-type", Files.probeContentType(imagePath));
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@DeleteMapping("/api/delete/wish/{id}")
	public ResponseEntity deleteWish(@PathVariable Long id) {
		wishService.delete(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
