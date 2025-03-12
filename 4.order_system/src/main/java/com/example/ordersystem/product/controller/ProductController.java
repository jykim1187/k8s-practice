package com.example.ordersystem.product.controller;

import com.example.ordersystem.product.domain.Product;
import com.example.ordersystem.product.dtos.ProductRegisterDto;
import com.example.ordersystem.product.dtos.ProductResDto;
import com.example.ordersystem.product.dtos.ProductSearchDto;
import com.example.ordersystem.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<?> productCreate(ProductRegisterDto dto){ //@requestbody로 안받는 것은 json이 아니라 폼데이터로 받는다는 말
         Product product = productService.productCreate(dto);
        return new ResponseEntity<>(product.getId(),HttpStatus.CREATED);
    }


    @GetMapping("/list")
    public ResponseEntity<?> productList(Pageable pageable, ProductSearchDto dto){

        Page<ProductResDto> productResDtos = productService.findAll(pageable,dto);
        return new ResponseEntity<>(productResDtos,HttpStatus.OK);
    }


}
