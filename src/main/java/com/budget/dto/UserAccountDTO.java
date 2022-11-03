package com.budget.dto;

import lombok.Data;

@Data
public class UserAccountDTO {
    String userPk, account;
    Integer total;
}
