package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankCartRepository extends JpaRepository<BankCart, Long> {
    BankCart findByCartId(Long cartId);
    BankCart findByCartNumber(String cartNumber);
    BankCart findByAccount(Account account);
}
