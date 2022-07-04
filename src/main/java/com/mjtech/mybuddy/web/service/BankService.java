package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Users;

import java.util.Optional;

/**
 * BankService interface structure the business logic
 * of bank.
 */
public interface BankService {

  /**
   * getOneBank. Method that get one bank from database.
   *
   * @param user a bank user
   * @return optional bank
   */
  Optional<Bank> getOneBank(Users user);

  /**
   * saveBank. Method that save given bank in database.
   *
   * @param bank a bank
   * @return a bank
   */
  Bank saveBank(Bank bank);
}
