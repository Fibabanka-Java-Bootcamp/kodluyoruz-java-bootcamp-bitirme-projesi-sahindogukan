package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
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
@ApiModel(value = "Credit Cart Extract Model")
@Table(name = "EXTRACT")
public class CartExtract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long extractNo;

    @NotNull
    private String transactionName;

    @NotNull
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    @JsonIgnore
    private CrediCart crediCart;
}
