package com.New.YouBooking.services.ServiceImp;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.Hotel;
import com.New.YouBooking.models.Room;
import com.New.YouBooking.models.enums.StatusHotel;
import com.New.YouBooking.repositories.HotelRepository;
import com.New.YouBooking.services.HotelService;
import com.New.YouBooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImp implements HotelService {
    private final HotelRepository hotelRepository;
    private final AppUserServiceImp userService;
    private final RoomService roomService;

    @Override
    public Hotel createHotel(Hotel hotel) {

        if(isExiste(hotel)){
            throw new IllegalStateException("hotel already exist");
        }

        if(hotel.getManager() == null || hotel.getManager().getId()==null){
            throw new IllegalStateException("Hotel needs a manager");
        }

        AppUser manager = userService.getUserById(hotel.getManager().getId());
        if(manager == null){
            throw new IllegalStateException("No manager for this hotel");
        }
        hotel.setStatus(StatusHotel.valueOf("CHECKING"));
        Hotel hotelSaved = hotelRepository.save(hotel);
        manager.getHotels().add(hotelSaved);
        return hotelSaved;
    }
    @Override
    public Hotel updateHotel(Long id,Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(id);
        if(!hotelExist) {
            throw new IllegalStateException("hotel not found");
        }
        if(hotel.getManager() == null || hotel.getManager().getId()==null){
            throw new IllegalStateException("needs a manager");
        }
        AppUser manager = userService.getUserById(hotel.getManager().getId());
        if(manager == null){
            throw new IllegalStateException("no manager found");
        }
        Hotel hotelSaved = hotelRepository.save(hotel);
        return hotelRepository.save(hotelSaved);
    }
    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    @Override
    public Page<Hotel> getAllHotels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepository.findAllByStatus(pageable,StatusHotel.ACCEPTED);
    }
    @Override
    public Hotel deleteHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(!hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " not found");
        }
        if(hotel.getManager() == null || hotel.getManager().getId()==null){
            throw new IllegalStateException("hotel needs a manager");
        }
        AppUser manager = userService.getUserById(hotel.getManager().getId());
        if(manager == null){
            throw new IllegalStateException("no manager found");
        }
        hotelRepository.deleteById(hotel.getId());
        return hotel;
    }
    @Override
    public Hotel validateHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(!hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " not found");
        }
        Hotel hotelByName = hotelRepository.findByName(hotel.getName());
        hotelByName.setStatus(StatusHotel.valueOf("ACCEPTED"));
        return hotelRepository.save(hotelByName);
    }
    @Override
    public Hotel declineHotel(Hotel hotel) {
        Boolean hotelExist = hotelRepository.existsById(hotel.getId());
        if(!hotelExist){
            throw new IllegalStateException("Hotel N°" + hotel.getId() + " not found");
        }
        Hotel hotelByName = hotelRepository.findByName(hotel.getName());
        hotelByName.setStatus(StatusHotel.valueOf("DECLINED"));
        return hotelRepository.save(hotelByName);
    }
    @Override
    public Boolean isExiste(Hotel hotel) {
        Hotel found = hotelRepository.findByName(hotel.getName());
        Boolean findById = hotel.getId() != null && hotelRepository.existsById(hotel.getId());
        return found != null || findById;
    }
    @Override
    public Hotel getById(Long id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if(!hotel.isPresent()){
            throw new IllegalStateException("Hotel not found");
        }
        return hotel.get();
    }
    @Override
    public Room addRoom(Long id, Room room) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        if(existHotel==null){
            throw new IllegalStateException("Hotel not found");
        }
        AppUser manager = userService.getUserById(existHotel.getManager().getId());
        if(manager == null){
            throw new IllegalStateException("needs manager to login");
        }
        Room savedRoom = roomService.addRoom(existHotel,room);
        existHotel.getRooms().add(savedRoom);
        hotelRepository.save(existHotel);
        return savedRoom;
    }
/*
    @Override
    public Hotel nonAvailable(Long id, LocalDate startNonAvailable, LocalDate endNonAvailable) {
        Hotel existHotel = hotelRepository.findById(id).orElse(null);
        Boolean isValidDate = hotelValidator.validDate(startNonAvailable,endNonAvailable);
        if(existHotel==null){
            throw new IllegalStateException("Hotel non touvé");
        }
        if(!isValidDate){
            throw new IllegalStateException(hotelValidator.getErrorMessage());
        }
        if(existHotel.getOwner() == null || existHotel.getOwner().getId()==null){
            throw new IllegalStateException("l'hotel doit contenir un propritaire");
        }
        Users owner = userService.getUserById(existHotel.getOwner().getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(owner == null){
            throw new IllegalStateException("le propritaire de ce Hotel n'existe pas");
        }
        if(owner.getEmail() != authentication.getName()){
            throw new IllegalStateException("vous n'avez pas le droit de modifier ce Hotel");
        }
        existHotel.setStartNonAvailable(startNonAvailable);
        existHotel.setEndNonAvailable(endNonAvailable);
        return hotelRepository.save(existHotel);
    }
    @Override
    public Boolean isHotelAvailable(Reservation reservation) {
        if(reservation.getRoom()==null || reservation.getStartDate()==null || reservation.getEndDate()==null){
            throw new IllegalStateException("les donnés de réservation est invalids");
        }
        Room room = roomService.getRoomById(reservation.getRoom().getId());
        if(room==null || room.getHotel()==null){
            throw new IllegalStateException("cette chambre n'appartient à aucun hotel");
        }
        Hotel hotel = this.getById(room.getHotel().getId());
        if(hotel.getEndNonAvailable()==null || hotel.getStartNonAvailable()==null){
            return true;
        }

        if (reservation.getStartDate().isBefore(hotel.getEndNonAvailable()) && reservation.getEndDate().isAfter(hotel.getStartNonAvailable())) {
            return false;
        }
        return true;

    }

    @Override
    public List<Hotel> filterByCriteria(FilterCriteria criteria) {
        return hotelRepository.findAll(new Specification<Hotel>(){
            @Override
            public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("status"),StatusHotel.Accépté));
                if(criteria.getCity()!=null){
                    predicates.add(cb.like(root.get("city"),"%" +criteria.getCity() + "%"));
                }
                if(criteria.getHotelName()!=null){
                    predicates.add(cb.like(root.get("name"), "%" + criteria.getHotelName() + "%"));
                }
                if(criteria.getPrixMin()!=null && criteria.getPrixMax()!=null){
                    Join<Hotel, Room> rooms = root.join("rooms");
                    predicates.add(cb.between(rooms.get("price"),criteria.getPrixMin(), criteria.getPrixMin()));
                }if (criteria.getAvailabilityStart() != null && criteria.getAvailabilityEnd() != null){
                    Join<Hotel, Room> rooms = root.join("rooms");
                    Join<Room, Reservation> reservations = rooms.join("reservations");
                    predicates.add(cb.not(cb.between(reservations.get("startDate"), criteria.getAvailabilityStart(), criteria.getAvailabilityEnd())));
                    predicates.add(cb.not(cb.between(reservations.get("endDate"), criteria.getAvailabilityStart(), criteria.getAvailabilityEnd())));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

 */

}
