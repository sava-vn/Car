package com.vn.service.impl;

import com.vn.entities.Car;
import com.vn.repository.CarRepository;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    public Car findCarByLicensePlate(String licensePlate) {
        return carRepository.findCarByLicensePlate(licensePlate);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Page<Car> findByCity(String city, Pageable pageable) {
        return carRepository.findByCity(city, pageable);
    }

    @Override
    public Car findById(Integer id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Car> listCarByMemberId(Integer id, Pageable pageable) {
        return carRepository.findByMemberId(id,pageable);
    }

    public Page<Car> listAll(int pageNumber, String sortField, String sortDir, Integer id) {
        Sort sort = Sort.by("price");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1,4,sort);
        return carRepository.findByMemberId(id,pageable);
    }

    @Override
    public Car findByIdCar(Integer id) {
        return carRepository.getOne(id);
    }

    @Override
    public Car update(Car car) {
        return carRepository.save(car);
    }
    @Override
    public boolean delete(Integer id) {
        carRepository.deleteById(id);
        return true;
    }

    @Override
    public Car findCarById(Integer id) {
        return carRepository.findCarById(id);
        }
    @Override
    public Page<Car> findByCityAndDate(String city, LocalDate date, Pageable pageable) {
        System.out.println("************************************" + date);
        return carRepository.findByCityAndDate(city, date, pageable);
    }
}
