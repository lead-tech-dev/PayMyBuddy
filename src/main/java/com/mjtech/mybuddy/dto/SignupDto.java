package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * The SignupDto class implements a SignupDto
 * entity.
 */
@lombok.Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String retypePassword;

  @NotBlank
  private String email;
}
