package com.ballis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.DTO.ProductAllDTO;
import com.ballis.model.DTO.ProductBuyDTO;
import com.ballis.model.DTO.ProductMethodDTO;
import com.ballis.model.DTO.ProductNewDTO;
import com.ballis.model.DTO.ProductOneDTO;
import com.ballis.model.DTO.ProductPopDTO;
import com.ballis.model.DTO.ProductSellDTO;
import com.ballis.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product findById(Long id) {
		return productRepository.findById(id).get();
		}

	public List<Product> findByWishMemberNumber(Member member) {
		return productRepository.findByWishMemberNumber(member);
	}
	
	public List<ProductNewDTO> getProductNew() {
		return productRepository.getProductNew();
	}
	
	public List<ProductPopDTO> getProductPop() {
		return productRepository.getProductPop();
	}
	
	public List<ProductOneDTO> getProductOne(Long productid) {
		return productRepository.getProductOne(productid);	
	}

	public List<ProductAllDTO> getProductAll(Integer sort) {
		return productRepository.getProductAll(sort);	
	}
	
//	public List<ProductAllDTO> searchProduct(String name) {
//		return productRepository.searchProduct(name);	
//	}
	
	public List<ProductAllDTO> searchProduct2(Integer sort, String name) {
		return productRepository.searchProduct2(sort, name);	
	}
	
	// 빠른 배송 상품 중 각 사이즈별 최저가 데이터만 출력
	public List<ProductBuyDTO> getFastProduct(Long productid) {
		return productRepository.getFastProduct(productid);
	}
	
	// 일반 배송 상품 중 각 사이즈별 최저가 데이터만 출력
	public List<ProductBuyDTO> getNormalProduct(Long productid) {
		return productRepository.getNormalProduct(productid);
	}
	
	// 각 사이즈별 빠른 배송, 일반 배송 상품이 둘다 존재할때 -> 판매가격이 더 낮은 데이터 출력
	public List<ProductBuyDTO> getBothProduct(Long productid) {
		List<ProductBuyDTO> fastProducts = getFastProduct(productid);
		List<ProductBuyDTO> normalProducts = getNormalProduct(productid);
		List<ProductBuyDTO> result = new ArrayList<>();
	    for (ProductBuyDTO fastProduct : fastProducts) {
	        for(ProductBuyDTO normalProduct : normalProducts) {
	            if(fastProduct.getSellProductSize().equals(normalProduct.getSellProductSize())) {
	                result.add(fastProduct.getSellWishPrice() <= normalProduct.getSellWishPrice() ? fastProduct : normalProduct);
	                break;
	            }
	        }
	    }
		return result;
	}
	
	// 각 사이즈별 빠른 배송, 일반 배송 상품이 둘다 존재할때 -> 판매가격이 더 낮은 데이터 출력
	// 빠른배송, 일반배송 중 하나만 존재할때 -> 하나만 존재하는것 출력
	public List<ProductBuyDTO> getCheaperProduct(Long productid) {
		List<ProductBuyDTO> fastProducts = getFastProduct(productid);
		List<ProductBuyDTO> normalProducts = getNormalProduct(productid);
		List<ProductBuyDTO> result = new ArrayList<>();
	    for (ProductBuyDTO fastProduct : fastProducts) {
	        boolean isSizeMatched = false;
	        for(ProductBuyDTO normalProduct : normalProducts) {
	            if(fastProduct.getSellProductSize().equals(normalProduct.getSellProductSize())) {
	                result.add(fastProduct.getSellWishPrice() <= normalProduct.getSellWishPrice() ? fastProduct : normalProduct);
	                isSizeMatched = true;
	                break;
	            }
	        }
	        if (!isSizeMatched) {
	            result.add(fastProduct);
	        }
	    }
	    for (ProductBuyDTO normalProduct : normalProducts) {
	        boolean isSizeMatched = false;
	        for(ProductBuyDTO fastProduct : fastProducts) {
	            if(normalProduct.getSellProductSize().equals(fastProduct.getSellProductSize())) {
	                isSizeMatched = true;
	                break;
	            }
	        }
	        if (!isSizeMatched) {
	            result.add(normalProduct);
	        }
	    }
		return result;
	}

	public List<ProductMethodDTO> getProductBySize(Long productid, Integer size) {
		return productRepository.getProductBySize(productid, size);
	}
	
	// 판매-대표정보
	public List<ProductSellDTO> getSellingProduct(Long productid) {
		return productRepository.getSellingProduct(productid);
	}

}
