package com.kodluyoruz.bootcampproject.service;

import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.BankCart;

import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.BankCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class BankCartService {

    @Autowired
    AccountService accountService;
    @Autowired
    BankCartRepository bankCartRepository;

    public BankCart createBankCart(Long accountNumber) {

        Account account = accountService.getAccountByAccountNumber(accountNumber);

        if (!ObjectUtils.isEmpty(account.getBankCart())){
            throw new ApiRequestException("Bu hesaba tanımlı " + account.getBankCart().getCartNumber() +
                    " numaralı bir banka kartı zaten mevcuttur.");
        }

        BankCart newBankCart = new BankCart();
        account.setBankCart(newBankCart);

        newBankCart.setCartNumber(Static.createRandomValue(16));
        newBankCart.setBalance(account.getBalance());
        newBankCart.setAccount(account);

        accountService.saveAccount(account);

        return newBankCart;
    }

    public void saveBankCart(BankCart bankCart){
        bankCartRepository.save(bankCart);
    }

    public BankCart getBankCartByCartNumber(String cartNumber){
        BankCart bankCart = bankCartRepository.findByCartNumber(cartNumber);
        if (ObjectUtils.isEmpty(bankCart)){
            throw new ApiRequestException("Kart numarasına ait hiç bir ön ödemeli kart bulunamadı.");
        }
        return bankCart;
    }

    public String deleteBankCart(String cartNumber) {

        BankCart bankCart = getBankCartByCartNumber(cartNumber);
        bankCartRepository.delete(bankCart);
        return "Kart silme işlemi başarıyla tamamlandı";
    }
}
