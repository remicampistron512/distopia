package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.repositories.ShowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowingService {

  private final ShowingRepository showingRepository;

  public List<Showing> findAll() {
    return showingRepository.findAll();
  }

  public Showing findById(Long id) {
    return showingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Séance non trouvée avec id : " + id));
  }

  public Showing save(Showing showing) {
    return showingRepository.save(showing);
  }

  public void deleteById(Long id) {
    showingRepository.deleteById(id);
  }
}