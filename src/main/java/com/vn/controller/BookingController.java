package com.vn.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vn.entities.Booking;
import com.vn.entities.Car;
import com.vn.entities.Member;
import com.vn.repository.CarRepository;
import com.vn.service.BookingService;
import com.vn.service.impl.CustomUserDetails;
import com.vn.utils.CarStatusEnum;

@Controller
public class BookingController {
    @Autowired
    private CarRepository carRepository;

	@Autowired
	BookingService bookingService;
	
	@GetMapping("/rent_car")
	public String bookingProcess(Model model,
								@RequestParam(name = "carID") Integer carID,
								HttpSession httpSession) {
		Car car = carService.findById(carID);
		model.addAttribute("car", car);
		
		httpSession.setAttribute("car", car);
		return "booking/booking_process";
	}
	
	@PostMapping("/booking_result")
	public String bookingSuccessful(@ModelAttribute("payment")Integer paymentMethod,
									HttpSession httpSession,
									Model model){
		
		Booking booking = new Booking();
		
		Car car = (Car) httpSession.getAttribute("car");
		booking.setCar(car);
		booking.setPaymentMethod(paymentMethod);
		
		Member member = new Member();
		CustomUserDetails detail = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Member> optional = memberSerivce.findUserById(detail.getId());
		if(optional.isEmpty()) {
			return "redirect:/login";
		} else member = optional.get();
		booking.setMember(member);
		
		bookingService.addBooking(booking);
		
		model.addAttribute("booking", booking);
		return "booking/booking_successful";
	}
	
	@GetMapping("/booking")
	public String bookingDetail(Model model,
								@RequestParam(name = "booking_id") Integer bookingId) {
		Booking booking = bookingService.findBookingById(bookingId);
		model.addAttribute(booking);
		return "booking/booking_detail";
	}
	
	@GetMapping("/my_booking")
	public String bookingsListByMember(Model model,
										@RequestParam(name="userID") Integer memberId) {
//		String memberName = null;
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if(auth.isAuthenticated()) {
//			memberName = auth.;
//		}
//		Member member = memberRepository.findByName(memberName);
		List<Booking> bookings = bookingService.findAllByMember(memberId);
		model.addAttribute("bookings", bookings);
		
		return "booking/booking_list";
	}

    @GetMapping("/booking/status")
    public String addStatus2(Model model) {
        model.addAttribute("carStatus", CarStatusEnum.values());
        model.addAttribute("car", new Car());

        return "car/booking_status";
    }

    // Confirm deposit
    @PostMapping("/booking/status")
    public String checkAddStatus(@ModelAttribute("car") Car car, Model model, RedirectAttributes redirectAttributes) {

//        Integer bookingId = car.getBookingId();
//        List<Booking> booking = (List<Booking>) bookingRepository.getOne(bookingId);
//
//        car.setBookings(booking);
        car.setStatus(CarStatusEnum.Booked);
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("messDeposit", "Confirm Deposit successful");
        model.addAttribute("carStatus", CarStatusEnum.Booked);

        return "redirect:/car/payment";
    }

    // Confirm payment

    @GetMapping("/car/payment")
    public String confirmPayment(Model model) {
        model.addAttribute("carStatus", CarStatusEnum.Booked);
        model.addAttribute("car", new Car());

        return "car/confirm_payment";
    }

    @PostMapping("/car/payment")
    public String checkConfirmPayment(@ModelAttribute("car") Car car, Model model, RedirectAttributes redirectAttributes) {

        car.setStatus(CarStatusEnum.Available);
//        car.getBookings().sort(new Comparator<Booking>() {
//            @Override
//            public int compare(Booking o1, Booking o2) {
//                return o2.getId() - o1.getId();
//            }
//        });
//        Booking booking = car.getBookings().get(0);
//        booking.setBookingStatus(BookingStatusEnum.Completed);

        carRepository.save(car);
        redirectAttributes.addFlashAttribute("messPayment", "Confirm Payment successful");
        model.addAttribute("carStatus", CarStatusEnum.values());

        return "redirect:/booking/status";
    }
}
