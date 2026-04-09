package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.City;
import com.fms_ea.distopia.entities.Movie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  List<Movie> findTop6ByOrderByCommunityRatingDesc();

  @Query("""
    select distinct m
    from Movie m
    left join fetch m.showings s
    left join fetch s.cinema
    where m.id = :id
""")
  Optional<Movie> findByIdWithShowings(@Param("id") Long id);
}