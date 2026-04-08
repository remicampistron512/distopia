package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.Cinema;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

  List<Cinema> findTop12ByOrderByCommunityRatingDesc();
  @Query("""
        select c
        from Cinema c
        left join fetch c.showings
        where c.id = :id
    """)
  Optional<Cinema> findByIdWithShowings(@Param("id") Long id);
}