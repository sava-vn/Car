package com.vn.controller;

import com.vn.entities.Car;
import com.vn.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
    @GetMapping("/Add-Car/error")
    public String getFormAddCarError() {
        return "Carowner/addCar";
    }


    @PostMapping("/Add-Car")
    public String addCarForm(@ModelAttribute("car") Car car, Model model,
                             RedirectAttributes ra,
                             @RequestParam("pregistrationPaperUrl") MultipartFile multipartFile1,
                             @RequestParam("pcetifiticateInspectionUrl") MultipartFile multipartFile2,
                             @RequestParam("pinsuranceUrl") MultipartFile multipartFile3,
                             @RequestParam("pfrontImageUrl") MultipartFile multipartFile4,
                             @RequestParam("pbackImageUrl") MultipartFile multipartFile5,
                             @RequestParam("pleftImageUrl") MultipartFile multipartFile6,
                             @RequestParam("prightImageUrl") MultipartFile multipartFile7) throws IOException {
        Car carCheck = carService.findCarByLicensePlate(car.getLicensePlate());
        if (carCheck != null) {
            model.addAttribute("msg", "Car is already exits");
            return "Carowner/addCar";
        }
        String registrationPaper = StringUtils.cleanPath(multipartFile1.getOriginalFilename());
        String cetifiticateInspection = StringUtils.cleanPath(multipartFile2.getOriginalFilename());
        String insurance = StringUtils.cleanPath(multipartFile3.getOriginalFilename());

        String frontImageUrl = StringUtils.cleanPath(multipartFile4.getOriginalFilename());
        String backImageUrl = StringUtils.cleanPath(multipartFile5.getOriginalFilename());
        String leftImageUrl = StringUtils.cleanPath(multipartFile6.getOriginalFilename());
        String rightImageUrl = StringUtils.cleanPath(multipartFile7.getOriginalFilename());


        car.setRegistration(registrationPaper);
        car.setInspection(cetifiticateInspection);
        car.setInsuranceUrl(insurance);
        String images = frontImageUrl + ","
                + backImageUrl +","
                + leftImageUrl +","
                + rightImageUrl;
        car.setImages(images);

        Car savecar = carService.saveCar(car);

        String uploadDir = "./src/main/resources/static/images/" + savecar;

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
        try (InputStream inputStream = multipartFile4.getInputStream()) {
            Path filePath = uploadPath.resolve(frontImageUrl);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = multipartFile5.getInputStream()) {
            Path filePath = uploadPath.resolve(backImageUrl);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream inputStream = multipartFile6.getInputStream()) {
            Path filePath = uploadPath.resolve(leftImageUrl);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        try (InputStream inputStream = multipartFile7.getInputStream()) {
            Path filePath = uploadPath.resolve(rightImageUrl);
            System.out.println(filePath.toFile().getAbsolutePath());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ra.addFlashAttribute("msg", "Your car add succesfull!");
        return "Carowner/homepagecarowner";

    }

    @GetMapping("/booking_process")
    public String getBooking() {
        return "booking_process";
    }

    @GetMapping("/car/edit/{licensePlate}")
    public String getEditCar(@PathVariable("licensePlate") String licensePlate, Model model) {
        Car car = carService.findCarByLicensePlate(licensePlate);
        model.addAttribute("car", car);
        return "Carowner/editCar";
    }

//    @RequestMapping(value = { "/carImage" }, method = RequestMethod.GET)
//    public void carImage(HttpServletRequest request, HttpServletResponse response, Model model,
//                         @RequestParam("licensePlate") String licensePlate) throws IOException {
//        Car car = null;
//        if (licensePlate != null) {
//            car = this.carService.findCarByLicensePlate(licensePlate);
//        }
//        if (car != null && car.getFrontImageUrl() != null) {
//            response.setContentType("image/png");
//            response.getOutputStream().write(car.getFrontImageUrl().getBytes());
//        }
//        response.getOutputStream().close();
//    }

    @GetMapping("/listCar")
    public String listCar(Model model) {
        return listByPage(model, 1, "price", "asc");
    }

    @GetMapping("/page/{pageNumber}")
    public String listByPage(Model model,
                             @PathVariable("pageNumber") int currentPage,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir) {
        Page<Car> page = carService.listAll(currentPage, sortField, sortDir);
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();

        List<Car> carList = page.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("carList", carList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "Carowner/list_car";
    }

    @GetMapping("/List-Car")
    public String getListCar(@ModelAttribute("car") Car car, Model model){
        List<Car> cars = carService.findAll();
        model.addAttribute("cars", cars);
        return "Carowner/listCar";
    }

}
