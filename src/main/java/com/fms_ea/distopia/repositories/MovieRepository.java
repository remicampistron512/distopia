package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  List<Movie> findTop6ByOrderByCommunityRatingDesc();
}