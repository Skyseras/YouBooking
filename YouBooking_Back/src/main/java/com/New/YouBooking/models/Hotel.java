package com.New.YouBooking.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.New.YouBooking.models.enums.StatusHotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Hotel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(unique = true)
    private String address;

    @NotNull
    private String city;

    @Enumerated(EnumType.STRING)
    private StatusHotel status;

    @OneToMany(mappedBy = "hotel",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties("hotel")
    private List<Room> rooms;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties("hotels")
    private AppUser manager;
}
