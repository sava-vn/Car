package com.vn.dto;

import com.vn.entities.Car;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Data
@AllArgsConstructor
public class CarDTO {
    private Integer id;
    private String name;
    private Double rating;
    private Integer rides;
    private String location;
    private String status;
    private Double price;
    private String[] images = new String[3];


    public CarDTO() {
    }

    public CarDTO(Car car){
        this.id = car.getId();
        this.name = car.getBrand() + " " + car.getModel() + " " + car.getYear();
        this.rating = car.getRating();
        this.location = car.getCity() + " - " + car.getDistrict();
        this.rides = car.getSeat();
        this.status = "Available";
        this.price = car.getPrice();
        StringTokenizer stz = new StringTokenizer(car.getImages(),",");
        images[0] = stz.nextToken();
        images[1] = stz.nextToken();
        images[2] = stz.nextToken();
    }
}