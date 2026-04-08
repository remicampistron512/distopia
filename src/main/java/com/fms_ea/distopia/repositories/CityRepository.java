package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Cinema;
import com.fms_ea.distopia.entities.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends JpaRepository<City, Long> {

  List<City> findAllByOrderByNameAsc();


  @Query("""
        select c
        from City c
        left join fetch c.cinemas
        where c.id = :id
    """)
  Optional<City> findByIdWithCinemas(@Param("id") Long id);


}