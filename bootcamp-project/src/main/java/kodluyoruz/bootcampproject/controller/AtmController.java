package com.kodluyoruz.bootcampproject.controller;

import com.kodluyoruz.bootcampproject.dto.DeptPaymentAtmWsDto;
import com.kodluyoruz.bootcampproject.dto.MoneyTransactionAtmWsDto;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.service.AccountService;
import com.kodluyoruz.bootcampproject.service.AtmService;
import com.kodluyoruz.bootcampproject.service.CrediCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/atm")
@Api(value = "ATM")
public class AtmController {

    @Autowired
    AccountService accountService;
    @Autowired
    CrediCartService crediCartService;
    @Autowired
    AtmService atmService;

    @PostMapping("/account/pay/creditCart/debt")
    public ResponseEntity<CrediCart> payCreditCartDeptWithAccount(@RequestBody String cartNumber,
                                                          @RequestParam(value = "amount") Double amount,
                                                          @RequestParam(value = "accountIban") String iban){
       //return accountService.payDebtForCreditCart(iban,cartNumber,amount);
        return null;
    }

    @PostMapping("/pay/creditCart/debt")
    @ApiOperation(value = "ATM ile kredi kartı borcu ödeme")
    public ResponseEntity<CrediCart> payCreditCartDept(@RequestBody DeptPaymentAtmWsDto deptPaymentAtmWsDto){
        return crediCartService.payDebtWithAtm(deptPaymentAtmWsDto);
    }

    @PostMapping("/loadMoney/bankcart")
    @ApiOperation(value = "Banka kartı ile hesaba para yükleme")
    public ResponseEntity<String> loadMoney(@RequestBody MoneyTransactionAtmWsDto moneyTransactionAtmWsDto){
        return atmService.loadMoneyForBankCart(moneyTransactionAtmWsDto);
    }

    @PostMapping("/withdrawMoney/bankcart")
    @ApiOperation(value = "Banka kartı ile hesaptan para çekme")
    public ResponseEntity<String> withdrawMoney(@RequestBody MoneyTransactionAtmWsDto moneyTransactionAtmWsDto){
        return atmService.withdrawMoneyForBankCart(moneyTransactionAtmWsDto);
    }
}
