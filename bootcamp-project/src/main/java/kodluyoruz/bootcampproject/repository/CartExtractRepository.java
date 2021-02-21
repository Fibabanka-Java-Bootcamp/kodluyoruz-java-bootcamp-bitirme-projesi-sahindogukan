package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.CartExtract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartExtractRepository extends JpaRepository<CartExtract,Long> {

    List<CartExtract> findByCrediCart(CrediCart crediCart);
}
