package com.mjtech.mybuddy.web.service.impl;

import com.mjtech.mybuddy.model.Coupon;
import com.mjtech.mybuddy.repository.CouponRepository;
import com.mjtech.mybuddy.web.service.CouponService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Tag("CouponServiceImplTest")
@DisplayName("coupon service implementation class test")
class CouponServiceImplTest {

    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    private Coupon coupon;

    private Date expiredDate;

    @BeforeEach
    private void setUp() {
        couponService = new CouponServiceImpl(couponRepository);
        expiredDate = couponService.calculateExpiryDate(24 * 60);
        coupon = new Coupon(100, expiredDate);
    }

    @Test
    @DisplayName("generateCoupon, should save a coupon lis for given coupon number and price")
    void generateCoupon_ShouldSaveCouponList_ForGivenCouponNumberAndPrice() {
        // GIVEN
        when(couponRepository.save(coupon)).thenReturn(coupon);

        // WHEN
        List<Coupon> result = couponService.generateCoupon("1", 100, expiredDate);

        // THEN
        verify(couponRepository, times(1)).save(coupon);
        assertThat(result).contains(coupon);
    }

    @Test
    @DisplayName("getContacts should return coupons list")
    void getCoupons_ShouldReturnCouponList() {
        // GIVEN
        when(couponRepository.findAll()).thenReturn(List.of(coupon));

        // WHEN
        Iterable<Coupon> coupons = couponService.getCoupons();

        // THEN
        verify(couponRepository, times(1)).findAll();
        assertThat(coupons).contains(coupon);
    }

    @Test
    @DisplayName("deleteContact, should delete coupon for given coupon id")
    void deleteCoupon_ShouldReturnCoupon_ForGivenCouponId() {
        // GIVEN
        doNothing().when(couponRepository).deleteById(coupon.getId());

        // WHEN
        couponService.deleteCoupon(coupon.getId());

        // THEN
        verify(couponRepository, times(1)).deleteById(coupon.getId());
        assertTrue(couponService.getOneCoupon(String.valueOf(coupon.getId())).isEmpty());
    }

    @Test
    @DisplayName("findPage, should return coupon page list for given page number")
    void findPage_ShouldReturnCouponPageList_ForGivenPageNumber() {
        // GIVEN
        int pageNumber = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        Page<Coupon> coupons = new PageImpl(List.of(coupon));
        when(couponRepository.findAll(pageable)).thenReturn(coupons);

        // WHEN
        Page<Coupon> results = couponService.findPage(pageNumber);

        // THEN
        verify(couponRepository, times(1)).findAll(pageable);
        assertThat(results).contains(coupon);
    }

    @Test
    @DisplayName("getOneCoupon, should return coupon for given coupon id")
    void getOneCoupon_ShouldReturnCoupon_ForGivenCouponId() {
        // GIVEN
        when(couponRepository.findById(coupon.getId())).thenReturn(Optional.ofNullable(coupon));

        // WHEN
        Optional<Coupon> result = couponService.getOneCoupon(String.valueOf(coupon.getId()));

        // THEN
        verify(couponRepository, times(1)).findById(coupon.getId());
        assertThat(result.get()).isEqualTo(coupon);
    }

    @Test
    @DisplayName("saveCoupon should return saved coupon for given coupon")
    void saveCoupon_ShouldReturnSavedCoupon_ForGivenCoupon() {
        // GIVEN
        when(couponRepository.save(coupon)).thenReturn(coupon);

        // WHEN
        Coupon result = couponService.saveCoupon(coupon);

        // THEN
        verify(couponRepository, times(1)).save(coupon);
        assertThat(result).isEqualTo(coupon);
    }
}