package com.vn.controller.quannp1;

import com.vn.entities.Car;
import com.vn.entities.Member;
import com.vn.service.CarService;
import com.vn.service.MemberService;
import com.vn.service.impl.CustomUserDetails;
import com.vn.utils.CarStatusEnum;
import com.vn.utils.ImageUtil;
import com.vn.utils.ValidatedEditCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class CarOwnerController2 {
    @Autowired
    private CarService carService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/Add-Car")
    public String getFormAddCar(Model model) {

        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("fullName", detail.getMember().getFullName());
        return "car/addCar";
    }

    @PostMapping("/Add-Car")
    public String addCarForm(@ModelAttribute("car") Car car, Model model,
                             RedirectAttributes ra,
                             @RequestParam("pregistration") MultipartFile multipartFile1,
                             @RequestParam("pinspection") MultipartFile multipartFile2,
                             @RequestParam("pinsuranceUrl") MultipartFile multipartFile3,
                             @RequestParam("pimages") MultipartFile[] multipartFileImage,
                             RedirectAttributes redirectAttributes) throws IOException {
        Car carCheck = carService.findCarByLicensePlate(car.getLicensePlate());
        if (carCheck != null) {
            model.addAttribute("msg", "Car is already exits");
            return "car/addCar";
        }

        car.setRegistration(ImageUtil.saveImage(multipartFile1));
        car.setInspection(ImageUtil.saveImage(multipartFile2));
        car.setInsuranceUrl(ImageUtil.saveImage(multipartFile3));

        StringBuilder carImages = new StringBuilder();
        for (MultipartFile image : multipartFileImage) {
            carImages.append(ImageUtil.saveImage(image)).append(",");
        }


        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("fullName", detail.getMember().getFullName());

        Member member = memberService.findById(detail.getMember().getId());
        car.setMember(member);
        car.setStatus(CarStatusEnum.Available);

        car.setImages(carImages.toString());
        carService.saveCar(car);
        redirectAttributes.addFlashAttribute("message", "Your car add succesfull!");

        return "redirect:/listCar";
    }

    @GetMapping("/car/edit/{id}")
    public String getEditCar(@PathVariable("id") Integer id, Model model) {
        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("fullName", detail.getMember().getFullName());

        Car car = carService.findCarById(id);


        model.addAttribute("car", car);
        model.addAttribute("carStatus", CarStatusEnum.values());
        return "car/editCar";
    }

    @PostMapping("/car/edit/{id}")
    public String submitEditCar(@Validated(value = ValidatedEditCar.class) @ModelAttribute Car car, BindingResult result, @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "car/editCar";
        }

        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("fullName", detail.getMember().getFullName());

        CarStatusEnum status = car.getStatus();
        if (status.equals(CarStatusEnum.Booked)){
            model.addAttribute("messBooked","Can't change status to Booked");
            model.addAttribute("car", car);
            model.addAttribute("carStatus", CarStatusEnum.values());
            return "car/editCar";
        }

        redirectAttributes.addFlashAttribute("message", "Edit car successful");
        model.addAttribute("carStatus", CarStatusEnum.values());
        carService.saveCar(car);
        return "redirect:/listCar";
    }

}