package com.fms_ea.distopia.services;

/**
 * Exception thrown when authentication fails
 * because of invalid credentials.
 */
public class BadCredentialsException extends RuntimeException {

  /**
   * Creates a new exception with a message.
   *
   * @param message error message
   */
  public BadCredentialsException(String message) {
    super(message);
  }
}