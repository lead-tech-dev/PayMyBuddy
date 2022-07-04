package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Bank;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.BankRepository;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("BankServiceImplTest")
@DisplayName("bank service implementation class test")
class BankServiceImplTest {

    @Mock
    private BankRepository bankRepository;

    private BankService bankService;

    private Bank bank = null;


    @BeforeEach
    private void setUp() {
        bankService = new BankServiceImpl(bankRepository);

        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        Users user = new Users();
        user.setEmail("cartman@yahoo.fr");
        user.setUsername("cartman");
        user.setName("cartman");
        user.setPassword(encryptedPassword);

        bank = new Bank(
                1,
                "azerty",
                0,
                "la meilleur bank",
                "", user
        );

    }


    @Test
    @DisplayName("getOneBank should return optional bank account for given bank id")
    void getOneBank() {
        // GIVEN
        when(bankRepository.findByUser(bank.getUser())).thenReturn(Optional.ofNullable(bank));

        // WHEN
        Optional<Bank> result = bankService.getOneBank(bank.getUser());

        // THEN
        verify(bankRepository, times(1)).findByUser(bank.getUser());
        assertThat(result.get()).isEqualTo(bank);
    }

    @Test
    @DisplayName("saveBank should save one bank account")
    void saveBank_ShouldSaveOne_ForGivenBank() {
        // GIVEN
        when(bankRepository.save(bank)).thenReturn(bank);

        // WHEN
        Bank result = bankService.saveBank(bank);

        // THEN
        verify(bankRepository, times(1)).save(bank);
        assertThat(result).isEqualTo(bank);
    }
}