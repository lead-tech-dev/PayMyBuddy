package com.mjtech.mybuddy.dto;

import com.mjtech.mybuddy.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * The TransactionDto class implements a TransactionDto
 * entity.
 */
@lombok.Generated
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
  private Date transferDate;

  private String name;

  private String email;

  private double amount;

  private String description;

  private Users debtor;

  private Users credit;
}
