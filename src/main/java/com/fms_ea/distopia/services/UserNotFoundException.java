package com.fms_ea.distopia.services;

/**
 * Exception thrown when a user
 * cannot be found during authentication.
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Creates a new exception with a message.
   *
   * @param message error message
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}