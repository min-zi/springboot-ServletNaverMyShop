package com.ming.snavermyshop;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성함
@RestController // ResponseBody + Controller, JSON으로 데이터를 주고받음을 선언함
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    /////////// DB에 신규 상품 등록 ///////////
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {

        Product product = this.productService.createProduct(requestDto); //this 생략 가능


        // 응답 보내기
        return product;
    }


    /////////// 관심상품 최저가 업데이트 API ///////////
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {

        Product product = productService.updateProduct(id, requestDto);


        // 응답 보내기 (업데이트된 상품 id)
        return product.getId();
    }


    /////////// 등록된 전체 상품 목록 조회 ///////////
    @GetMapping("/api/products")
    public List<Product> getProducts() throws SQLException {

        List<Product> products = productService.getProducts();


        // 응답 보내기
        return products;
    }
}