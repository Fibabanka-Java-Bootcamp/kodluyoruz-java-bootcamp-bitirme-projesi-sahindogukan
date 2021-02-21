package com.kodluyoruz.bootcampproject.service;

import com.kodluyoruz.bootcampproject.dto.AccountWsDto;
import com.kodluyoruz.bootcampproject.dto.DeptPaymentAccountWsDto;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.Client;
import com.kodluyoruz.bootcampproject.entity.MoneyTransferModel;
import com.kodluyoruz.bootcampproject.enumTypes.AccountCurrency;
import com.kodluyoruz.bootcampproject.enumTypes.AccountType;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.AccountRepository;
import com.kodluyoruz.bootcampproject.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CrediCartService crediCartService;

    private final String accountCurrencyRegex = "TRY|USD|EUR";
    private final String accountTypeRegex = "CHECKING|DEPOSIT";

    StringBuilder stringBuilder = new StringBuilder().append("^TR\\d{7}[0-9A-Z]{17}$");

    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    public Client getClientByID(String identityNumber){
        return clientRepository.findByIdentityNumber(identityNumber);
    }

    public Account getAccountByAccountNumber(Long accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (ObjectUtils.isEmpty(account)){
            throw new ApiRequestException("Bu hesap numarası ile ilişkili bir hesap bulunamadı.",
                    HttpStatus.NOT_FOUND);
        }
        return account;
    }

    public Account getAccountByIBAN(String ibanNo) {

        if (!ibanNo.matches(stringBuilder.toString())) {
            Account account = accountRepository.findByIBAN(ibanNo);
            if (!ObjectUtils.isEmpty(account)) {
                return account;
            } else {
                throw new ApiRequestException(ibanNo + " ile ilişkili herhangi bir hesap bulunamadı.",
                        HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ApiRequestException("Lütfen geçerli formatta bir IBAN numarası giriniz.",
                    HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<Account> updateAccountBalance(Long accountNumber, Double newBalance) {
       Account account = getAccountByAccountNumber(accountNumber);

       account.setBalance(newBalance);
       accountRepository.save(account);
       return new ResponseEntity<>(account, HttpStatus.OK);
    }

    public boolean validAmount(double amount, Account fromAccount){
        return fromAccount.getBalance() >= amount;
    }

    public AccountWsDto createAccount(AccountWsDto accountWsDto, String identityNumber){

        if (!accountWsDto.getAccountCurrency().matches(accountCurrencyRegex)){
            throw new ApiRequestException("Hesap para birimi sadece TRY|EUR|USD olabilir.");
        }
        if (!accountWsDto.getAccountType().matches(accountTypeRegex)){
            throw new ApiRequestException("Hesap tipi sadece CHECKING|DEPOSIT olabilir.");
        }
        Client client = getClientByID(identityNumber);

        Account account = new Account();
        account.setClient(client);
        account.setCreatedDate(new Date());
        account.setIBAN(Static.createRandomIBAN());
        account.setAccountCurrency(AccountCurrency.valueOf(accountWsDto.getAccountCurrency()));
        account.setAccountType(AccountType.valueOf(accountWsDto.getAccountType()));

        client.getAccounts().add(account);

        clientRepository.save(client);
        accountWsDto.setIBAN(account.getIBAN());
        return accountWsDto;
    }

    public List<Account> getAccountsByClient(String identityNumber) {
        Client client = getClientByID(identityNumber);
        return client.getAccounts();
    }

    public void makeMoneyTransfer(Account toAccount, Account fromAccount, double moneyAmount){

        if (validAmount(moneyAmount, fromAccount)){
            toAccount.setBalance(toAccount.getBalance()+moneyAmount);
            fromAccount.setBalance(fromAccount.getBalance()-moneyAmount);
        }else {
            throw new ApiRequestException("İşlemin gerçekleşebilmesi için gönderici hesapta yeterli" +
                    " miktar bulunmamaktadır.", HttpStatus.BAD_REQUEST);
        }

        accountRepository.save(toAccount);
        accountRepository.save(fromAccount);
    }

    public ResponseEntity<MoneyTransferModel> transfer(MoneyTransferModel moneyTransferModel){

        Account toAccount = getAccountByIBAN(moneyTransferModel.getToIBAN());
        Account fromAccount = getAccountByIBAN(moneyTransferModel.getFromIBAN());

        if (!(fromAccount.getClient().getId() == toAccount.getClient().getId())){
            throw new ApiRequestException("Havale işlemi sadece aynı kullanıcıya ait hesaplar" +
                    " arasında yapılabilir.", HttpStatus.BAD_REQUEST);
        }

        makeMoneyTransfer(toAccount,fromAccount,moneyTransferModel.getTransferAmout());

        return new ResponseEntity<>(moneyTransferModel,HttpStatus.OK);
    }

    public ResponseEntity<MoneyTransferModel> eftTransfer(MoneyTransferModel moneyTransferModel) {
        Account toAccount = getAccountByIBAN(moneyTransferModel.getToIBAN());
        Account fromAccount = getAccountByIBAN(moneyTransferModel.getFromIBAN());

        if (fromAccount.getAccountType().equals(AccountType.DEPOSIT)){
            throw new ApiRequestException("Vadeli hesaptan para gönderim işlemi yapılamaz.",
                    HttpStatus.BAD_REQUEST);
        };

        makeMoneyTransfer(toAccount,fromAccount,moneyTransferModel.getTransferAmout());

        return new ResponseEntity<>(moneyTransferModel,HttpStatus.OK);
    }

    public ResponseEntity<Account> deleteAccountByAccountID(Long accountNumber) {

        Account account = getAccountByAccountNumber(accountNumber);
        accountRepository.delete(account);
        return new ResponseEntity<Account>(account,HttpStatus.OK);


    }

    public ResponseEntity<CrediCart> payDebtForCreditCart(DeptPaymentAccountWsDto deptPaymentAccountWsDto){

        Account account = getAccountByIBAN(deptPaymentAccountWsDto.getAccountIban());
        CrediCart crediCart = crediCartService.getCrediCartByCartNumber(deptPaymentAccountWsDto.getCartNumber());

        if (!(account.getBalance() >= deptPaymentAccountWsDto.getAmount())){
            throw new ApiRequestException("Kredi kartı borcunu ödemek için hesapta yeterli miktar bulunmamaktadır.",
                    HttpStatus.BAD_REQUEST);
        }

        crediCart.setCartDebt(crediCart.getCartDebt()-deptPaymentAccountWsDto.getAmount());
        account.setBalance(account.getBalance()-deptPaymentAccountWsDto.getAmount());

        crediCartService.saveCrediCart(crediCart);
        saveAccount(account);
        return new ResponseEntity<>(crediCart,HttpStatus.OK);
/*
        Account account = getAccountByIBAN(iban);
        CrediCart crediCart = crediCartService.getCrediCartByCartNumber(cartNumber);

        if (!(account.getBalance() >= amount)){
            throw new ApiRequestException("Kredi kartı borcunu ödemek için hesapta yeterli miktar bulunmamaktadır.",
                    HttpStatus.BAD_REQUEST);
        }

        crediCart.setCartDebt(crediCart.getCartDebt()-amount);
        account.setBalance(account.getBalance()-amount);

        crediCartService.saveCrediCart(crediCart);
        saveAccount(account);
        return new ResponseEntity<>(crediCart,HttpStatus.OK);

 */
    }
}
