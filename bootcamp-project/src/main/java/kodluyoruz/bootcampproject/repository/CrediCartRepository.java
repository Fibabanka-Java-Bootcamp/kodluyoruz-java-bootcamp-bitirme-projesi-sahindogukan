package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrediCartRepository extends JpaRepository<CrediCart, Long> {
    CrediCart findByCartId(Long cartId);
    CrediCart findByCartNumber(String cartNumber);
    List<CrediCart> findCrediCartsByClient(Client client);
}
