package com.mjtech.mybuddy.web.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * CustomAccessDeniedHandlerService. class that implement
 * AccessDeniedHandler business logic
 */
public class CustomAccessDeniedHandlerService implements AccessDeniedHandler {

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.sendRedirect("/");
  }
}
