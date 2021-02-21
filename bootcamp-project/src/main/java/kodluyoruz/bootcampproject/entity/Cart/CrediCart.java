package com.kodluyoruz.bootcampproject.entity.Cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.CartExtract;
import com.kodluyoruz.bootcampproject.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CREDI_CART")
public class CrediCart {

    @Column(name = "CART_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long cartId;

    @Column(name = "CART_NUMBER")
    private String cartNumber;

    @Column(name = "CART_DEBT")
    private Double cartDebt = 0D;

    @Column(name = "CART_LIMIT")
    @NotNull(message = "Bu alan zorunludur.")
    private Double cartLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="client_id")
    @JsonIgnore
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "crediCart")
    @JsonIgnore
    private List<CartExtract> extracts;

}
