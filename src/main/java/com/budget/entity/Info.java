package com.budget.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 파라미터가 없는 생성자를 생성
//@AllArgsConstructor // 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성
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
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "LOCALDATETIME")
    private LocalDateTime localDateTime = LocalDateTime.now();

    //@NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

