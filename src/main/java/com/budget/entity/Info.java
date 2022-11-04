package com.budget.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class Info {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SPENDING")
    private Integer spending;

    @Column(name = "INCOME")
    private Integer income;

    //@NotNull
    @Column(name = "LOCALDATETIME")
    private LocalDateTime localDateTime = LocalDateTime.now();

    //@NotNull
    @Column(name = "LOCALDATE")
    private LocalDate localDate;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ACCOUNT")
    private String account;

    @ManyToOne
    @JoinColumn(name = "USER_PK")
    private UserPk userPk;
}
