package com.fms_ea.distopia.web;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.services.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
class HomeControllerMockMvcTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private HomeService homeService;

  @Test
  void home_shouldDisplayHomePageWithModelAttributes() throws Exception {
    // Arrange
    List<Movie> movies = List.of(new Movie(), new Movie());
    List<Cinema> cinemas = List.of(new Cinema());
    List<City> cities = List.of(new City(), new City());
    List<Showing> showings = List.of(new Showing());

    when(homeService.getFeaturedMovies()).thenReturn(movies);
    when(homeService.getTopCinemas()).thenReturn(cinemas);
    when(homeService.getAllCities()).thenReturn(cities);
    when(homeService.getUpcomingShowings()).thenReturn(showings);

    // Act + Assert
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andExpect(model().attribute("movies", sameInstance(movies)))
        .andExpect(model().attribute("cinemas", sameInstance(cinemas)))
        .andExpect(model().attribute("cities", sameInstance(cities)))
        .andExpect(model().attribute("showings", sameInstance(showings)))
        .andExpect(model().attribute("currentPage", equalTo("home")));

    verify(homeService).getFeaturedMovies();
    verify(homeService).getTopCinemas();
    verify(homeService).getAllCities();
    verify(homeService).getUpcomingShowings();
  }
}