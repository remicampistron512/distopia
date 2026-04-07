package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.repositories.CinemaRepository;
import com.fms_ea.distopia.repositories.CityRepository;
import com.fms_ea.distopia.repositories.MovieRepository;
import com.fms_ea.distopia.repositories.ShowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

  private final MovieRepository movieRepository;
  private final CinemaRepository cinemaRepository;
  private final CityRepository cityRepository;
  private final ShowingRepository showingRepository;

  public List<Movie> getFeaturedMovies() {
    return movieRepository.findTop6ByOrderByCommunityRatingDesc();
  }

  public List<Cinema> getTopCinemas() {
    return cinemaRepository.findTop12ByOrderByCommunityRatingDesc();
  }

  public List<City> getAllCities() {
    return cityRepository.findAllByOrderByNameAsc();
  }

  public List<Showing> getUpcomingShowings() {
    return showingRepository
        .findTop10ByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime.now());
  }
}