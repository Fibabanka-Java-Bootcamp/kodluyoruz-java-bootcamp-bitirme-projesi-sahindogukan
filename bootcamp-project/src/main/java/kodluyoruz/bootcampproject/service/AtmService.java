package com.kodluyoruz.bootcampproject.service;

import com.kodluyoruz.bootcampproject.dto.MoneyTransactionAtmWsDto;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AtmService {

    @Autowired
    BankCartService bankCartService;
    @Autowired
    AccountService accountService;

    public ResponseEntity<String> loadMoneyForBankCart(MoneyTransactionAtmWsDto moneyTransactionAtmWsDto){
        BankCart bankCart = bankCartService.getBankCartByCartNumber(moneyTransactionAtmWsDto.getCartNumber());
        Account account = accountService.getAccountByAccountNumber(bankCart.getAccount().getAccountNumber());
        account.setBalance(account.getBalance()+moneyTransactionAtmWsDto.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>(account.getIBAN()+" IBAN no'lu hesaba " + moneyTransactionAtmWsDto.getAmount() + " " +
                account.getAccountCurrency().toString() + " başarıyla yatırıldı.", HttpStatus.OK);
    }

    public ResponseEntity<String> withdrawMoneyForBankCart(MoneyTransactionAtmWsDto moneyTransactionAtmWsDto) {
        BankCart bankCart = bankCartService.getBankCartByCartNumber(moneyTransactionAtmWsDto.getCartNumber());
        Account account = accountService.getAccountByAccountNumber(bankCart.getAccount().getAccountNumber());
        boolean isValid = accountService.validAmount(moneyTransactionAtmWsDto.getAmount(), account);

        if (isValid){
            account.setBalance(account.getBalance()-moneyTransactionAtmWsDto.getAmount());
        }else{
            throw new ApiRequestException("Hesaptaki tutar ("+account.getBalance()+") çekmek istediğiniz tutar (" +
            moneyTransactionAtmWsDto.getAmount() + ") için yetersizdir.");
        }

        accountService.saveAccount(account);
        return new ResponseEntity<>("Çekilen tutar: "+moneyTransactionAtmWsDto.getAmount() +
                " Yeni bakiye: "+ account.getBalance(), HttpStatus.OK);
    }
}
