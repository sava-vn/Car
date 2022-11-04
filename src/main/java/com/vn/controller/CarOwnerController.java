package com.vn.controller;

import com.vn.entities.Car;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CarOwnerController {

    @Autowired
    private CarService carService;

    @GetMapping("/homepagecarowner")
    public String homePageCarOwner() {
        return "Carowner/homepagecarowner";
    }

    @GetMapping("/Add-Car")
    public String getFormAddCar() {
        return "Carowner/addCar";
    }

    @PostMapping("/Add-Car")
        public String addCarForm (@ModelAttribute("car") Car car, Model model){
            Car carCheck = carService.findCarByLicensePlate(car.getLicensePlate());
            if(carCheck!=null){
                model.addAttribute("msg", "Car is already exits");
                return "Carowner/addCar";
            }
            carService.saveCar(car);
            return "Carowner/homepagecarowner";

    }
    @GetMapping("/List-Car")
    public String getListCar(@ModelAttribute("car") Car car, Model model){
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "Carowner/listCar";
    }


}
