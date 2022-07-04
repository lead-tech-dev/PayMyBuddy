package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Users;

import java.util.Optional;

/**
 * AccountService interface structure the business logic
 * of account.
 */
public interface AccountService {

  /**
   * saveAccount. Method that save given account in database.
   *
   * @param account an account
   * @return an account
   */
  Account saveAccount(Account account);


  /**
   * getOneAccount. Method that get one account from database.
   *
   * @param id an account id
   * @return optional account
   */
  Optional<Account> getOneAccount(long id);

  /**
   * getOneAccountByUser. Method that get one account from database.
   *
   * @param user an account user
   * @return optional account
   */
  Optional<Account> getOneAccountByUser(Users user);
}
