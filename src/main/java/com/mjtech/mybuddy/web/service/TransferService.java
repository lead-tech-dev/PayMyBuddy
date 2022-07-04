package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Transfer;
import org.springframework.data.domain.Page;


/**
 * TransferService interface structure the business logic
 * of transfer.
 */
public interface TransferService {

  /**
   * saveTransfer. Method that save given transfer in database.
   *
   * @param transfer a transfer
   * @return a transfer
   */
  Transfer saveTransfer(Transfer transfer);

  /**
   * getTransaction. Method that get transaction page from database.
   *
   * @param id         user id
   * @param pageNumber a requested page
   * @return a transfer page
   */
  Page<Transfer> getTransaction(Long id, int pageNumber);
}
