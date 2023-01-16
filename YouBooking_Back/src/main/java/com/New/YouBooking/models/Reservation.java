package com.New.YouBooking.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.uuid.Generators;
import com.New.YouBooking.models.enums.StatusReservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ref;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private StatusReservation status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @PrePersist
    public void generateReference() {
        UUID uuid = Generators.timeBasedGenerator().generate();
        this.ref = uuid.toString();
    }

}
