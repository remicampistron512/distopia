package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import com.fms_ea.distopia.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;

  public List<Movie> findAll() {
    return movieRepository.findAll();
  }

  public Movie findById(Long id) {
    return movieRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Film non trouvé avec id : " + id));
  }

  public Movie save(Movie movie, String actorsText) {

    // Conversion du champ texte en liste
    if (actorsText != null && !actorsText.isBlank()) {
      List<String> actors = Arrays.stream(actorsText.split(","))
          .map(String::trim)
          .toList();
      movie.setActors(actors);
    }

    return movieRepository.save(movie);
  }

  public void deleteById(Long id) {
    movieRepository.deleteById(id);
  }

  public Movie findByIdWithShowings(Long id) {
    return movieRepository.findByIdWithShowings(id)
        .orElseThrow(() -> new RuntimeException("Film non trouvé avec l'id: " + id));
  }
}