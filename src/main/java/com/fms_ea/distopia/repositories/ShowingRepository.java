package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Showing;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for showing persistence.
 * Provides database access for Showing entities.
 */
public interface ShowingRepository extends JpaRepository<Showing, Long> {

  /**
   * Finds the next 10 upcoming showings.
   *
   * @param now current date and time
   * @return upcoming showings ordered by date
   */
  List<Showing> findTop10ByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime now);
}