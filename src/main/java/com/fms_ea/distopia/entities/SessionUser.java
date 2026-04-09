package com.fms_ea.distopia.entities;

/**
 * Lightweight session object used to store
 * authenticated user information.
 */
public class SessionUser {

  private Long id;
  private String username;
  private String role;

  /**
   * Creates a session user.
   *
   * @param id user id
   * @param username username
   * @param role user role
   */
  public SessionUser(Long id, String username, String role) {
    this.id = id;
    this.username = username;
    this.role = role;
  }

  /**
   * Returns user id.
   *
   * @return user id
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns username.
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns user role.
   *
   * @return role
   */
  public String getRole() {
    return role;
  }
}