package com.vn.repository;

import com.vn.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Car findCarByLicensePlate(String licensePlate);

    List<Car> findAll();
}
