package com.kodluyoruz.bootcampproject.service;


import com.kodluyoruz.bootcampproject.dto.ClientWsDto;
import com.kodluyoruz.bootcampproject.entity.Account;
import com.kodluyoruz.bootcampproject.entity.Cart.CrediCart;
import com.kodluyoruz.bootcampproject.entity.Client;
import com.kodluyoruz.bootcampproject.exception.ApiRequestException;
import com.kodluyoruz.bootcampproject.repository.ClientRepository;
import com.kodluyoruz.bootcampproject.repository.CrediCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CrediCartRepository crediCartRepository;

    public boolean validUnique(String identityNumber){
        Client client = clientRepository.findByIdentityNumber(identityNumber);
        return ObjectUtils.isEmpty(client);
    }

    public Client saveClient(Client client){
        if (validUnique(client.getIdentityNumber())){
            return clientRepository.save(client);
        }else{
            throw new ApiRequestException("Bu TC kimlik no ile zaten bir kullanıcı mevcut.");
        }

    }

    public Client getClientByIdentityNumber(String identityNumber){

        Client client = clientRepository.findByIdentityNumber(identityNumber);
        if (ObjectUtils.isEmpty(client)){
            throw new ApiRequestException(identityNumber + "TC kimlik numarası ile ilişkili bir kullanıcı bulunamadı",
                    HttpStatus.NOT_FOUND);
        }
        return client;
    }

    public ResponseEntity<Client> registerClient(Client client){

        Client saveClient = saveClient(client);
        return new ResponseEntity<>(saveClient, HttpStatus.CREATED);
    }

    public ResponseEntity<Client> updateClient(String identityNumber, ClientWsDto client) {

        Client currentClient = getClientByIdentityNumber(identityNumber);

        if (!ObjectUtils.isEmpty(client.getAdress())){
            currentClient.setAdress(client.getAdress());
        }
        if (!ObjectUtils.isEmpty(client.getName())){
            currentClient.setName(client.getName());
        }
        if (!ObjectUtils.isEmpty(client.getSurname())){
            currentClient.setSurname(client.getSurname());
        }
        if (!ObjectUtils.isEmpty(client.getPhoneNumber())){
            currentClient.setPhoneNumber(client.getPhoneNumber());
        }
        if (!ObjectUtils.isEmpty(client.getEmail())){
            currentClient.setEmail(client.getEmail());
        }

        clientRepository.save(currentClient);
        return new ResponseEntity<>(currentClient, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteClient(String identityNumber){

        Client getClient = getClientByIdentityNumber(identityNumber);

        for (CrediCart crediCart : getClient.getCrediCarts()){
            if (crediCart.getCartDebt() > 0){
                throw new ApiRequestException("Bu kullanıcının " + crediCart.getCartNumber() + " " +
                        "numaralı kartında " + crediCart.getCartDebt() + " TL borcu bulunduğundan hesabı silinemez.",
                        HttpStatus.BAD_REQUEST);
            }
        }

        for (Account account : getClient.getAccounts()){
            if (account.getBalance() > 0){
                throw new ApiRequestException("Bu kullanıcı " + account.getIBAN() +
                        " IBAN numaralı hesabındaki mevcut bakiye yüzünden silinemez.");
            }
        }
        clientRepository.delete(getClient);
        return new ResponseEntity<String>("Kullanıcı başarıyla silindi", HttpStatus.OK);
    }
}
