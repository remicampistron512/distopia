package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

  private final CityRepository cityRepository;

  public List<City> findAll() {
    return cityRepository.findAll();
  }

  public City findById(Long id) {
    return cityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ville non trouvée avec id : " + id));
  }

  public City save(City city) {
    return cityRepository.save(city);
  }

  public void deleteById(Long id) {
    cityRepository.deleteById(id);
  }
}