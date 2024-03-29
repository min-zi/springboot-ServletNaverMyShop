package com.ming.snavermyshop.controller;

import com.ming.snavermyshop.dto.ProductMypriceRequestDto;
import com.ming.snavermyshop.dto.ProductRequestDto;
import com.ming.snavermyshop.model.Product;
import com.ming.snavermyshop.model.User;
import com.ming.snavermyshop.model.UserRoleEnum;
import com.ming.snavermyshop.security.UserDetailsImpl;
import com.ming.snavermyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


// @RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성함
@RestController // ResponseBody + Controller, JSON으로 데이터를 주고받음을 선언함
public class ProductController {

    private final ProductService productService;

    @Autowired // 사실 생성자가 하나이기 때문에 생략 가능
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    /////////// DB에 신규 상품 등록 ///////////
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();

        Product product = this.productService.createProduct(requestDto, userId); //this 생략 가능


        // 응답 보내기
        return product;
    }

    /////////// 관심상품 최저가 업데이트 API ///////////
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        Product product = productService.updateProduct(id, requestDto);


        // 응답 보내기 (업데이트된 상품 id)
        return product.getId();
    }


    /////////// 로그인한 회원이 등록한 관심 상품 조회 ///////////
    @GetMapping("/api/products")
    public Page<Product> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 로그인 되어 있는 회원 테이블의 ID
        Long userId = userDetails.getUser().getId();
        page = page - 1;

        return productService.getProducts(userId, page, size, sortBy, isAsc);
    }

    /////////// 관리자용 관심 상품 전체 조회 ///////////
    @Secured(UserRoleEnum.Authority.ADMIN)
    @GetMapping("/api/admin/products")
    public Page<Product> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        page = page - 1;
        return productService.getAllProducts(page, size, sortBy, isAsc);
    }

    /////////// 상품에 폴더 추가 ///////////
    @PostMapping("/api/products/{productId}/folder")
    public Long addFolder(
            @PathVariable Long productId,
            @RequestParam Long folderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        Product product = productService.addFolder(productId, folderId, user);
        return product.getId();
    }
}