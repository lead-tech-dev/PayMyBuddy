package com.mjtech.mybuddy.model;


import com.mjtech.mybuddy.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * The Coupon class implements a coupon
 * entity.
 */

@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private double price;

  @Enumerated(EnumType.STRING)
  private Status status;
  private Date expiredAt;

  public Coupon(double price, Date expiredAt) {
    this.price = price;
    this.status = Status.ACTIVE;
    this.expiredAt = expiredAt;
  }


}
