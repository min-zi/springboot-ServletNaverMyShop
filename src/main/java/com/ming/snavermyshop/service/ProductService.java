package com.ming.snavermyshop.service;

import com.ming.snavermyshop.model.Product;
import com.ming.snavermyshop.dto.ProductMypriceRequestDto;
import com.ming.snavermyshop.repository.ProductRepository;
import com.ming.snavermyshop.dto.ProductRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product createProduct(ProductRequestDto requestDto, Long userId) {
        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto, userId);

        productRepository.save(product);

        return product;
    }


    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) {

        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));

        int myprice = requestDto.getMyprice();
        product.setMyprice(myprice);
        productRepository.save(product);

        return product;
    }


    public List<Product> getProducts(Long userId) {

        return productRepository.findAllByUserId(userId);
    }
}
