package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.repositories.CinemaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for cinema business logic.
 * Handles cinema retrieval and persistence.
 */
@Service
@RequiredArgsConstructor
public class CinemaService {

  private final CinemaRepository cinemaRepository;

  /**
   * Returns all cinemas.
   *
   * @return list of cinemas
   */
  public List<Cinema> findAll() {
    return cinemaRepository.findAll();
  }

  /**
   * Finds a cinema by id.
   *
   * @param id cinema id
   * @return cinema
   */
  public Cinema findById(Long id) {
    return cinemaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec id : " + id));
  }

  /**
   * Finds a cinema with its showings.
   *
   * @param id cinema id
   * @return cinema with related showings
   */
  public Cinema findByIdWithShowings(Long id) {
    return cinemaRepository.findByIdWithShowings(id)
        .orElseThrow(() -> new RuntimeException("Cinéma non trouvé"));
  }

  /**
   * Saves a cinema.
   *
   * @param cinema cinema to save
   * @return saved cinema
   */
  public Cinema save(Cinema cinema) {
    return cinemaRepository.save(cinema);
  }

  /**
   * Deletes a cinema by id.
   *
   * @param id cinema id
   */
  public void deleteById(Long id) {
    cinemaRepository.deleteById(id);
  }

  /**
   * Searches cinemas by name.
   *
   * @param kw search keyword
   * @return matching cinemas
   */
  public Object findByNameContains(String kw) {
    return cinemaRepository.findByNameContains(kw);
  }
}