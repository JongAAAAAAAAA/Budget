package com.budget.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserAccount {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")  // 컬럼 지정
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_PK") // 컬럼 지정
    private UserPk userPk;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT") //어떤 column과 연결이 될 지 설정
    private Account account;
}
