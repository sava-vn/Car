package com.vn.controller.sangnt24;

import com.vn.entities.Car;
import com.vn.entities.Member;
import com.vn.service.CarService;
import com.vn.service.MemberService;
import com.vn.service.impl.CustomUserDetails;
import com.vn.utils.CarStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CarOwnerController {
    @Autowired
    private CarService carService;
    @Autowired
    private MemberService memberService;

    // List Car
    @GetMapping("/listCar")
    public String listCarByMemberId(Model model,
                              @RequestParam(name = "id", required = false) Integer id,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              @RequestParam("sort") Optional<String> sort) {

        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        String sortType = sort.orElse("none");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("id", id);
        model.addAttribute("sortType", sortType);

        Pageable pageable;
        switch (sortType) {
            case "ascPrice":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("price").ascending());
                break;
            case "descPrice":
                pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("price").descending());
                break;
            default:
                pageable = PageRequest.of(currentPage - 1, pageSize);
        }

        Page<Car> resultPage = carService.listCarByMemberId(detail.getId(), pageable);

        List<Car> carList = resultPage.getContent();

        long totalItems = resultPage.getTotalElements();
        int totalPages = resultPage.getTotalPages();
        model.addAttribute("carList", carList);
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("totolPage", totalPages);
        model.addAttribute("totalItems", totalItems);

        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > 5) {
                if (end == totalPages) {
                    start = end - 4;
                } else {
                    if (start == 1) {
                        end = start + 4;
                    }
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "car/listCar";
    }

    // Add Car
    @GetMapping("/addCar")
    public String addContentUi(Model model, HttpSession session) {
        // Check role Car Owner
        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        model.addAttribute("addCar", new Car());
        model.addAttribute("carStatus", CarStatusEnum.values());
        return "/car/addCar";
    }


    @PostMapping("/addCar")
    public String checkAddCar(@Valid @ModelAttribute("addCar") Car car, BindingResult result,
                              HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Validate Add Car
        if (result.hasErrors()) {
            return "/car/addCar";
        }
        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        Member member = memberService.findById(detail.getId());
        car.setMember(member);

        carService.saveCar(car);
        model.addAttribute("carStatus", CarStatusEnum.values());
        redirectAttributes.addFlashAttribute("message", "AddCar is susscessfull");
        return "redirect:/listCar";
    }

    // Edit Car
    @GetMapping("/editCar/{id}")
    public String editCar(Model model, @PathVariable("id") Integer idCar, HttpSession session) {
        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        Car car = carService.findByIdCar(idCar);

        model.addAttribute("carStatus", CarStatusEnum.values());
        model.addAttribute("editCar", car);
        return "/car/editCar";
    }

    @PostMapping("/editCar")
    public String editContentById(@ModelAttribute("editCar") Car car,
                                  Model model, RedirectAttributes redirectAttributes) {

        // Edit Car by email Member (role: Car owner)
        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        Car editCar = carService.findByIdCar(car.getId());
        editCar.setPrice(car.getPrice());
        editCar.setDeposit(car.getDeposit());
        editCar.setStatus(car.getStatus());

        carService.update(editCar);
        redirectAttributes.addFlashAttribute("message", "Edit car sucessfull");
        return "redirect:/listCar";
    }

    // Delete Car
    @GetMapping("/deleteCar/{id}")
    public String deleteCarById(Model model, @PathVariable("id") Integer idCar,
                                RedirectAttributes redirectAttributes) {
        carService.delete(idCar);
        redirectAttributes.addFlashAttribute("message", "Delete car sucessfull");
        return "redirect:/listCar";
    }
}
