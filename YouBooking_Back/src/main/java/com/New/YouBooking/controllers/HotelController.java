package com.New.YouBooking.controllers;

import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Room;
import com.New.YouBooking.services.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/hotel")
public class HotelController {
    private HotelService hotelService;
    protected HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }
    @GetMapping("/")
    public ResponseEntity<List<Hotel>> getAllHotels(){
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    @GetMapping("/hotels")
    public ResponseEntity<Page<Hotel>> getAllHotels(@RequestParam (defaultValue = "0") int page){
        Page<Hotel> hotels = hotelService.getAllHotels(page, 10);
        System.out.println(hotels);
        return ResponseEntity.ok(hotels);
    }
    @PutMapping("/validateHotel")
    public ResponseEntity<Hotel> validateHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.validateHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @PutMapping("/declineHotel")
    public ResponseEntity<Hotel> declineHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.declineHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @PostMapping("/createHotel")
    public ResponseEntity createHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.createHotel(hotel);
        if(response != null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(hotel);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity getHotel(@PathVariable Long id){
        Hotel hotel = hotelService.getById(id);
        if(hotel!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotel);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel N° "+id+"n'existe pas");
        }
    }
    @DeleteMapping("/deleteHotel")
    public ResponseEntity deleteHotel(@RequestBody Hotel hotel){
        Hotel response = hotelService.deleteHotel(hotel);
        if(response!=null){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hotel N° "+hotel.getId()+" is deleted");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel N° "+hotel.getId()+" not found");
        }
    }
    @PostMapping("/addRoom/{id}")
    public ResponseEntity addRoom(@PathVariable Long id,@RequestBody Room room){
        Room response = hotelService.addRoom(id, room);
        if(response.equals(room)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
