package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.BankRepository;
import com.mjtech.mybuddy.web.service.BankService;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * BankServiceImpl. class that implement
 * bank business logic
 */
@Service
public class BankServiceImpl implements BankService {

  private final BankRepository bankRepository;

  public BankServiceImpl(BankRepository bankRepository) {
    this.bankRepository = bankRepository;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Bank> getOneBank(Users user) {
    return bankRepository.findByUser(user);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Bank saveBank(Bank newBank) {
    return bankRepository.save(newBank);
  }
}
