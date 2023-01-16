package com.New.YouBooking.services.ServiceImp;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.Reservation;
import com.New.YouBooking.models.Room;
import com.New.YouBooking.models.enums.StatusReservation;
import com.New.YouBooking.repositories.ReservationRepository;
import com.New.YouBooking.services.AppUserService;
import com.New.YouBooking.services.HotelService;
import com.New.YouBooking.services.ReservationService;
import com.New.YouBooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImp implements ReservationService {
    private final AppUserService userService;
    private final RoomService roomService;
    private final ReservationRepository reservationRepository;


    @Override
    public Reservation addReservation(Reservation reservation) {
        try {
            AppUser user = userService.getUserById(reservation.getUser().getId());
            Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());
            if (reservationToCheck != null) {
                throw new IllegalStateException("Reservation already exist");
            }
            Boolean isRoomAvailable = roomService.isRoomAvailable(reservation);
            if (!isRoomAvailable) {
                throw new IllegalStateException("Room not available");
            }
            reservation.setStatus(StatusReservation.CHECKING);
            Reservation savedReservation = reservationRepository.save(reservation);
            Room room = roomService.getRoomById(savedReservation.getRoom().getId());
            room.getReservations().add(savedReservation);
            user.getReservations().add(savedReservation);
            userService.updateUser(user.getId(),user);
            roomService.updateRoom(room);
            return savedReservation;
        }
        catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public Reservation updateReservation(String ref, Reservation reservation) {
        try{
            AppUser user = userService.getUserById(reservation.getUser().getId());
            if (user == null) {
                throw new IllegalStateException("User not found");
            }
            Reservation reservationToCheck = reservationRepository.getReservationByRef(ref);
            if (reservationToCheck == null) {
                throw new IllegalStateException("Reservation not found");
            }
            if(reservationToCheck.getStatus() == StatusReservation.valueOf("ACCEPTED")){
                throw new IllegalStateException("Don't have permission to change status");
            }
            Boolean isRoomAvailable = roomService.isRoomAvailable(reservation);
            if (!isRoomAvailable) {
                throw new IllegalStateException("Room not available");
            }
            int numberOfDays = (int) ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
            Double totalPrice =Double.valueOf(Math.round(reservation.getRoom().getPrice()*numberOfDays*100)/100d);
            reservationToCheck.setTotalPrice(totalPrice);
            reservationToCheck.setStartDate(reservation.getStartDate());
            reservationToCheck.setEndDate(reservation.getEndDate());
            reservationToCheck.setRoom(reservation.getRoom());
            Reservation savedReservation = reservationRepository.save(reservationToCheck);
            return savedReservation;
        }catch (IllegalStateException e){
            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public Reservation confirmReservation(Reservation reservation) {
        Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());
        if (reservationToCheck == null) {
            throw new IllegalStateException("Reservation not found");
        }
        if(reservationToCheck.getStatus()==StatusReservation.valueOf("DECLINED")){
            throw new IllegalStateException("Reservation DECLINED");
        }
        reservationToCheck.setStatus(StatusReservation.valueOf("ACCEPTED"));
        return reservationRepository.save(reservationToCheck);
    }

    @Override
    public Reservation cancelReservation(Reservation reservation) {
        Reservation reservationToCheck = reservationRepository.getReservationByRef(reservation.getRef());

        if (reservationToCheck == null) {
            throw new IllegalStateException("Reservation not found");
        }
        Room room = reservationToCheck.getRoom();
        List<Reservation> reservations = room.getReservations();
        reservations.remove(reservationToCheck);
        reservationToCheck.setStatus(StatusReservation.valueOf("DECLINED"));
        room.getReservations().add(reservationToCheck);
        roomService.updateRoom(room);
        return reservationRepository.save(reservationToCheck);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationByRef(String ref) {
        Reservation reservation = reservationRepository.getReservationByRef(ref);
        if(reservation == null){
            throw new IllegalStateException("Reservation not found");
        }
        return reservation;
    }

    @Override
    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (!reservation.isPresent()){
            throw new IllegalStateException("Reservation not found");
        }
        return reservation.get();
    }
}
