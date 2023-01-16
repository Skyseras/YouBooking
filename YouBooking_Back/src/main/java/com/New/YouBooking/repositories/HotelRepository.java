package com.New.YouBooking.repositories;

import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.enums.StatusHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
    List<Hotel> findAll(Specification<Hotel> hotelSpecification);
    Page<Hotel> findAllByStatus(Pageable pageable, StatusHotel status);
    Hotel findByName(String name);
    Hotel findByAddress(String address);
}
