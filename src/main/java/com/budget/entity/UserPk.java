package com.budget.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserPk {
    @Id
    @Column(name = "USER_PK")
    private String userPk;
}
