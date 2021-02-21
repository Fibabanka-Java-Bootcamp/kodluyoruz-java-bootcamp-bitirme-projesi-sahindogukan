package com.kodluyoruz.bootcampproject.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.MoneyTransferModel;
import com.kodluyoruz.bootcampproject.entity.Rates;
import com.kodluyoruz.bootcampproject.entity.Root;
import com.kodluyoruz.bootcampproject.enumTypes.AccountCurrency;
import com.kodluyoruz.bootcampproject.enumTypes.AccountType;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.TransferRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransferService {

    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    TransferRepository transferRepository;

    RestTemplate restTemplate = new RestTemplate();

    public boolean validAmount(double amount, Account fromAccount){
        return fromAccount.getBalance() >= amount;
    }

    public Double getExchangeValues(AccountCurrency base, AccountCurrency change, Double value) throws IOException {

        String response = restTemplate.getForObject("https://api.exchangeratesapi.io/latest?base="+base.toString(), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        Root root = objectMapper.readValue(response,Root.class);

        if (change.equals(AccountCurrency.EUR)){
            return (root.rates.eUR*value);
        }else if (change.equals(AccountCurrency.USD)){
            return (root.rates.uSD*value);
        }else if (change.equals(AccountCurrency.TRY)){
            return (root.rates.tRY*value);
        }else {
            throw new ApiRequestException("Exchange işlemi başarısız oldu");
        }
    }

    public void makeMoneyTransfer(Account toAccount, Account fromAccount, double moneyAmount) throws IOException {

        if (validAmount(moneyAmount, fromAccount)){
            Double value = getExchangeValues(fromAccount.getAccountCurrency(),toAccount.getAccountCurrency(),moneyAmount);
            toAccount.setBalance(toAccount.getBalance()+value);
            fromAccount.setBalance(fromAccount.getBalance()-moneyAmount);
        }else {
            throw new ApiRequestException("İşlemin gerçekleşebilmesi için gönderici hesapta yeterli" +
                    " miktar bulunmamaktadır.", HttpStatus.BAD_REQUEST);
        }

        accountService.saveAccount(toAccount);
        accountService.saveAccount(fromAccount);
    }

    public ResponseEntity<MoneyTransferModel> transfer(MoneyTransferModel moneyTransferModel) throws IOException {

        Account toAccount = accountService.getAccountByIBAN(moneyTransferModel.getToIBAN());
        Account fromAccount = accountService.getAccountByIBAN(moneyTransferModel.getFromIBAN());

        if (!(fromAccount.getClient().getId() == toAccount.getClient().getId())){
            throw new ApiRequestException("Havale işlemi sadece aynı kullanıcıya ait hesaplar" +
                    " arasında yapılabilir.", HttpStatus.BAD_REQUEST);
        }

        makeMoneyTransfer(toAccount,fromAccount,moneyTransferModel.getTransferAmout());
        transferRepository.save(moneyTransferModel);
        return new ResponseEntity<>(moneyTransferModel,HttpStatus.OK);
    }

    public ResponseEntity<MoneyTransferModel> eftTransfer(MoneyTransferModel moneyTransferModel) throws IOException {
        Account toAccount = accountService.getAccountByIBAN(moneyTransferModel.getToIBAN());
        Account fromAccount = accountService.getAccountByIBAN(moneyTransferModel.getFromIBAN());

        if (fromAccount.getAccountType().equals(AccountType.DEPOSIT)){
            throw new ApiRequestException("Vadeli hesaptan para gönderim işlemi yapılamaz.",
                    HttpStatus.BAD_REQUEST);
        };

        makeMoneyTransfer(toAccount,fromAccount,moneyTransferModel.getTransferAmout());
        transferRepository.save(moneyTransferModel);
        return new ResponseEntity<>(moneyTransferModel,HttpStatus.OK);
    }

}
