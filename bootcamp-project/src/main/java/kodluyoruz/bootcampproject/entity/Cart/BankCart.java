package com.kodluyoruz.bootcampproject.entity.Cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "BANK_CART")
public class BankCart {

    @Column(name = "CART_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long cartId;

    @Column(name = "CART_NUMBER")
    @NotNull(message = "Bu alan zorunludur.")
    private String cartNumber;

    @Column(name = "CART_BALANCE")
    private Double balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    @JsonIgnore
    private Account account;

}
