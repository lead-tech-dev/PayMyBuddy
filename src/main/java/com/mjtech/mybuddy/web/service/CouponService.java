package com.mjtech.mybuddy.web.service;

import com.mjtech.mybuddy.model.Coupon;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CouponService interface structure the business logic
 * of coupon.
 */
public interface CouponService {

  /**
   * generateCoupon. Method that generate a number of
   * coupon in database.
   *
   * @param couponNumber the number of coupons to generate
   * @param price        a coupon price
   * @param expiredDate  a expired date of coupon
   * @return coupon list
   */
  List<Coupon> generateCoupon(String couponNumber, double price, Date expiredDate);

  /**
   * getCoupons. Method that get coupon list from database.
   *
   * @return Iterable coupon
   */
  Iterable<Coupon> getCoupons();

  /**
   * deleteCoupon. Method that delete given coupon in database.
   *
   * @param id a contact id
   */
  void deleteCoupon(long id);

  /**
   * findPage. Method that get coupon page from database.
   *
   * @param pageNumber a requested page
   * @return a coupon page
   */
  Page<Coupon> findPage(int pageNumber);

  /**
   * getOneCoupon. Method that get one coupon from database.
   *
   * @param couponId a coupon id
   * @return optional coupon
   */
  Optional<Coupon> getOneCoupon(String couponId);

  /**
   * saveCoupon. Method that save given coupon in database.
   *
   * @param coupon a coupon
   * @return a coupon
   */
  Coupon saveCoupon(Coupon coupon);

  /**
   * calculateExpiryDate. Method that calculate an expired date of coupon.
   *
   * @param expiryTimeInMinutes an expiry time
   * @return a date
   */
  Date calculateExpiryDate(final int expiryTimeInMinutes);
}
