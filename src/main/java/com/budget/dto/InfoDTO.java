package com.budget.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InfoDTO {
    Integer money;
    String userPk, content, account;
    LocalDateTime localDateTime;
}
