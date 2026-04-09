package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for movie persistence.
 * Provides database access for Movie entities.
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {

  /**
   * Finds the top 6 highest-rated movies.
   *
   * @return featured movies
   */
  List<Movie> findTop6ByOrderByCommunityRatingDesc();

  /**
   * Finds a movie with its showings and related cinemas.
   *
   * @param id movie id
   * @return movie with associated showings
   */
  @Query("""
    select distinct m
    from Movie m
    left join fetch m.showings s
    left join fetch s.cinema
    where m.id = :id
""")
  Optional<Movie> findByIdWithShowings(@Param("id") Long id);
}