package com.ming.snavermyshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줌
@NoArgsConstructor // 기본 생성자를 만들어줌
@Entity // DB 테이블 역할을 함
public class User {

    // ID가 자동으로 생성되고 증가함
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id // PK로 설정
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // enum을 string으로 변환해서 저장
    private UserRoleEnum role;

    @Column(unique = true)
    private Long kakaoId;

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = null;
    }

    public User(String username, String password, String email, UserRoleEnum role, Long KakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = KakaoId;
    }
}