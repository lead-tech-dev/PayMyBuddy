package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * The CouponRechargeDto class implements a CouponRechargeDto
 * entity.
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRechargeDto {

  @NotBlank
  private String couponId;

}
