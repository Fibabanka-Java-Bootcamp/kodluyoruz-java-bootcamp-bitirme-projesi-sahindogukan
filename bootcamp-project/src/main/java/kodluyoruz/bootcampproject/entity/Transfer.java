package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TRANSFER")
@ApiModel(value = "Transfer Model")
public class Transfer {

    @Column(name = "TRANSFER_ID")
    @Id
    @JsonIgnore
    private Long transferId;

    @Column(name = "FROM_IBAN")
    @NotNull(message = "Alıcı icin IBAN no giriniz. ")
    private String fromIBAN;

    @Column(name = "TO_IBAN")
    @NotNull(message = "Gonderici icin IBAN no giriniz. ")
    private String toIBAN;

    @Column(name = "SENT_AMOUNT")
    @NotNull(message = "Gönderilecek tutarı giriniz. ")
    private Double sentAmount;


}
