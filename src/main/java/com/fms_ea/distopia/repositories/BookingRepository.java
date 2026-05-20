package com.fms_ea.distopia.repositories;


import com.fms_ea.distopia.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
