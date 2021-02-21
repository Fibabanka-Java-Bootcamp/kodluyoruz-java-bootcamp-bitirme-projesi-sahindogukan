package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByAccountNumber(Long accountNumber);
    Account findByIBAN(String ibanNo);
}
