package com.vn.controller;

import com.vn.dto.CarDTO;
import com.vn.entities.Car;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CarController {
    @Autowired
    CarService carService;

    @GetMapping("/search/car")
    public String searchCarPage(Model model,
                                @RequestParam(name = "city", required = false) String city,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        model.addAttribute("city",city);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage -1, pageSize);
        Page<Car> resultPage = carService.findByCity(city, pageable);
        int totalPages = resultPage.getTotalPages();
        List<CarDTO> carDTOs = new ArrayList<>();
        for (Car car: resultPage.getContent()) {
            carDTOs.add(new CarDTO(car));
        }
        model.addAttribute("carDTOs", carDTOs);
        model.addAttribute("totolCar", resultPage.getTotalElements());

        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage +2 , totalPages);

            if (totalPages > 5) {
                if (end == totalPages) {
                    start = end - 4;
                } else {
                    if (start == 1) {
                        end = start + 4;
                    }
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start,end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("pageSize",pageSize);
            model.addAttribute("totolPage",totalPages);
        }


        return "listCarSearch";
    }
}
