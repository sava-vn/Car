package com.vn.controller;


import com.vn.entities.Member;
import com.vn.service.MemberService;
import com.vn.utils.Utility;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class GeneralController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Utility utility;

    @GetMapping("/home")
    public String homePage() {
        return "home_logout";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpPage(@ModelAttribute("member")Member member, Model model) {

        Member checkMem = memberService.findUserByEmailAndFullName(member.getEmail(), member.getFullName());
        if (checkMem != null) {
            model.addAttribute("msg", "Email is taken!");
            return "signup";
        }
        memberService.save(member);
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @PostMapping("/signin")
    public String signInPage(@ModelAttribute("member")Member member) {

        return "redirect:/home";
    }

    @GetMapping("/forgot_password")
    public String forgotPassForm() {

        return "forgot_password";
    }

    @PostMapping("/forgot_password")
    public String forgotPassProcessing(@ModelAttribute("member") Member member,
                                       Model model,
                                       HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            memberService.updateResetPasswordToken(token, email);
            String resetPassLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            utility.sendEmail(email, resetPassLink);

        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Something wrong while sending email!");
        }

        return "forgot_password";
    }

    @GetMapping("/reset_password")
    public String resetPassForm(@Param(value = "token") String token, Model model) {
        Member member = memberService.findByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (member == null) {
            model.addAttribute("message", "The request is expired! Please send a new request to reset your password by entering your email again on previous step!");
            return "reset_password";
        }

        return "reset_password";
    }

    @PostMapping("/reset_password")
    public String resetPassProcessing(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Member member = memberService.findByResetPasswordToken(token);

        if (member == null) {
            model.addAttribute("message", "Invalid token");
            return "reset_password";
        } else {
            memberService.updatePassword(member, password);
            model.addAttribute("message", "Reset password successfully!");
        }

        return "reset_password_success";
    }

    @GetMapping("/logout")
    public String logOut() {
        return "Home";
    }

}