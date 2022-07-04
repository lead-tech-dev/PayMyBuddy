package com.mjtech.mybuddy.model;

import com.mjtech.mybuddy.enumeration.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class CouponTest {

    private Coupon coupon1;

    private Coupon coupon2;

    @BeforeEach
    private void setUp() {
        coupon1 = new Coupon();
        coupon1.setId(1);
        coupon1.setPrice(90);
        coupon1.setStatus(Status.ACTIVE);
        coupon1.setExpiredAt(new Date());

        coupon2 = new Coupon();
        coupon2.setId(1);
        coupon2.setPrice(90);
        coupon2.setStatus(Status.ACTIVE);
        coupon2.setExpiredAt(new Date());
    }

    @Test
    void testEquals() {
        assertThat(coupon1.toString().equals(coupon2.toString())).isEqualTo(true);
    }

    @Test
    void canEqual() {
        assertThat(coupon1.toString()).isEqualTo(coupon2.toString());
    }

    @Test
    void testHashCode() {
        assertThat(coupon1.toString().hashCode()).isEqualTo(coupon2.toString().hashCode());
    }
}