package com.kodluyoruz.bootcampproject.repository;

import com.kodluyoruz.bootcampproject.entity.MoneyTransferModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<MoneyTransferModel, Long> {
}
