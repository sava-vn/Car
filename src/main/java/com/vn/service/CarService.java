package com.vn.service;

import com.vn.entities.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {
    Car findCarByLicensePlate(String licensePlate);

    Integer saveCar(Car car);

    List<Car> findAll();
}
