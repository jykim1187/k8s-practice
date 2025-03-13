package com.example.ordersystem.product.service;


import com.example.ordersystem.common.service.StockInventoryService;
import com.example.ordersystem.product.domain.Product;
import com.example.ordersystem.product.dtos.ProductRegisterDto;
import com.example.ordersystem.product.dtos.ProductResDto;
import com.example.ordersystem.product.dtos.ProductSearchDto;
import com.example.ordersystem.product.dtos.ProductUpdateStockDto;
import com.example.ordersystem.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.kafka.common.protocol.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final StockInventoryService stockInventoryService;


    public ProductService(ProductRepository productRepository, StockInventoryService stockInventoryService) {
        this.productRepository = productRepository;

        this.stockInventoryService = stockInventoryService;

    }

    public Product productCreate (ProductRegisterDto dto){
//         try {
//             //      member조회
//             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

// //
//             Product product = productRepository.save(dto.toEntity(authentication.getName()));
// //            레디스에도 재고 추가
//             stockInventoryService.increaseStock(product.getId(), dto.getStockQuantity());
// //            그런게 레디스는 아래에서 에러가 떠도 레디스는 트랜잭션 롤백이 안된다.(트랜잭션은 rdb의 개념이기 떄문)
// //            그래서 에러가 날 시 레디스에서 다시 재고를 빼도록 try-catch 처리가 필요함.


//             //       aws에 image저장 후에 url추출
// //               aws에 s3접근 가능한 iam계정(새끼 계정 같은 느낌) 생성, iam계정을 통해 aws에 접근가능한 접근 객체 생성
// //
//             MultipartFile image = dto.getProductImage();
//             byte[] bytes = image.getBytes();
//             String fileName = product.getId() + " "+ image.getOriginalFilename(); //여따 파일명에 아이디 값 불이려고 위에서 product뽑아냅
// //    먼저 local에 저장
//             Path path = Paths.get("C:/Users/Playdata/Desktop/tmp/",fileName);
//             Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
//             return product;
//         } catch (IOException e){
//             throw new RuntimeException("이미지 저장 실패");// 롤백처리를 위해 checkException으로 던져줘야함
//         }
        return null;
    }

    public Page<ProductResDto> findAll(Pageable pageable, ProductSearchDto searchDto) {
//        검색을 위해 Specification 객체 사용
//        Specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성
        Specification<Product> specification = new Specification<Product>() {  //==>Specification인터페이스를 즉석에서 구현하여 인터페이스의 추상메서드를 구현
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//   Predicate 는 조건식을 표현하는 객체로, 주로
                //              root : 엔티티의 속성을 접근하기 위한 객체, criteriabuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();
                if (searchDto.getCategory() != null) {//카테고리 있다면 카테고리로 검색
                    predicates.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
                    //root.get("") 겟 안에는 엔티티 칼럼값
                }
                if(searchDto.getProductName() != null){//이름이 널이 아니라
                    predicates.add(criteriaBuilder.like(root.get("name"),"%"+searchDto.getProductName()+"%"));
//                    name like '% %'
                }
//               Predicate는 원래 배열을 요구하는데
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for(int i=0; i< predicates.size(); i++){
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

       Page<Product> productList = productRepository.findAll(specification,pageable);
               return productList.map(p->p.fromEntity());
   }

   public ProductResDto productDetail(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없네"));
        return product.fromEntity();
   }

   public Product updateStockQuantity(ProductUpdateStockDto dto){
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(()->new EntityNotFoundException());
        product.updateStockQuantity(dto.getProductQuantity());
        return product;
   }
   @KafkaListener(topics = "update-stock-topic", containerFactory = "kafkaListner")
   public void productConsumer(String message){

       System.out.println("consumer 메세지 수신" + message);
        ObjectMapper objectMapper = new ObjectMapper();
       try {
           ProductUpdateStockDto dto = objectMapper.readValue(message,ProductUpdateStockDto.class);
           this.updateStockQuantity(dto);
       } catch (JsonProcessingException e) {
           throw new RuntimeException(e);
       }

   }


    }
