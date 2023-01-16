package com.New.YouBooking.repositories;
import com.New.YouBooking.models.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {
    AppUserRole findByName(String name);
}
