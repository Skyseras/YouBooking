package com.New.YouBooking.controllers;

import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Room;
import com.New.YouBooking.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("**")
@RequestMapping("api/room")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rooms);
    }
    @GetMapping("/{id}")
    public ResponseEntity getRoomById(@PathVariable Long id){
        Room room = roomService.getRoomById(id);
        if(room == null){
            return ResponseEntity.badRequest().body("room not found");
        }
        return ResponseEntity.ok().body(room);
    }
    @PutMapping("/updateRoom")
    public ResponseEntity updateRoom(@RequestBody Room room){
        Room roomResponse = roomService.updateRoom(room);
        if(roomResponse==null){
            return ResponseEntity.badRequest().body("room not updated");
        }
        return ResponseEntity.ok().body(room);
    }
    @DeleteMapping("/deleteRoom")
    public ResponseEntity deleteRoom(@RequestBody Room room){
        Room roomResponse = roomService.deleteRoom(room);
        if(roomResponse==null){
            return ResponseEntity.badRequest().body("room not updated");
        }
        return ResponseEntity.ok().body(room);
    }
}
