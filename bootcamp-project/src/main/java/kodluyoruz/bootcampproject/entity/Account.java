package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.enumTypes.AccountCurrency;
import com.kodluyoruz.bootcampproject.enumTypes.AccountType;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@ApiModel(value = "Account Model")
@Table(name = "ACCOUNT")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long accountNumber;

    @Column(name = "IBAN")
    private String IBAN;

    @Column(name = "ACCOUNT_TYPE")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bu alan zorunludur.")
    private AccountType accountType;

    @Column(name = "ACCOUNT_CURRENCY")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Bu alan zorunludur.")
    private AccountCurrency accountCurrency;

    @Column(name = "BALANCE")
    private Double balance = 0D;

    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.DATE)
    private Date createdDate = new Date();

    @OneToOne(
            cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "account")
    @JsonIgnore
    private BankCart bankCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    @JsonIgnore
    private Client client;
}
