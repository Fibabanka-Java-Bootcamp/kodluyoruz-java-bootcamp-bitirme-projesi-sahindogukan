package com.kodluyoruz.bootcampproject.controller;

import com.kodluyoruz.bootcampproject.entity.CartExtract;
import com.kodluyoruz.bootcampproject.entity.Product;
import com.kodluyoruz.bootcampproject.service.ShoppingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/shop")
@Api(value = "Shop")
public class ShoppingController {

    @Autowired
    ShoppingService shoppingService;

    @PostMapping("/buy")
    @ApiOperation(value = "Alışveriş işlemi yapma")
    public ResponseEntity<CartExtract> buy(@Valid @RequestBody Product product, @RequestParam String cartNumber){
        return shoppingService.buyProduct(product,cartNumber);
    }
}
