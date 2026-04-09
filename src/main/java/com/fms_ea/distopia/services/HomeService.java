package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.repositories.CinemaRepository;
import com.fms_ea.distopia.repositories.CityRepository;
import com.fms_ea.distopia.repositories.MovieRepository;
import com.fms_ea.distopia.repositories.ShowingRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for home page content.
 * Provides featured data displayed on the main page.
 */
@Service
@RequiredArgsConstructor
public class HomeService {

  private final MovieRepository movieRepository;
  private final CinemaRepository cinemaRepository;
  private final CityRepository cityRepository;
  private final ShowingRepository showingRepository;

  /**
   * Returns featured movies.
   *
   * @return top rated movies
   */
  public List<Movie> getFeaturedMovies() {
    return movieRepository.findTop6ByOrderByCommunityRatingDesc();
  }

  /**
   * Returns top rated cinemas.
   *
   * @return top cinemas
   */
  public List<Cinema> getTopCinemas() {
    return cinemaRepository.findTop12ByOrderByCommunityRatingDesc();
  }

  /**
   * Returns all cities sorted by name.
   *
   * @return list of cities
   */
  public List<City> getAllCities() {
    return cityRepository.findAllByOrderByNameAsc();
  }

  /**
   * Returns upcoming showings.
   *
   * @return next showings
   */
  public List<Showing> getUpcomingShowings() {
    return showingRepository
        .findTop10ByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime.now());
  }
}