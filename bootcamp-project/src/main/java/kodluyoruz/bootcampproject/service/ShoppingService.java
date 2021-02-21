package com.kodluyoruz.bootcampproject.service;

import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.CartExtract;
import com.kodluyoruz.bootcampproject.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ShoppingService {

    @Autowired
    AccountService accountService;
    @Autowired
    CrediCartService crediCartService;

    public ResponseEntity<CartExtract> buyProduct(Product product, String cartNumber) {

        CartExtract cartExtract = new CartExtract();
        cartExtract.setTotalAmount(product.getProductTotalPrice());
        cartExtract.setTransactionName(product.getProductName());

        CrediCart crediCart = crediCartService.getCrediCartByCartNumber(cartNumber);
        crediCart.setCartDebt(crediCart.getCartDebt()+product.getProductTotalPrice());
        crediCart.getExtracts().add(cartExtract);

        cartExtract.setCrediCart(crediCart);

        crediCartService.saveCrediCart(crediCart);

        return new ResponseEntity<>(cartExtract, HttpStatus.CREATED);

    }
}
