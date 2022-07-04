package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Account;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.AccountRepository;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("AccountServiceImplTest")
@DisplayName("account service implementation class")
class AccountServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    private Account account = null;

    @BeforeEach
    private void setUp() {
        accountService = new AccountServiceImpl(accountRepository);

        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        Users user = new Users();
        user.setEmail("cartman@yahoo.fr");
        user.setUsername("cartman");
        user.setName("cartman");
        user.setPassword(encryptedPassword);

        account = new Account();
        account.setId(1);
        account.setBalance(0);
        account.setCreatedAt(new Date());
        account.setUser(user);
    }

    @Test
    @DisplayName("saveAccount should save one account")
    void saveAccount_ShouldSaveOne_ForGivenAccount() throws Exception {
        // GIVEN
        when(accountRepository.save(account)).thenReturn(account);

        // WHEN
        Account result = accountService.saveAccount(account);

        // THEN
        verify(accountRepository, times(1)).save(account);
        assertThat(result).isEqualTo(account);
    }

    @Test
    @DisplayName("getOneAccount should return optional account for given account id")
    void getOneAccount_ShouldReturnAccount_ForGivenAccountId() {
        // GIVEN
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        // WHEN
        Optional<Account> result = accountService.getOneAccount(account.getId());

        // THEN
        verify(accountRepository, times(1)).findById(account.getId());
        assertThat(result.get()).isEqualTo(account);
    }

    @Test
    @DisplayName("getOneAccount should return optional account for given account user")
    void getOneAccountByUser_ShouldReturnAccount_ForGivenAccountUser() {
        // GIVEN
        when(accountRepository.findByUser(account.getUser())).thenReturn(Optional.of(account));

        // WHEN
        Optional<Account> result = accountService.getOneAccountByUser(account.getUser());

        // THEN
        verify(accountRepository, times(1)).findByUser(account.getUser());
        assertThat(result.get()).isEqualTo(account);
    }
}