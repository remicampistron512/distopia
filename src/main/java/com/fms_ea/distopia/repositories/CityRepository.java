package com.fms_ea.distopia.repositories;

import com.fms_ea.distopia.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

  List<City> findAllByOrderByNameAsc();
}