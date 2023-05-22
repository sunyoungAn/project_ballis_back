package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.Selling;
import com.ballis.model.DTO.SellingAddDTO;
import com.ballis.model.DTO.SellingChartDTO;
import com.ballis.repository.MemberRepository;
import com.ballis.repository.ProductRepository;
import com.ballis.repository.SellingRepository;


import jakarta.transaction.Transactional;

@Service
public class SellingService {
	
	@Autowired
	private SellingRepository sellingRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ProductRepository productRepository;
	
	// 판매입찰 차트
		public List<SellingChartDTO> findByProduct_IdOrderByRegistDateDesc(Long productid) {
			List<Selling> sellings = sellingRepository.findByProduct_IdOrderByRegistDateDesc(productid);
		    Map<String, SellingChartDTO> sellingMap = new HashMap<>();
		    
		    for (Selling selling : sellings) {
		        Integer productSize = selling.getProductSize();
		        Integer wishPrice = selling.getWishPrice();
		        String key = productSize + "-" + wishPrice;
		        
		        // productSize, wishPrice 값이 동일하면 cnt 1 증가 후 배열에 넣지 않음
		        if (sellingMap.containsKey(key)) {
		            SellingChartDTO sellingChartDTO = sellingMap.get(key);
		            sellingChartDTO.setCnt(sellingChartDTO.getCnt() + 1);
		        } else {
		            SellingChartDTO sellingChartDTO = new SellingChartDTO(selling.getId(), productSize, wishPrice, 1);
		            sellingMap.put(key, sellingChartDTO);
		        }
		    }
		    
//		    // 오름차순 정렬
//		    List<SellingChartDTO> sellingChartDTOList = new ArrayList<>(sellingMap.values());
//		    sellingChartDTOList.sort(Comparator.comparing(SellingChartDTO::getWishPrice));
		    
//		    return sellingChartDTOList;
		
		   
		    return new ArrayList<>(sellingMap.values());
		}
		
		// 판매입찰, 보관판매 등록
		@Transactional
		public Selling save(SellingAddDTO sellingDto, String type) {
			Selling selling = new Selling();
			
			Member member = memberRepository.findById(sellingDto.getMemberNumber())
					.orElseThrow(()-> new IllegalArgumentException("member number를 찾을 수 없습니다."));
			selling.setMember(member);
			
			Product product = productRepository.findById(sellingDto.getProductId())
					.orElseThrow(()->new IllegalArgumentException("product id를 찾을 수 없습니다."));
			selling.setProduct(product);
			
			selling.setProductSize(sellingDto.getProductSize());
			selling.setWishPrice(sellingDto.getWishPrice());
			selling.setExpiryDate(sellingDto.getExpiryDate());	
			selling.setRegistDate(LocalDateTime.now());
			selling.setDataStatus(1);
			// 보관판매
			if("keep".equals(type)) {
				selling.setSellingStatus(11);	
				selling.setInventoryDiv(1);
			}
			// 판매입찰
			if("bid".equals(type)) {
				selling.setSellingStatus(1);	
				selling.setInventoryDiv(2);
			}
			return sellingRepository.save(selling);
		}
		
		
		// 판매입찰 거래체결
		@Transactional
		public Selling updateSelling(Long id, String type) {
			Selling selling = sellingRepository.findById(id)
					.orElseThrow(()->new IllegalArgumentException("selling id를 찾을 수 없습니다."));
			// 빠른배송
			if("fast".equals(type)) {
				selling.setSellingStatus(18);		
				selling.setModifiedDate(LocalDateTime.now());
			}
			// 즉시구매
			if("normal".equals(type)) {
				selling.setSellingStatus(99);
				selling.setModifiedDate(LocalDateTime.now());
			}
			return sellingRepository.save(selling);
		}
		
		// 빠른 배송 상품 존재 여부 확인
		public boolean hasStorageProduct(Long productId) {
		    List<Selling> sellings = sellingRepository.findByInventoryDivAndSellingStatusAndProductId(1, 11, productId);
		    return sellings != null && !sellings.isEmpty();
		}

		public List<Selling> findBySellingStatus(int sellingStatus) {
			return sellingRepository.findBySellingStatus(sellingStatus);
		}

		public List<Selling> findByMemberMemberNumber(Long memberNumber) {
			return sellingRepository.findByMemberMemberNumber(memberNumber);
		}
		
		public List<Selling> findByMemberMemberNumberAndInventoryDiv(Long memberNumber, int inventoryDiv) {
			return sellingRepository.findByMemberMemberNumberAndInventoryDiv(memberNumber, inventoryDiv);
		}


		public List<Selling> findSellingByMemberMemberNumberAndRegistDateBetween(Long memberNumber,
				LocalDateTime startDateTime, LocalDateTime endDateTime) {
			return sellingRepository.findSellingByMemberMemberNumberAndRegistDateBetween(memberNumber, startDateTime, endDateTime);
		}

		public void delete(Long id) {
			sellingRepository.deleteById(id);
			
		}

	
	

}
