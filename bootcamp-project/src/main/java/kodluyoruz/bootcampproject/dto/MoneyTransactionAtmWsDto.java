package com.kodluyoruz.bootcampproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MoneyTransactionAtmWsDto {

    private String cartNumber;
    private Double amount;
}
