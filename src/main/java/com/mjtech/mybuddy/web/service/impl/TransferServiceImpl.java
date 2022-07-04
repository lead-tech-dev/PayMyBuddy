package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Transfer;
import com.mjtech.mybuddy.repository.TransferRepository;
import com.mjtech.mybuddy.web.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * TransferServiceImpl. class that implement
 * transfer business logic
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

  private final TransferRepository transferRepository;


  public TransferServiceImpl(TransferRepository transferRepository) {
    this.transferRepository = transferRepository;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Transfer saveTransfer(Transfer transfer) {
    transferRepository.save(transfer);
    return transfer;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Page<Transfer> getTransaction(Long id, int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber - 1, 5);
    return transferRepository.findUserTransactions(id, pageable);
  }
}
