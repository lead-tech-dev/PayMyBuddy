package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BankRepository extends CrudRepository<Bank, Long> {
  Optional<Bank> findByUser(Users user);
}
