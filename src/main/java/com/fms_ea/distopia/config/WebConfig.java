package com.fms_ea.distopia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Spring MVC configuration.
 * Configures static resource access.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * Registers the uploads folder as a static resource location.
   *
   * @param registry resource handler registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
  }
}