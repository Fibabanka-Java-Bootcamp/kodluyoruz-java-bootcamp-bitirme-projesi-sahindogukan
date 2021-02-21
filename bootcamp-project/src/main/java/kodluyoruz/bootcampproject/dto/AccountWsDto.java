package com.kodluyoruz.bootcampproject.dto;

import com.kodluyoruz.bootcampproject.enumTypes.AccountCurrency;
import com.kodluyoruz.bootcampproject.enumTypes.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountWsDto {

    private String IBAN;

    private String accountType;

    private String accountCurrency;

    private Double balance = 0D;

}
