package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Showing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Long> {

  List<Showing> findTop10ByStartDateTimeAfterOrderByStartDateTimeAsc(LocalDateTime now);
}