package com.mjtech.mybuddy.repository;

import com.mjtech.mybuddy.model.Coupon;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * CouponRepository. Interface that allows to access and
 * persist data between Coupon object and buddy database
 */
@Repository
public interface CouponRepository extends PagingAndSortingRepository<Coupon, Long> {

}
