package com.mjtech.mybuddy.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;


/**
 * SecurityUtility. The utility class that implement beans of passwordEncoder
 * and randomPassword.
 */
@Component
public class SecurityUtility {
  private static final String SALT = "salt"; // Salt should be protected carefully

  /**
   * passwordEncoder. Method that encode given password
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public static BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * randomPassword. Methode that generate a random password.
   *
   * @return String
   */
  @Bean
  public static String randomPassword() {
    String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder salt = new StringBuilder();
    Random rnd = new Random();

    while (salt.length() < 18) {
      int index = (int) (rnd.nextFloat() * SALTCHARS.length());
      salt.append(SALTCHARS.charAt(index));
    }

    String saltStr = salt.toString();

    return saltStr;
  }

}
