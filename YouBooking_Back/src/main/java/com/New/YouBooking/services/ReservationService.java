package com.New.YouBooking.services;

import com.New.YouBooking.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    Reservation updateReservation(String ref, Reservation reservation);
    Reservation confirmReservation(Reservation reservation);
    Reservation cancelReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationByRef(String ref);
    Reservation getReservationById(Long id);
}
