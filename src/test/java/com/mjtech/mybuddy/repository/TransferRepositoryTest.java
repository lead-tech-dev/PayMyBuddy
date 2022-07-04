package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.enumeration.TransferType;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.utility.SecurityUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("TransferRepositoryTest")
@DisplayName("transferRepository interface test")
class TransferRepositoryTest {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    private Transfer transfer;
    private Users debtor;

    private Users debit;

    @BeforeEach
    private void setUp() {
        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        debit = new Users();
        debit.setEmail("cartman@yahoo.fr");
        debit.setUsername("cartman");
        debit.setName("cartman");
        debit.setPassword(encryptedPassword);
        usersRepository.save(debit);

        debtor = new Users();
        debtor.setEmail("laplume@yahoo.fr");
        debtor.setUsername("laplume");
        debtor.setName("laplume");
        debtor.setPassword(encryptedPassword);
        usersRepository.save(debtor);

        Connection connection = new Connection(debit, debtor);
        connection.setStatus(Status.ACTIVE);

        connectionRepository.save(connection);

        transfer = new Transfer();
        transfer.setDescription("Tranfert vers mon compte bancaire");
        transfer.setAmount(100);
        transfer.setTransferDate(new Date());
        transfer.setCommission(0.25);
        transfer.setCredit(debit);
        transfer.setDebtor(debtor);
        transfer.setType(TransferType.OPERATION);
        transfer.setConnection(connection);

        transferRepository.save(transfer);

    }

    @Test
    @DisplayName("findUserTransactions, should return user transaction list")
    void findUserTransactions_ShouldReturnUserTransactionList() {
        // GIVEN
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);

        // WHEN
        Page<Transfer> results = transferRepository.findUserTransactions(debit.getId(), pageable);

        // THEN
        assertThat(results).contains(transfer);
    }
}