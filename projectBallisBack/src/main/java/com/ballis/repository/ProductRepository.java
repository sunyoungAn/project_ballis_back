package com.ballis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ballis.model.Member;
import com.ballis.model.Product;
import com.ballis.model.DTO.ProductAllDTO;
import com.ballis.model.DTO.ProductBuyDTO;
import com.ballis.model.DTO.ProductMethodDTO;
import com.ballis.model.DTO.ProductNewDTO;
import com.ballis.model.DTO.ProductOneDTO;
import com.ballis.model.DTO.ProductPopDTO;
import com.ballis.model.DTO.ProductSellDTO;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>  {

	@Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.wishs w WHERE w.member = :member")
	List<Product> findByWishMemberNumber(@Param("member") Member member);

	// 메인-신상품 목록
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductNewDTO"
	          + "(prod.id, prod.productEngName, img.imagePath, bd.brandName, sell.wishPrice) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId "
	            + "INNER JOIN Brand bd ON prod.brand = bd.brandId "
	            + "LEFT JOIN Selling sell ON prod.id = sell.product AND NOT (sell.sellingStatus = 99 OR sell.sellingStatus = 18) "
	            + "WHERE img.pageDiv = 1 AND img.mainImageDiv = 1 "
	            + "AND (sell.wishPrice IS NULL OR "
	            + "sell.wishPrice = ("
	            + "SELECT MIN(wishPrice) "
	            + "FROM Selling "
	            + "WHERE product = prod.id AND NOT (sellingStatus = 99 OR sellingStatus = 18))) "
	            + "AND (sell.inventoryDiv IS NULL OR sell.inventoryDiv = 1 OR "
	            + "(sell.inventoryDiv = 2 AND NOT EXISTS "
	            + "(SELECT 1 FROM Selling subSell WHERE prod.id = subSell.product "
	            + "AND sell.wishPrice = subSell.wishPrice "
	            + "AND subSell.inventoryDiv = 1 "
	            + "AND NOT (subSell.sellingStatus = 99 OR subSell.sellingStatus = 18)))) "
	            + "ORDER BY prod.launchingDate DESC "
	            + "LIMIT 12",
	            nativeQuery = false)
	List<ProductNewDTO> getProductNew();
	
	// 메인-인기상품 목록
	@Query(value="SELECT "
			+ "new com.ballis.model.DTO.ProductPopDTO"
			+ "(prod.id, prod.productEngName, MAX(img.imagePath), bd.brandName, sell.wishPrice, sell.inventoryDiv, COUNT(cont.id)) "
			+ "FROM Product prod "
            + "INNER JOIN Image img ON prod.id = img.targetId "
            + "INNER JOIN Brand bd ON prod.brand = bd.brandId "
            + "LEFT JOIN Selling sell ON prod.id = sell.product AND NOT (sell.sellingStatus = 99 OR sell.sellingStatus = 18) "
            + "LEFT JOIN Contract cont ON prod.id = cont.product "
            + "WHERE img.pageDiv = 1 AND img.mainImageDiv = 1 "
            + "AND (sell.wishPrice IS NULL OR "
            + "sell.wishPrice = ("
            + "SELECT MIN(wishPrice) "
            + "FROM Selling "
            + "WHERE product = prod.id AND NOT (sellingStatus = 99 OR sellingStatus = 18))) "
            + "AND (sell.inventoryDiv IS NULL OR sell.inventoryDiv = 1 OR "
            + "(sell.inventoryDiv = 2 AND NOT EXISTS "
            + "(SELECT 1 FROM Selling subSell WHERE prod.id = subSell.product "
            + "AND sell.wishPrice = subSell.wishPrice "
            + "AND subSell.inventoryDiv = 1 "
            + "AND NOT (subSell.sellingStatus = 99 OR subSell.sellingStatus = 18)))) "
            + "GROUP BY prod.id, prod.productEngName, bd.brandName, sell.wishPrice, sell.inventoryDiv "
            + "ORDER BY COUNT(cont.id) DESC "
            + "LIMIT 12",
			nativeQuery = false)
	List<ProductPopDTO> getProductPop();
	
	// 상세-상품 1개
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductOneDTO"
	          + "(prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.color, prod.launchingDate, prod.launchingPrice, "
	          + "img.imagePath, bd.brandName, buy.wishPrice, sell.wishPrice, sell.inventoryDiv, con.price) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId "
	            + "INNER JOIN Brand bd ON prod.brand = bd.brandId "
	            + "LEFT JOIN Buying buy ON prod.id = buy.product AND NOT buy.buyingStatus = 99 "
	            + "LEFT JOIN Selling sell ON prod.id = sell.product AND NOT (sell.sellingStatus = 99 OR sell.sellingStatus = 18) "
	            + "LEFT JOIN Contract con ON prod.id = con.product "
	            + "WHERE prod.id = :productid "
	            + "AND img.pageDiv = 1 "
	            + "AND (sell.wishPrice IS NULL OR sell.wishPrice = "
	            + "(SELECT MIN(wishPrice) "
	            + "FROM Selling "
	            + "WHERE product = prod.id AND NOT (sellingStatus = 99 OR sellingStatus = 18)) "
	            + "AND (buy.wishPrice IS NULL OR buy.wishPrice = "
	            + "(SELECT MAX(wishPrice) "
	            + "FROM Buying "
	            + "WHERE product = prod.id AND NOT buyingStatus = 99))) "
	            + "ORDER BY con.contractDate DESC "
	            + "LIMIT 1",
	            nativeQuery = false)
	List<ProductOneDTO> getProductOne(@Param("productid") Long productid);
	
	// 구매-보관상품 데이터만 출력
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductBuyDTO"
	          + "(prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, prod.sizeMax, prod.sizeUnit, "
	          + "img.imagePath, sell.sellingStatus, sell.productSize, MIN(sell.wishPrice), sell.inventoryDiv, sell.member.memberNumber, MIN(sell.id)) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId "
	            + "LEFT JOIN Selling sell ON prod.id = sell.product "
	            + "WHERE prod.id = :productid "
	            + "AND img.pageDiv = 1 AND img.mainImageDiv = 1 AND sell.inventoryDiv = 1 AND sell.sellingStatus = 11 "
	            + "AND sell.wishPrice = "
	            + "(SELECT MIN(sell2.wishPrice) FROM Selling sell2 "
	            + "WHERE sell2.product = prod.id AND sell2.productSize = sell.productSize "
	            + "AND sell2.inventoryDiv = 1 AND sell2.sellingStatus = 11) "	 
	            + "GROUP BY prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, "
	            + "prod.sizeMax, prod.sizeUnit, img.imagePath, sell.sellingStatus, sell.productSize, sell.inventoryDiv, sell.member.memberNumber "
				+ "ORDER BY MIN(sell.wishPrice) ASC"
	            ,nativeQuery = false)
	List<ProductBuyDTO> getFastProduct(@Param("productid") Long productid);
	
	// 구매-일반상품 데이터만 출력
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductBuyDTO"
	          + "(prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, prod.sizeMax, prod.sizeUnit, "
	          + "img.imagePath, sell.sellingStatus, sell.productSize, MIN(sell.wishPrice), sell.inventoryDiv, sell.member.memberNumber, MIN(sell.id)) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId "
	            + "LEFT JOIN Selling sell ON prod.id = sell.product "
	            + "WHERE prod.id = :productid "
	            + "AND img.pageDiv = 1 AND img.mainImageDiv = 1 AND sell.inventoryDiv = 2 AND sell.sellingStatus = 1 "
	            + "AND sell.wishPrice = "
	            + "(SELECT MIN(sell2.wishPrice) FROM Selling sell2 "
	            + "WHERE sell2.product = prod.id AND sell2.productSize = sell.productSize "
	            + "AND sell2.inventoryDiv = 2 AND sell2.sellingStatus = 1 ) "
	            + "GROUP BY prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, "
	            + "prod.sizeMax, prod.sizeUnit, img.imagePath, sell.sellingStatus, sell.productSize, sell.inventoryDiv, sell.member.memberNumber, sell.id "
				+ "ORDER BY MIN(sell.wishPrice) ASC"
	            ,nativeQuery = false)
	List<ProductBuyDTO> getNormalProduct(@Param("productid") Long productid);
	
	// 구매-즉시구매 또는 구매입찰, 판매-즉시판매 또는 판매입찰
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductMethodDTO"
	          + "(prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, img.imagePath, "
	          + "sell.sellingStatus, sell.productSize, MIN(sell.wishPrice), sell.inventoryDiv, sell.member.memberNumber, "
	          + "buy.buyingStatus, buy.productSize, MAX(buy.wishPrice), buy.member.memberNumber) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId AND img.pageDiv = 1 AND img.mainImageDiv = 1 "
	            + "LEFT JOIN Selling sell ON prod.id = sell.product "
	            + "AND sell.productSize = :size "
	            + "AND (sell.sellingStatus = 1 OR sell.sellingStatus = 11) "
	            + "AND (sell.inventoryDiv = 1 OR "
	            + "(sell.inventoryDiv = 2 AND NOT EXISTS "
	            + "(SELECT 1 FROM Selling subSell WHERE prod.id = subSell.product "
	            + "AND sell.wishPrice = subSell.wishPrice "
	            + "AND subSell.inventoryDiv = 1) "
	            + "OR (SELECT COUNT(*) FROM Selling subSell "
	            + "WHERE prod.id = subSell.product AND sell.wishPrice = subSell.wishPrice AND subSell.inventoryDiv = 2) = 1)) "
	            + "LEFT JOIN Buying buy ON prod.id = buy.product "
	            + "AND buy.productSize = :size "
	            + "AND buy.buyingStatus = 1 "
	            + "WHERE prod.id = :productid "
	            + "GROUP BY prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, img.imagePath, "
	            + "sell.sellingStatus, sell.productSize, sell.inventoryDiv, sell.member.memberNumber, "
	            + "buy.buyingStatus, buy.productSize, buy.member.memberNumber "
	            + "ORDER BY MIN(sell.wishPrice) ASC, MAX(buy.wishPrice) DESC",
	            nativeQuery = false)
	List<ProductMethodDTO> getProductBySize(@Param("productid") Long productid, @Param("size") Integer size);
	
	// 전체 판매상품 리스트
	@Query(value="SELECT "
			+ "new com.ballis.model.DTO.ProductAllDTO"
			+ "(prod.id, prod.productEngName, prod.productKorName, MAX(img.imagePath), bd.brandName, "
			+ "sell.wishPrice, sell.inventoryDiv, COUNT(DISTINCT wi.id), COUNT(DISTINCT re.id), "
			+ "prod.category, prod.gender, prod.brand.id, sell.productSize, prod.launchingDate, COUNT(DISTINCT con.id)) "
			+ "FROM Product prod "
            + "INNER JOIN Image img ON prod.id = img.targetId "
            + "INNER JOIN Brand bd ON prod.brand = bd.brandId "
            + "LEFT JOIN Selling sell ON prod.id = sell.product "
            + "LEFT JOIN Wish wi ON prod.id = wi.product "
            + "LEFT JOIN Review re ON prod.id = re.product "
            + "LEFT JOIN Contract con ON prod.id = con.product "        
            + "WHERE img.pageDiv = 1 AND img.mainImageDiv = 1 "
            // AND (sell.sellingStatus = 1 OR sell.sellingStatus = 11)
            + "AND sell.wishPrice = "
            + "(SELECT MIN(wishPrice) "
            + "FROM Selling "
            + "WHERE product = prod.id) "
            + "AND (sell.inventoryDiv = 1 OR "
            + "(sell.inventoryDiv = 2 AND NOT EXISTS "
            + "(SELECT 1 FROM Selling subSell WHERE prod.id = subSell.product "
            + "AND sell.wishPrice = subSell.wishPrice "
            + "AND subSell.inventoryDiv = 1))) "
            + "GROUP BY prod.id, prod.productEngName, prod.productKorName, bd.brandName, sell.wishPrice, sell.inventoryDiv, "
            + "prod.category, prod.gender, prod.brand, sell.productSize "
            + "ORDER BY "
            + "CASE :sort "
            + "WHEN 1 THEN COUNT(DISTINCT con.id) "
            + "WHEN 2 THEN COUNT(DISTINCT wi.id) "
            + "WHEN 3 THEN prod.launchingDate "
            + "WHEN 4 THEN COUNT(DISTINCT re.id) "
            + "ELSE COUNT(DISTINCT con.id) END DESC",
    		nativeQuery = false)
    List<ProductAllDTO> getProductAll(@Param("sort") Integer sort);
	
	// 판매-대표정보
	@Query(value="SELECT "
	          + "new com.ballis.model.DTO.ProductSellDTO"
	          + "(prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, prod.sizeMax, prod.sizeUnit, "
	          + "img.imagePath, buy.buyingStatus, buy.productSize, MAX(buy.wishPrice), buy.member.memberNumber, MIN(buy.id)) "
	            + "FROM Product prod "
	            + "INNER JOIN Image img ON prod.id = img.targetId "
	            + "LEFT JOIN Buying buy ON prod.id = buy.product "
	            + "WHERE prod.id = :productid "
	            + "AND img.pageDiv = 1 AND img.mainImageDiv = 1 AND buy.buyingStatus = 1 "
	            + "AND buy.wishPrice = "
	            + "(SELECT MAX(buy2.wishPrice) FROM Buying buy2 "
	            + "WHERE buy2.product = prod.id AND buy2.productSize = buy.productSize AND buy2.buyingStatus = 1 ) "
	            + "GROUP BY prod.id, prod.productEngName, prod.productKorName, prod.modelNumber, prod.sizeMin, prod.sizeMax, prod.sizeUnit, "
	            + "img.imagePath, buy.buyingStatus, buy.productSize, buy.member.memberNumber "
				+ "ORDER BY MAX(buy.wishPrice) DESC"
	            ,nativeQuery = false)
	List<ProductSellDTO> getSellingProduct(@Param("productid") Long productid);

}