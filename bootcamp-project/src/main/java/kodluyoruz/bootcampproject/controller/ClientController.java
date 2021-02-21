package com.kodluyoruz.bootcampproject.controller;


import com.kodluyoruz.bootcampproject.dto.ClientWsDto;
import com.kodluyoruz.bootcampproject.entity.Client;
import com.kodluyoruz.bootcampproject.repository.ClientRepository;
import com.kodluyoruz.bootcampproject.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping(value = "/client")
@Api(value = "Client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;


    @PostMapping("/register")
    @ApiOperation(value = "Kullanıcı oluşturma")
    public ResponseEntity<Client> register(@Valid @RequestBody Client client){
        return clientService.registerClient(client);
    }

    @GetMapping("/getClients")
    @ApiOperation(value = "Bütün kullanıcıları listeleme")
    public List<Client> getClients(){
      return clientRepository.findAll();
    }

    @GetMapping("/getClient")
    public Client getClient(@RequestParam(value = "clientId") String identityNumber){
        return clientService.getClientByIdentityNumber(identityNumber);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Kullanıcı güncelleme")
    public ResponseEntity<Client> updateClient(@RequestParam(value = "clientId") String identityNumber, @RequestBody ClientWsDto client){
       return clientService.updateClient(identityNumber,client);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Kullanıcı silme")
    public ResponseEntity<String> deleteClient(@RequestParam(value = "clientId") String identityNumber){
         return clientService.deleteClient(identityNumber);
    }

}
