package com.vn.controller.quannp1;

import com.vn.entities.Car;
import com.vn.entities.Member;
import com.vn.service.CarService;
import com.vn.service.MemberService;
import com.vn.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);
        return "car/testAddCar";
    }

    @PostMapping("/Add-Car")
    public String addCarForm(@ModelAttribute("car") Car car, Model model,
                             RedirectAttributes ra,
                             @RequestParam("pregistration") MultipartFile multipartFile1,
                             @RequestParam("pinspection") MultipartFile multipartFile2,
                             @RequestParam("pinsuranceUrl") MultipartFile multipartFile3,
                             @RequestParam("pimages") MultipartFile[] multipartFileImage) throws IOException {
        Car carCheck = carService.findCarByLicensePlate(car.getLicensePlate());
        if (carCheck != null) {
            model.addAttribute("msg", "Car is already exits");
            return "car/testAddCar";
        }
        //Lay ten goc cua anh them vao car
        String registrationPaper = StringUtils.cleanPath(multipartFile1.getOriginalFilename());
        String cetifiticateInspection = StringUtils.cleanPath(multipartFile2.getOriginalFilename());
        String insurance = StringUtils.cleanPath(multipartFile3.getOriginalFilename());
        car.setRegistration(registrationPaper);
        car.setInspection(cetifiticateInspection);
        car.setInsuranceUrl(insurance);
        String[] carImages = new String[3];
        for (MultipartFile listImages : multipartFileImage) {
            String images = StringUtils.cleanPath(listImages.getOriginalFilename());
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(carImages));
            arrayList.add(images);
            carImages = arrayList.toArray(carImages);
        }
        String saveCarImages = Arrays.toString(carImages);
        car.setImages(saveCarImages);

        CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member m = new Member();
        m.setFullName(detail.getUsername());
        model.addAttribute("user", m);

        Member member = memberService.findById(detail.getId());
        car.setMember(member);

        Car saveCars = carService.saveCar(car);
        String uploadDir = "./src/main/resources/static/images/" + saveCars.getId();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile1.getInputStream()) {
            Path filePath = uploadPath.resolve(registrationPaper);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = multipartFile2.getInputStream()) {
            Path filePath = uploadPath.resolve(cetifiticateInspection);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = multipartFile3.getInputStream()) {
            Path filePath = uploadPath.resolve(insurance);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (MultipartFile listImages : multipartFileImage) {
            String images = StringUtils.cleanPath(listImages.getOriginalFilename());
            try (InputStream inputStream = listImages.getInputStream()) {
                Path filePath = uploadPath.resolve(images);
                System.out.println(filePath.toFile().getAbsolutePath());
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ra.addFlashAttribute("msg", "Your car add succesfull!");
        return "redirect:/listCar";
    }

    @GetMapping("/car/edit/{id}")
    public String getEditCar(@PathVariable("id") Integer id, Model model) {
        Car car = carService.findCarById(id);
        model.addAttribute("car", car);
        return "car/testEditCar";
    }

    @PostMapping("/car/edit/{id}")
    public String submitEditCar(@ModelAttribute Car car, @PathVariable("id") Integer id, Model model) {
        carService.saveCar(car);
        model.addAttribute("complete", "Edit car successful");
        return "redirect:/listCar";
    }

}