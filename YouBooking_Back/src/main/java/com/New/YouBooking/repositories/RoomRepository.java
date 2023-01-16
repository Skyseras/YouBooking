package com.New.YouBooking.repositories;

import com.New.YouBooking.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("**")
public interface RoomRepository extends JpaRepository<Room,Long> {
    Room findByHotelIdAndNumber(Long id, int number);
}
