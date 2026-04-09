package com.fms_ea.distopia.services;

import com.fms_ea.distopia.entities.Showing;
import com.fms_ea.distopia.repositories.ShowingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for showing business logic.
 * Handles showing retrieval and persistence.
 */
@Service
@RequiredArgsConstructor
public class ShowingService {

  private final ShowingRepository showingRepository;

  /**
   * Returns all showings.
   *
   * @return list of showings
   */
  public List<Showing> findAll() {
    return showingRepository.findAll();
  }

  /**
   * Finds a showing by id.
   *
   * @param id showing id
   * @return showing
   */
  public Showing findById(Long id) {
    return showingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Séance non trouvée avec id : " + id));
  }

  /**
   * Saves a showing.
   *
   * @param showing showing to save
   * @return saved showing
   */
  public Showing save(Showing showing) {
    return showingRepository.save(showing);
  }

  /**
   * Deletes a showing by id.
   *
   * @param id showing id
   */
  public void deleteById(Long id) {
    showingRepository.deleteById(id);
  }
}