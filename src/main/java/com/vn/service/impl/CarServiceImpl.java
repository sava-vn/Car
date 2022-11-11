package com.vn.service.impl;


import com.vn.entities.Car;
import com.vn.repository.CarRepository;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Car saveCar(Car car) {
		return carRepository.save(car);
	}

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Page<Car> listAll(int pageNumber, String sortField, String sortDir) {
        Sort sort = Sort.by("price");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1,4,sort);
        return carRepository.findAll(pageable);
    }

}
