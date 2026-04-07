package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

  List<Cinema> findTop12ByOrderByCommunityRatingDesc();
}