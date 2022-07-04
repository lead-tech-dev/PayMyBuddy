package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Coupon;
import com.mjtech.mybuddy.repository.CouponRepository;
import com.mjtech.mybuddy.web.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * CouponServiceImpl. class that implement
 * coupon business logic
 */
@Service
public class CouponServiceImpl implements CouponService {

  private static final int EXPIRATION = 60 * 24;

  private final CouponRepository couponRepository;

  public CouponServiceImpl(CouponRepository couponRepository) {
    this.couponRepository = couponRepository;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Coupon> generateCoupon(String couponNumber, double price, Date expiredDate) {
    List<Coupon> coupons = new ArrayList<>();
    for (int i = 0; i < Integer.parseInt(couponNumber); i++) {
      coupons.add(couponRepository.save(new Coupon(price, expiredDate)));
    }

    return coupons;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Coupon> getCoupons() {
    return couponRepository.findAll();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteCoupon(long id) {
    couponRepository.deleteById(id);
  }

  public Page<Coupon> findPage(int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber - 1, 5);
    return couponRepository.findAll(pageable);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Coupon> getOneCoupon(String couponId) {
    return couponRepository.findById(Long.valueOf(couponId));
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Coupon saveCoupon(Coupon coupon) {
    couponRepository.save(coupon);
    return coupon;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Date calculateExpiryDate(final int expiryTimeInMinutes) {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }
}
