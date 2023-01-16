package com.New.YouBooking.controllers;

import com.New.YouBooking.models.Reservation;
import com.New.YouBooking.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("**")
@RequestMapping("api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @PostMapping("/addReservation")
    public ResponseEntity saveReservation(@Validated @RequestBody Reservation reservation){
        try{
            Reservation reservationResponse = reservationService.addReservation(reservation);
            if(reservationResponse!=null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
            }else{
                return ResponseEntity.badRequest().body("Invalid DATA");
            }
        }catch (IllegalStateException e){
            String message = e.getMessage();
            return ResponseEntity.status(401).body(message);
        }
    }
    @PutMapping("/cancelReservation")
    public ResponseEntity cancelReservation(@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.cancelReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("reservation can't be canceled");
        }
    }
    @PutMapping("/confirmReservation")
    public ResponseEntity confirmReservation(@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.confirmReservation(reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("reservation can't be confirmed");
        }
    }
    @PutMapping("/updateReservation/{ref}")
    public ResponseEntity updateReservation(@PathVariable String ref,@RequestBody Reservation reservation){
        Reservation reservationResponse = reservationService.updateReservation(ref, reservation);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("reservation can't be updated");
        }
    }
    @GetMapping("/{ref}")
    public ResponseEntity getReservationByRef(@PathVariable String ref){
        Reservation reservationResponse = reservationService.getReservationByRef(ref);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("reservation not found");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity getReservationById(@PathVariable Long id){
        Reservation reservationResponse = reservationService.getReservationById(id);
        if(reservationResponse!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
        }else{
            return ResponseEntity.badRequest().body("reservation not found");
        }
    }
    @GetMapping("/")
    public ResponseEntity getAllReservations(){
        List<Reservation> reservationResponse = reservationService.getAllReservations();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationResponse);
    }

}
