package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The LoginDto class implements a LoginDto
 * entity.
 */

@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  @NotBlank
  private String email;

  @NotBlank
  private String password;
}
