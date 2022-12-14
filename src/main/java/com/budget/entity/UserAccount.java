package com.budget.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor // 파라미터가 없는 생성자를 생성
@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
@Entity
public class UserAccount {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_PK")
    private UserPk userPk;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "TOTAL")
    private Integer total;

//    @ManyToOne
//    @JoinColumn(name = "ACCOUNT")
//    private Account account;
}
