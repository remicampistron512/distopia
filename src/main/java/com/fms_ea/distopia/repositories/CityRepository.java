package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for city persistence.
 * Provides database access for City entities.
 */
public interface CityRepository extends JpaRepository<City, Long> {

  /**
   * Finds all cities ordered by name.
   *
   * @return sorted list of cities
   */
  List<City> findAllByOrderByNameAsc();

  /**
   * Finds a city with its associated cinemas.
   *
   * @param id city id
   * @return city with cinemas
   */
  @Query("""
    select distinct c
    from City c
    left join fetch c.cinemas
    where c.id = :id
  """)
  Optional<City> findByIdWithCinemas(@Param("id") Long id);

}