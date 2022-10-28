package com.vn.controller;


import com.vn.entities.Member;
import com.vn.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GeneralController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpPage(@ModelAttribute("member")Member member, Model model) {

        Member checkMem = memberService.findUserByEmail(member.getEmail());
        if (checkMem != null) {
            model.addAttribute("signUpMsg", "Email is taken!");
            return "signup";
        }

        memberService.save(member);
        return "redirect:/signing";
    }

    @GetMapping("/signing")
    public String signIn() {
        return "signing";
    }

    @PostMapping("/signing")
    public String signInPage(@ModelAttribute("member")Member member) {

        return "redirect:/home";
    }

    @GetMapping("/forgot-pass-step1")
    public String forgotPass1() {

        return "forgot-pass-step1";
    }

    @PostMapping("/forgot-pass-step1")
    public String forgotPass1Post(@ModelAttribute("member") Member member, Model model) {

        Member checkMem = memberService.findUserByEmail(member.getEmail());
        if (checkMem == null) {
            model.addAttribute("message", "Email does not exist!");
            return "forgot-pass-step1";
        }

        return "redirect:/forgot-pass-step2";
    }

    @GetMapping("/forgot-pass-step2")
    public String forgotPass2() {
        return "forgot-pass-step2";
    }


}
