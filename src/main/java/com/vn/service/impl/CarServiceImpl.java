package com.vn.service.impl;

import com.vn.entities.Car;
import com.vn.repository.CarRepository;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car findCarByLicensePlate(String licensePlate) {
        return carRepository.findCarByLicensePlate(licensePlate);
    }

    @Override
    public Integer saveCar(Car car) {
        return carRepository.save(car).getId();
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

}
