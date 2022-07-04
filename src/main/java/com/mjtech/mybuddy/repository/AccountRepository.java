package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * AccountRepository. Interface that allows to access and
 * persist data between Account object and buddy database
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
  Optional<Account> findByUser(Users user);
}
