package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.AccountRepository;
import com.mjtech.mybuddy.web.service.AccountService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


/**
 * AccountServiceImpl. class that implement
 * account business logic
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Account> getOneAccount(long id) {
    return accountRepository.findById(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Account> getOneAccountByUser(Users user) {
    return accountRepository.findByUser(user);
  }
}
