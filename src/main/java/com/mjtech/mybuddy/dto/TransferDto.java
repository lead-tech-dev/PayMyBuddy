package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The TransferDto class implements a TransferDto
 * entity.
 */
@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

  @NotBlank
  private String transfer;

  @NotBlank
  private String description;

  private int amount;
}
