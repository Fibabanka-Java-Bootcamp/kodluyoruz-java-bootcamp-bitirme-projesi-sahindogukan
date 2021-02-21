package com.kodluyoruz.bootcampproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeptPaymentAccountWsDto {

    private String accountIban;

    private Double amount;

    private String cartNumber;

}
