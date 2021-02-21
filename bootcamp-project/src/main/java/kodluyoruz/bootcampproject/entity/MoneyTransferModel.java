package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.enumTypes.TransferType;
import io.swagger.annotations.ApiModel;
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
@Table(name = "MONEY_TRANSFER_MODEL")
public class MoneyTransferModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long transferId;

    @Column(name = "TO_IBAN")
    @NotNull(message = "Gönderilen hesap IBAN bilgisi boş bırakılamaz.")
    private String toIBAN;

    @Column(name = "FROM_IBAN")
    @NotNull(message = "Gönderen hesap IBAN bilgisi boş bırakılamaz.")
    private String fromIBAN;

    @Column(name = "TRANSFER_AMOUNT")
    @NotNull(message = "Gönderilecek tutar boş bırakılamaz.")
    private Double transferAmout;

    @Column(name = "TRANSFER_TYPE")
    @NotNull(message = "Transfer tipi boş bırakılamaz.")
    private String transferType;
}
