package com.kodluyoruz.bootcampproject.controller;

import com.kodluyoruz.bootcampproject.dto.AccountWsDto;
import com.kodluyoruz.bootcampproject.dto.DeptPaymentAccountWsDto;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.MoneyTransferModel;
import com.kodluyoruz.bootcampproject.enumTypes.TransferType;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.AccountRepository;
import com.kodluyoruz.bootcampproject.service.AccountService;
import com.kodluyoruz.bootcampproject.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.module.FindException;
import java.util.List;

@RestController
@RequestMapping(value = "/account")
@Api(value = "Account")
public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransferService transferService;

    @PostMapping("/createAccount")
    @ApiOperation(value = "Mevcut bir kullanıcı için hesap oluşturma")
    public AccountWsDto create(@ApiParam(value = "Account account") @RequestBody AccountWsDto account, @ApiParam(value = "identityNumber") @RequestParam(value = "id") String identityNumber){

        return accountService.createAccount(account, identityNumber);
    }

    @GetMapping("/getAccounts")
    @ApiOperation(value = "Kullanıcının hesaplarını getirme")
    public List<Account> getAccountsByClient(@RequestParam(value = "id") String identityNumber){
        try {
            return accountService.getAccountsByClient(identityNumber);
        }catch (Exception e){
            throw new ApiRequestException("Bu kullanıcıya ait herhangi hesap/lar bulunamadı.");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "Hesaptaki bakiyeyi güncelleme")
    public ResponseEntity<Account> updateAccountBalance(@RequestParam(value = "accountNumber") Long accountNumber, @RequestBody Double newBalance){
        return accountService.updateAccountBalance(accountNumber, newBalance);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Hesap silme")
    public ResponseEntity<Account> deleteAccount(@RequestParam(value = "id") Long accountId){
        return accountService.deleteAccountByAccountID(accountId);
    }

    @PostMapping("/money/transfer")
    @ApiOperation(value = "Hesaplar arası para transferi")
    public ResponseEntity<MoneyTransferModel> moneyTransfer(@Valid @RequestBody MoneyTransferModel moneyTransferModel) throws IOException {

        if (moneyTransferModel.getTransferType().equals(TransferType.TRANSFER.toString())){
            return transferService.transfer(moneyTransferModel);
        }else if (moneyTransferModel.getTransferType().equals(TransferType.EFT.toString())){
            return transferService.eftTransfer(moneyTransferModel);
        }
        throw new ApiRequestException("Transfer tipi 'TRANSFER' (hesaplar arası) ya da 'EFT' (bankalar arası) olmalıdır.",
                    HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/pay/creditCart/debt")
    @ApiOperation(value = "Hesaptan kredi kartı borcu ödeme")
    public ResponseEntity<CrediCart> payDebtForCreditCart(@RequestBody DeptPaymentAccountWsDto deptPaymentAccountWsDto){
        return accountService.payDebtForCreditCart(deptPaymentAccountWsDto);
    }
}
