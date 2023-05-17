package com.ballis.service;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ballis.model.Brand;
import com.ballis.model.Image;
import com.ballis.model.Product;
import com.ballis.model.admin.DTO.AdminBrandDTO;
import com.ballis.model.admin.DTO.AdminImageDTO;
import com.ballis.model.admin.DTO.AdminProductDetailDTO;
import com.ballis.model.admin.DTO.AdminProductListPageDTO;
import com.ballis.model.admin.DTO.AdminProductRegisterDTO;
import com.ballis.model.admin.DTO.AdminProductSearchResultDTO;
import com.ballis.repository.BrandRepository;
import com.ballis.repository.ImageRepository;
import com.ballis.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminProductService {
	
	@Value("${projectBallisBack.upload.path}")
	private String uploadPath;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	/*
	 * 상품리스트 - 상품관리리스트페이지에 띄울 정보 모두 가져오기
	 */
	public AdminProductListPageDTO getPageInfo(Specification<Product> spec, Pageable pageable) {
		
		// 상품정보
		Page<Product> productList = productRepository.findAll(spec, pageable);
		
		// 브랜드정보
		List<AdminBrandDTO> brandList = brandRepository.findBrandCategory();
		
		// 상품의 브랜드 정보
		List<Product> targetProducts = productList.getContent();
		
		HashMap<Long, String> map = new HashMap<Long, String>();
		for(int i = 0; i<targetProducts.size(); i++) {
			map.put(targetProducts.get(i).getId(), targetProducts.get(i).getBrand().getBrandName());
		}
		
		AdminProductListPageDTO resultDto = new AdminProductListPageDTO();
		resultDto.setProductList(productList);
		resultDto.setBrandList(brandList);
		resultDto.setProductBrandMap(map);
		
		return resultDto;
	}

	/*
	 * 상품검색
	 */
	public AdminProductSearchResultDTO searchProduct(Specification<Product> spec, Pageable pageable) {

		// 상품정보
		Page<Product> productList = productRepository.findAll(spec, pageable);
		
		// 상품의 브랜드 정보
		List<Product> targetProducts = productList.getContent();
				
		HashMap<Long, String> map = new HashMap<Long, String>();
		for(int i = 0; i<targetProducts.size(); i++) {
			map.put(targetProducts.get(i).getId(), targetProducts.get(i).getBrand().getBrandName());
		}
		
		AdminProductSearchResultDTO resultDto = new AdminProductSearchResultDTO();
		resultDto.setProductList(productList);
		resultDto.setProductBrandMap(map);
		
		return resultDto;
	}

	/*
	 * 상품삭제
	 */
	@Transactional
	public void delete(List<Long> ids) {
		
		// id 갯수만큼 반복하기
		for(Long id : ids) {
			Product targetProduct = productRepository.findById(id).get();
			targetProduct.setDataStatus(2);
			targetProduct.setModifiedDate(LocalDateTime.now());
			
			productRepository.save(targetProduct) ;
			
			// 이미지 조회
			List<Image> targetImageList = imageRepository.findByTargetIdAndPageDiv(id, 1);
						
			if(targetImageList.isEmpty() == false) {
				for(Image targetImage : targetImageList) {
					// 이미지 삭제
					imageRepository.delete(targetImage);
								
					// 로컬데이터도 삭제
					try {
									
						File file = new File(uploadPath + URLDecoder.decode(targetImage.getImagePath(), "UTF-8"));
						file.delete();
									
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * 브랜드 카테고리정보 얻기
	 */
	@Transactional
	public List<AdminBrandDTO> getBrand() {
		return brandRepository.findBrandCategory();
	}

	/*
	 * 상품등록
	 */
	@Transactional
	public Product register(AdminProductRegisterDTO dto) {

		// 브랜드 정보 가져오기
		Brand targetBrand = brandRepository.findById(dto.getBrandId()).get();
		
		// 상품 정보 설정
		Product newProduct = new Product();
		newProduct.setProductEngName(dto.getProductEngName());
		newProduct.setProductKorName(dto.getProductKorName());
		newProduct.setBrand(targetBrand);
		newProduct.setCategory(dto.getCategory());
		if(dto.getModelNumber() != null && dto.getModelNumber() != "") {
			newProduct.setModelNumber(dto.getModelNumber());
		}
		if(dto.getLaunchingDate() != null) {
			newProduct.setLaunchingDate(dto.getLaunchingDate());
		}
		if(dto.getColor() != null && dto.getColor() != "") {
			newProduct.setColor(dto.getColor());
		}
		newProduct.setLaunchingPrice(dto.getLaunchingPrice());
		newProduct.setGender(dto.getGender());
		newProduct.setResellTarget(dto.getResellTarget());
		newProduct.setDataStatus(1);
		newProduct.setSizeMin(dto.getSizeMin());
		newProduct.setSizeMax(dto.getSizeMax());
		newProduct.setSizeUnit(dto.getSizeUnit());
		newProduct.setExplanation(dto.getExplanation());
		newProduct.setRegistDate(LocalDateTime.now());
		
		// 상품등록
		return productRepository.save(newProduct);	
	}
	
	// 이미지 업로드, 등록 처리
	public void registerImage(MultipartFile imageFile, Long id, Integer div) {
		
		String originalName = imageFile.getOriginalFilename();
		String fileName = originalName.substring(originalName.lastIndexOf("//")+1);
		
		System.out.println("fileName" + fileName);
		
		String uuid = UUID.randomUUID().toString();
		
		String saveName = uploadPath + File.separator + uuid + "_" + fileName;
        
        Path savePath = Paths.get(saveName);
        
        System.out.println(savePath);
        
        Image newImage = new Image();
        newImage.setPageDiv(1);
        newImage.setTargetId(id);
        newImage.setMainImageDiv(div);
//        newImage.setImagePath(savePath.toString()); // 경로전체 지정
        newImage.setImagePath(uuid + "_" + fileName); // uuid붙은 이름만 지정
        newImage.setRegistDate(LocalDateTime.now());
		
        // 이미지를 db에 저장
		imageRepository.save(newImage);
        
        try{
        	imageFile.transferTo(savePath);
            //uploadFile에 파일을 업로드 하는 메서드 transferTo(file)
        } catch (IOException e) {
             e.printStackTrace();
             //printStackTrace()를 호출하면 로그에 Stack trace가 출력됩니다.
        }
		
	}

	/*
	 * 상품상세정보 가져오기
	 */
	public AdminProductDetailDTO findProductOne(Long id) {

		Product targetProduct = productRepository.findById(id).get();
		
		AdminProductDetailDTO resultDto = new AdminProductDetailDTO();
		resultDto.setProductEngName(targetProduct.getProductEngName());
		resultDto.setProductKorName(targetProduct.getProductKorName());
		resultDto.setBrandId(targetProduct.getBrand().getBrandId());
		resultDto.setCategory(targetProduct.getCategory());
		resultDto.setModelNumber(targetProduct.getModelNumber());
		resultDto.setLaunchingDate(targetProduct.getLaunchingDate());
		resultDto.setColor(targetProduct.getColor());
		resultDto.setLaunchingPrice(targetProduct.getLaunchingPrice());
		resultDto.setSizeMin(targetProduct.getSizeMin());
		resultDto.setSizeMax(targetProduct.getSizeMax());
		resultDto.setSizeUnit(targetProduct.getSizeUnit());
		resultDto.setGender(targetProduct.getGender());
		resultDto.setResellTarget(targetProduct.getResellTarget());
		resultDto.setExplanation(targetProduct.getExplanation());
		
		List<AdminImageDTO> imageList = new ArrayList<>();
		List<Image> targetImage = imageRepository.findByTargetIdAndPageDiv(id, 1);
		for(Image image : targetImage) {
			AdminImageDTO dto = new AdminImageDTO();
			dto.setId(image.getId());
			dto.setPageDiv(image.getPageDiv());
			dto.setTargetId(image.getTargetId());
			dto.setImagePath(image.getImagePath());
			dto.setMainImageDiv(image.getMainImageDiv());
			imageList.add(dto);
			}
		
		resultDto.setImageDtoList(imageList);
		return resultDto;
	}

	/*
	 * 이미지 삭제하기
	 */
	@Transactional
	public void deleteImage(Long id) {
		
		Image targetImage = imageRepository.findByIdAndPageDiv(id, 1);
		String name = targetImage.getImagePath();
		
		// 이미지삭제
		imageRepository.delete(targetImage);
		
		// 실제파일도삭제
		try {
			
			File file = new File(uploadPath + URLDecoder.decode(name, "UTF-8"));
			file.delete();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 상품수정
	 */
	@Transactional
	public void editProduct(Long id, AdminProductRegisterDTO dto) {
		
		// 브랜드 정보 가져오기
		Brand targetBrand = brandRepository.findById(dto.getBrandId()).get();

		// 상품 정보 설정
		Product newProduct = productRepository.findById(id).get();
		newProduct.setProductEngName(dto.getProductEngName());
		newProduct.setProductKorName(dto.getProductKorName());
		newProduct.setBrand(targetBrand);
		newProduct.setCategory(dto.getCategory());
		if (dto.getModelNumber() != null && dto.getModelNumber() != "") {
			newProduct.setModelNumber(dto.getModelNumber());
		}
		if (dto.getLaunchingDate() != null) {
			newProduct.setLaunchingDate(dto.getLaunchingDate());
		}
		if (dto.getColor() != null && dto.getColor() != "") {
			newProduct.setColor(dto.getColor());
		}
		newProduct.setLaunchingPrice(dto.getLaunchingPrice());
		newProduct.setGender(dto.getGender());
		newProduct.setResellTarget(dto.getResellTarget());
		newProduct.setSizeMin(dto.getSizeMin());
		newProduct.setSizeMax(dto.getSizeMax());
		newProduct.setSizeUnit(dto.getSizeUnit());
		newProduct.setExplanation(dto.getExplanation());
		newProduct.setModifiedDate(LocalDateTime.now());

		// 상품수정
		productRepository.save(newProduct);
	}


}
