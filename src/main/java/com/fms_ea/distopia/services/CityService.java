package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.repositories.CityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for city business logic.
 * Handles city retrieval and persistence.
 */
@Service
@RequiredArgsConstructor
public class CityService {

  private final CityRepository cityRepository;

  /**
   * Returns all cities.
   *
   * @return list of cities
   */
  public List<City> findAll() {
    return cityRepository.findAll();
  }

  /**
   * Finds a city by id.
   *
   * @param id city id
   * @return city
   */
  public City findById(Long id) {
    return cityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ville non trouvée avec id : " + id));
  }

  /**
   * Saves a city.
   *
   * @param city city to save
   * @return saved city
   */
  public City save(City city) {
    return cityRepository.save(city);
  }

  /**
   * Deletes a city by id.
   *
   * @param id city id
   */
  public void deleteById(Long id) {
    cityRepository.deleteById(id);
  }

  /**
   * Finds a city with its cinemas.
   *
   * @param id city id
   * @return city with related cinemas
   */
  public City findByIdWithCinemas(Long id) {
    return cityRepository.findByIdWithCinemas(id)
        .orElseThrow(() -> new RuntimeException("Ville non trouvée avec id : " + id));
  }
}