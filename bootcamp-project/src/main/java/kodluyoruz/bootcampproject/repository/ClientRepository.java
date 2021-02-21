package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long>  {

    Client findByIdentityNumber(String identityNumber);
    Client deleteClientByIdentityNumber(String identityNumber);
}
