package com.New.YouBooking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "username")
    private String username;

    private String password;

    @ManyToMany(fetch = EAGER)
    private Collection<AppUserRole> appUserRoles = new ArrayList<>();

    @OneToMany(mappedBy = "manager",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Hotel> hotels;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Reservation> reservations;
}
