package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthInterceptorTest {

  private AuthInterceptor authInterceptor;

  private HttpServletRequest request;
  private HttpServletResponse response;
  private Object handler;

  @BeforeEach
  void setUp() {
    authInterceptor = new AuthInterceptor();

    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    handler = new Object();
  }

  @Test
  void preHandle_shouldAllowHomePageWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowIndexPageWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/index");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowLoginPageWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/login");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowLogoutPageWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/logout");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowCssResourcesWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/css/style.css");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowJsResourcesWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/js/app.js");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldAllowImageResourcesWithoutAuthentication() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/images/logo.png");

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request, never()).getSession(false);
    verify(response, never()).sendRedirect(anyString());
  }

  @Test
  void preHandle_shouldRedirectToLogin_whenProtectedRouteHasNoSession() throws Exception {
    // Arrange
    when(request.getRequestURI()).thenReturn("/admin");
    when(request.getSession(false)).thenReturn(null);

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertFalse(result);

    verify(request).getRequestURI();
    verify(request).getSession(false);
    verify(response).sendRedirect("/login");
  }

  @Test
  void preHandle_shouldRedirectToLogin_whenProtectedRouteHasSessionWithoutLoggedUser() throws Exception {
    // Arrange
    HttpSession session = mock(HttpSession.class);

    when(request.getRequestURI()).thenReturn("/admin");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("loggedUser")).thenReturn(null);

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertFalse(result);

    verify(request).getRequestURI();
    verify(request).getSession(false);
    verify(session).getAttribute("loggedUser");
    verify(response).sendRedirect("/login");
  }

  @Test
  void preHandle_shouldAllowProtectedRoute_whenUserIsLoggedIn() throws Exception {
    // Arrange
    HttpSession session = mock(HttpSession.class);
    SessionUser loggedUser = new SessionUser(1L, "admin", "ADMIN");

    when(request.getRequestURI()).thenReturn("/admin");
    when(request.getSession(false)).thenReturn(session);
    when(session.getAttribute("loggedUser")).thenReturn(loggedUser);

    // Act
    boolean result = authInterceptor.preHandle(request, response, handler);

    // Assert
    assertTrue(result);

    verify(request).getRequestURI();
    verify(request).getSession(false);
    verify(session).getAttribute("loggedUser");
    verify(response, never()).sendRedirect(anyString());
  }
}