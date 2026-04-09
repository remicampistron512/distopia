package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.repositories.MovieRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for movie business logic.
 * Handles movie retrieval and persistence.
 */
@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;

  /**
   * Returns all movies.
   *
   * @return list of movies
   */
  public List<Movie> findAll() {
    return movieRepository.findAll();
  }

  /**
   * Finds a movie by id.
   *
   * @param id movie id
   * @return movie
   */
  public Movie findById(Long id) {
    return movieRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Film non trouvé avec id : " + id));
  }

  /**
   * Saves a movie and converts actors text into a list.
   *
   * @param movie movie to save
   * @param actorsText actors as comma-separated text
   * @return saved movie
   */
  public Movie save(Movie movie, String actorsText) {

    if (actorsText != null && !actorsText.isBlank()) {
      List<String> actors = Arrays.stream(actorsText.split(","))
          .map(String::trim)
          .toList();
      movie.setActors(actors);
    }

    return movieRepository.save(movie);
  }

  /**
   * Deletes a movie by id.
   *
   * @param id movie id
   */
  public void deleteById(Long id) {
    movieRepository.deleteById(id);
  }

  /**
   * Finds a movie with its showings.
   *
   * @param id movie id
   * @return movie with related showings
   */
  public Movie findByIdWithShowings(Long id) {
    return movieRepository.findByIdWithShowings(id)
        .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'id: " + id));
  }
}