package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.enumeration.Status;
import com.mjtech.mybuddy.enumeration.TransferType;
import com.mjtech.mybuddy.model.Connection;
import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.model.Users;
import com.mjtech.mybuddy.repository.ConnectionRepository;
import com.mjtech.mybuddy.repository.TransferRepository;
import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("TransferServiceImplTest")
@DisplayName("transfer service implementation class test")
class TransferServiceImplTest {

    private TransferService transferService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private ConnectionRepository connectionRepository;

    private Transfer transfer;

    private Users debtor;

    private Users debit;

    @BeforeEach
    private void setUp() {
        transferService = new TransferServiceImpl(transferRepository);

        String encryptedPassword = SecurityUtility.passwordEncoder().encode("123");
        debit = new Users();
        debit.setId(1L);
        debit.setEmail("cartman@yahoo.fr");
        debit.setUsername("cartman");
        debit.setName("cartman");
        debit.setPassword(encryptedPassword);

        debtor = new Users();
        debtor.setId(2L);
        debtor.setEmail("laplume@yahoo.fr");
        debtor.setUsername("laplume");
        debtor.setName("laplume");
        debtor.setPassword(encryptedPassword);

        Connection connection = new Connection(
                1,
                debtor,
                debit,
                Status.ACTIVE,
                new Date(),
                List.of(new Transfer())
        );

        transfer = new Transfer(
                1,
                "Remboursement",
                100,
                0.25,
                new Date(),
                TransferType.OPERATION,
                debtor,
                debit,
                connection
        );


    }

    @Test
    @DisplayName("saveTransfer should return saved transfer for given transfer")
    void saveTransfer_ShouldReturnSavedTransfer_ForGivenTransfer() {
        // GIVEN
        when(transferRepository.save(transfer)).thenReturn(transfer);

        // WHEN
        Transfer result = transferService.saveTransfer(transfer);

        // THEN
        verify(transferRepository, times(1)).save(transfer);
        assertThat(result).isEqualTo(transfer);
    }

    @Test
    @DisplayName("getTransaction, should return transfer for given coupon id")
    void getTransaction_ShouldReturnTransfer_ForGivenTransferId() {
        // GIVEN
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        Page<Transfer> transfers = new PageImpl(List.of(transfer));
        when(transferRepository.findUserTransactions(transfer.getId(), pageable)).thenReturn(transfers);

        // WHEN
        Page<Transfer> results = transferService.getTransaction(transfer.getId(), pageNumber);

        // THEN
        verify(transferRepository, times(1)).findUserTransactions(transfer.getId(), pageable);
        assertThat(results).contains(transfer);
    }
}