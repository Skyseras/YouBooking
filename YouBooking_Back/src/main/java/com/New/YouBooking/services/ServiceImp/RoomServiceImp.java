package com.New.YouBooking.services.ServiceImp;

import com.New.YouBooking.models.enums.StatusReservation;
import com.New.YouBooking.repositories.RoomRepository;
import com.New.YouBooking.services.RoomService;
import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Reservation;
import com.New.YouBooking.models.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImp implements RoomService {
    private final RoomRepository roomRepository;
    @Override
    public Room addRoom(Hotel hotel, Room room) {
        Room room1 = roomRepository.findByHotelIdAndNumber(hotel.getId(), room.getNumber());
        if (room1 !=null){
            throw new IllegalStateException("room already exist");
        }
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room deleteRoom(Room room) {
        if(room.getId()==null){
            throw new IllegalStateException("needs id");
        }
        Room roomExist = this.getRoomById(room.getId());
        if(roomExist ==null){
            throw new IllegalStateException("room not found");
        }
        roomRepository.deleteById(room.getId());
        return room;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Boolean isRoomAvailable(Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        Room room = this.getRoomById(reservation.getRoom().getId());
        if(room !=null){
            for (Reservation r : room.getReservations()) {
                if ((r!=null && (r.getStatus() == StatusReservation.CHECKING || r.getStatus() == StatusReservation.ACCEPTED) &&
                        (r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate)))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Room getRoomById(Long id) {
        Optional<Room> room = roomRepository.findById(id);
        if(!room.isPresent()){
            throw new IllegalStateException("room not found");
        }
        return room.get();
    }
}
