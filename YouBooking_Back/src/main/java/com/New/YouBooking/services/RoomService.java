package com.New.YouBooking.services;

import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Reservation;
import com.New.YouBooking.models.Room;

import java.util.List;

public interface RoomService {
    Room addRoom(Hotel hotel, Room room);
    Room updateRoom(Room room);
    Room deleteRoom(Room room);
    List<Room> getAllRooms();
    Boolean isRoomAvailable(Reservation reservation);
    Room getRoomById(Long id);
}
