package com.kodluyoruz.bootcampproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientWsDto {

    private String identityNumber;

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private String adress;
}
