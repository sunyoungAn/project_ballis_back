package com.ballis.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Buying;
import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.DTO.BuyingAddDTO;
import com.ballis.model.DTO.BuyingChartDTO;
import com.ballis.repository.BuyingRepository;
import com.ballis.repository.MemberRepository;
import com.ballis.repository.ProductRepository;

import jakarta.transaction.Transactional;


@Service
public class BuyingService {
	
	@Autowired
	private BuyingRepository buyingRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ProductRepository productRepository;
	
	public List<Buying> findBuyingByRegistDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		return buyingRepository.findByRegistDateBetween(startDateTime, endDateTime);
	}

	public List<Buying> finByMemberMemberNumber(Member member) {
		return buyingRepository.findByMemberMemberNumber(member);
	}
	
	// 구매입찰 차트
		public List<BuyingChartDTO> findBuyingByProduct_Id(Long productid) {
			List<Buying> buyings = buyingRepository.findByProduct_Id(productid);
		    Map<String, BuyingChartDTO> buyingMap = new HashMap<>();
		    
		    for (Buying buying : buyings) {
		        Integer productSize = buying.getProductSize();
		        Integer wishPrice = buying.getWishPrice();
		        String key = productSize + "-" + wishPrice;
		        
		        if (buyingMap.containsKey(key)) {
		            BuyingChartDTO buyingChartDTO = buyingMap.get(key);
		            buyingChartDTO.setCnt(buyingChartDTO.getCnt() + 1);
		        } else {
		            BuyingChartDTO buyingChartDTO = new BuyingChartDTO(buying.getId(), productSize, wishPrice, 1);
		            buyingMap.put(key, buyingChartDTO);
		        }
		    }
		    
		    // 내림차순 정렬
		    List<BuyingChartDTO> buyingChartDTOList = new ArrayList<>(buyingMap.values());
		    buyingChartDTOList.sort(Comparator.comparing(BuyingChartDTO::getWishPrice).reversed());
		    
		    return buyingChartDTOList;
		}
		
		// 구매입찰 등록
		@Transactional
		public Buying save(BuyingAddDTO buyingDto) {
			Buying buying = new Buying();
			// member 객체 설정
			Member member = memberRepository.findById(buyingDto.getMemberNumber())
					.orElseThrow(()->new IllegalArgumentException("회원 아이디를 찾을 수 없습니다."));
			buying.setMember(member);
			// product 객체 설정
			Product product = productRepository.findById(buyingDto.getProductId())
					.orElseThrow(()->new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));
			buying.setProduct(product);
			// 나머지
			buying.setProductSize(buyingDto.getProductSize());
			buying.setWishPrice(buyingDto.getWishPrice());
			buying.setExpiryDate(buyingDto.getExpiryDate());
			buying.setRegistDate(LocalDateTime.now());
			buying.setBuyingStatus(1);
			buying.setDataStatus(1);
			
			return buyingRepository.save(buying);
		}
		
		// 구매입찰 거래 체결 - 즉시판매
		@Transactional
		public Buying updateBuying(Long id) {
			Buying buying = buyingRepository.findById(id)
					.orElseThrow(()->new IllegalArgumentException("buying id를 찾을 수 없습니다."));
			
			buying.setBuyingStatus(99);
			buying.setModifiedDate(LocalDateTime.now());
		
			return buyingRepository.save(buying);
		}

	

	public List<Buying> finByMemberMemberNumber(Long memberNumber) {
		return buyingRepository.findByMemberMemberNumber(memberNumber);
	}

	public List<Buying> findBuyingByMemberMemberNumberAndRegistDateBetween(Long memberNumber, LocalDateTime startDateTime, LocalDateTime endDateTime) {
			return buyingRepository.findBuyingByMemberMemberNumberAndRegistDateBetween(memberNumber, startDateTime, endDateTime);
	}
}
