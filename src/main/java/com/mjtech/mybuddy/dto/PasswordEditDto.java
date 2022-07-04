package com.mjtech.mybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * The PasswordEditDto class implements a PasswordEditDto
 * entity.
 */
@lombok.Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEditDto {

  private long id;

  @NotBlank
  private String oldPassword;

  @NotBlank
  private String newPassword;

  @NotBlank
  private String retypedNewPassword;

}

