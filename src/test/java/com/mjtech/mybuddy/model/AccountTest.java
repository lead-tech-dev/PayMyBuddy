package com.mjtech.mybuddy.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    private Account account1;

    private Account account2;

    private void setUp() {
        Date date = new Date();
        account1 = new Account();
        account1.setId(1);
        account1.setBalance(10);
        account1.setCreatedAt(date);

        account2 = new Account();
        account2.setId(1);
        account2.setBalance(10);
        account2.setCreatedAt(date);
    }

    @Test
    void getCreatedAt() {
        // GIVEN
        Date date = new Date();
        Account account = new Account(1,
                date,
                10, new Users());
        // WHEN
        // THEN
        assertThat(account.getCreatedAt()).isEqualTo(date);
    }

}