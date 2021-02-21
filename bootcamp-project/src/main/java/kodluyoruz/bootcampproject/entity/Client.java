package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@ApiModel(value = "Client Model")
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @NotNull(message = "Bu alan zorunludur.")
    @Column(name = "IDENTITY_NUMBER", unique = true)
    @Pattern(
            regexp = "^[1-9]{1}[0-9]{9}[02468]{1}$",
            message = "Lütfen geçerli bir TC kimlik numarası giriniz."
    )
    private String identityNumber;

    @Column(name = "NAME")
    @NotNull(message = "Bu alan zorunludur.")
    @Size(min = 2, max = 25, message = "İsim alanı 2-25 karakter arası sınırlandırılmıştır.")
    private String name;

    @Column(name = "SURNAME")
    @NotNull(message = "Bu alan zorunludur.")
    @Size(min = 2, max = 25, message = "Soyisim alan 2-25 karakter arası sınırlandırılmıştır.")
    private String surname;

    @Column(name = "EMAIL")
    @NotNull(message = "Bu alan zorunludur.")
    @Email(message = "Lütfen geçerli formatta bir mail adresi giriniz.")
    private String email;

    @Column(name = "PHONE_NUMBER")
    @Pattern(
            regexp = "(05|5)[0-9][0-9][1-9]([0-9]){6}",
            message = "Lütfen başında '0' olmadan geçerli formatta bir telefon numarası giriniz."
    )
    @NotNull(message = "Bu alan zorunludur.")
    private String phoneNumber;

    @Column(name = "ADRESS")
    @NotNull(message = "Bu alan zorunludur.")
    private String adress;

    @OneToMany(
    cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "client")
    @JsonIgnore
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "client")
    @JsonIgnore
    private List<CrediCart> crediCarts = new ArrayList<>();

}
