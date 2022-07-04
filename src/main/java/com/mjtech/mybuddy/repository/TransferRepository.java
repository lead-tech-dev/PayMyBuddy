package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * TransferRepository. Interface that allows to access and
 * persist data between Transfer object and buddy database
 */
@Repository
public interface TransferRepository extends PagingAndSortingRepository<Transfer, Long> {

  @Query(value = "select * from transfer\n" +
          "where credit_id = :id or debtor_id= :id order by transfer_date desc", nativeQuery = true)
  Page<Transfer> findUserTransactions(@Param("id") Long id, Pageable pageable);

}
