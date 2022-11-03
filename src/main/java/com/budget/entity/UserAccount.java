package com.budget.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserAccount {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_PK")
    private UserPk userPk;

//    @ManyToOne
//    @JoinColumn(name = "ACCOUNT")
//    private Account account;

    @Id
    @Column(name = "Account")
    private String account;
}
