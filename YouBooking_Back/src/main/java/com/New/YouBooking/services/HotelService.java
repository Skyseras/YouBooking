package com.New.YouBooking.services;

import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Reservation;
import com.New.YouBooking.models.Room;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface HotelService {
    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();
    Hotel getById(Long id);
    Page<Hotel> getAllHotels(int page,int size);


    Hotel updateHotel(Long id,Hotel hotel);
    Hotel deleteHotel(Hotel hotel);


    Hotel validateHotel(Hotel hotel);
    Hotel declineHotel(Hotel hotel);
    Boolean isExiste(Hotel hotel);
    Room addRoom(Long id,Room room);

/*
    List<Hotel> filterByCriteria(FilterCriteria criteria);
    Hotel nonAvailable(Long id,LocalDate startNonAvailable,LocalDate endNonAvailable);
    Hotel makeHotelAvailable(Long id);
    Room addRoom(Long id, Room room);
    Boolean isHotelAvailable(Reservation reservation);

 */
}
