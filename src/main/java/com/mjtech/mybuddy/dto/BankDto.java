package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The BankDto class implements a BankDto
 * entity.
 */
@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDto {

  @NotBlank
  private String iban;

  private double balance;

  @NotBlank
  private String description;

  @NotBlank
  private String bankAccountName;
}
