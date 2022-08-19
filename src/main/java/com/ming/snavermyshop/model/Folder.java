package com.ming.snavermyshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줌
@NoArgsConstructor // 기본 생성자를 만들어줌
@Entity // DB 테이블 역할을 함
public class Folder {

    // ID가 자동으로 생성 및 증가함
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // 폴더명은 반드시 값을 가지도록 함
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    // 폴더를 만들어주는 생성자
    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }
}