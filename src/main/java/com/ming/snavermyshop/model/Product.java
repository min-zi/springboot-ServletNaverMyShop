package com.ming.snavermyshop.model;

import com.ming.snavermyshop.dto.ProductRequestDto;
import com.ming.snavermyshop.validator.ProductValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줌
@NoArgsConstructor // 기본 생성자를 만들어줌
@Entity // DB 테이블 역할을 함
public class Product {

    // ID가 자동으로 생성 및 증가함
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // 반드시 값을 가지도록 함
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    @Column(nullable = false)
    private Long userId;

    @ManyToMany
    private List<Folder> folderList;

    // 관심 상품 만들어주는 생성자
    public Product(ProductRequestDto requestDto, Long userId) {
        // 입력값 Validation
        ProductValidator.validateProductInput(requestDto, userId);

        // 관심상품을 등록한 회원 테이블 Id 저장
        this.userId = userId;
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.myprice = 0;
    }

    public void addFolder(Folder folder) {
        this.folderList.add(folder);
    }
}