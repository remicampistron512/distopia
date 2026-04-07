package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.repositories.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaService {

  private final CinemaRepository cinemaRepository;

  public List<Cinema> findAll() {
    return cinemaRepository.findAll();
  }

  public Cinema findById(Long id) {
    return cinemaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cinéma non trouvé avec id : " + id));
  }

  public Cinema save(Cinema cinema) {
    return cinemaRepository.save(cinema);
  }

  public void deleteById(Long id) {
    cinemaRepository.deleteById(id);
  }
}