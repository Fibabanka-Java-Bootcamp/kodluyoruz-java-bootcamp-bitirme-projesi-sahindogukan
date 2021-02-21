package com.kodluyoruz.bootcampproject.controller;

import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.CartExtract;
import com.kodluyoruz.bootcampproject.entity.MoneyTransferModel;
import com.kodluyoruz.bootcampproject.enumTypes.TransferType;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.service.BankCartService;
import com.kodluyoruz.bootcampproject.service.CrediCartService;
import com.kodluyoruz.bootcampproject.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/cart")
@Api(value = "Bank/Credit Cart")
public class CartController {

    @Autowired
    CrediCartService crediCartService;
    @Autowired
    BankCartService bankCartService;
    @Autowired
    TransferService transferService;

    @PostMapping("/create/crediCart")
    @ApiOperation(value = "Mevcut bir kullanıcı için kredi kartı oluşturma")
    public CrediCart createCrediCart(@RequestParam(value = "id") String identityNumber,
                                     @RequestBody Double limit) {
        return crediCartService.createCrediCart(identityNumber, limit);
    }

    @PostMapping("/create/bankCart")
    @ApiOperation(value = "Mevcut bir kullanıcının hesabı için banka kartı(ön ödemeli kart) oluşturma")
    public BankCart createBankCart(@RequestBody Long accountId) {
        return bankCartService.createBankCart(accountId);
    }

    @PostMapping("/crediCart/update/limit")
    @ApiOperation(value = "Kredi kartı limiti güncelleme")
    public CrediCart updateCrediCartLimit(@RequestParam(value = "cartNumber") String cartNumber,
                                          @RequestBody Double newlimit) {
        return crediCartService.updateCrediCartLimit(cartNumber, newlimit);
    }

    @DeleteMapping("/crediCart/delete")
    @ApiOperation(value = "Kredi kartı silme")
    public String deleteCrediCart(@RequestParam(value = "cartNumber") String cartNumber) {
        return crediCartService.deleteCrediCart(cartNumber);
    }

    @DeleteMapping("/bankCart/delete")
    @ApiOperation(value = "Banka kartı silme")
    public String deleteBankCart(@RequestParam(value = "cartNumber") String cartNumber) {
        return bankCartService.deleteBankCart(cartNumber);
    }

    @GetMapping("/getCrediCart")
    public CrediCart getCrediCart(@RequestParam(value = "cartId") Long cartId) {

        CrediCart crediCart = crediCartService.getCrediCart(cartId);
        if (ObjectUtils.isEmpty(crediCart)) {
            throw new ApiRequestException("Bu kart numarasına ait hiç bir kart bulunamadı.",
                    HttpStatus.NOT_FOUND);
        }
        return crediCart;
    }

    @GetMapping("/getCrediCarts")
    @ApiOperation(value = "Kullanıcının kredi kartlarını getirme")
    public List<CrediCart> getCrediCartsByClient(@RequestParam(value = "id") String identityNumber) {

        List<CrediCart> cartList = crediCartService.getCrediCartsByClient(identityNumber);

        if (CollectionUtils.isEmpty(cartList)) {
            throw new ApiRequestException("Bu kullanıcıya ait hiç bir kredi kartı bulunamadı.", HttpStatus.NOT_FOUND);
        }
        return cartList;
    }

    @GetMapping("/debt")
    @ApiOperation(value = "Kredi kartı borcu sorgulama")
    public String getCreditCartDept(@RequestParam(value = "cartNumber") String cartNumber) {
        return crediCartService.debtInformation(cartNumber);
    }

    @GetMapping("/extracts")
    @ApiOperation(value = "Kredi kartı ekstresi sorgulama")
    public List<CartExtract> getExtract(@RequestParam(value = "cartNumber") String cartNumber){
        return crediCartService.getExtract(cartNumber);
    }
}
