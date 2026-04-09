package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Cinema;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for cinema persistence.
 * Provides database access for Cinema entities.
 */
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

  /**
   * Finds the top 12 highest-rated cinemas.
   *
   * @return featured cinemas
   */
  List<Cinema> findTop12ByOrderByCommunityRatingDesc();

  /**
   * Finds a cinema with its showings, movies, and city.
   *
   * @param id cinema id
   * @return cinema with related data
   */
  @Query("""
    select distinct c
    from Cinema c
    left join fetch c.showings s
    left join fetch s.movie
    left join fetch c.city
    where c.id = :id
  """)
  Optional<Cinema> findByIdWithShowings(@Param("id") Long id);

  /**
   * Finds cinemas by name keyword.
   *
   * @param kw search keyword
   * @return matching cinemas
   */
  List<Cinema> findByNameContains(String kw);
}