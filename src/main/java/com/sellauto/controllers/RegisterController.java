package com.sellauto.controllers;

import com.sellauto.model.User;
import com.sellauto.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
//register page
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //Interface for performing authentication operations on a password.

    @Autowired
    public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    //  display register page
    @GetMapping("register")
    public String displayRegister(Model model) {
        return "register";
    }
    
    // add new user
    @PostMapping("register")
    public String registerUser(@ModelAttribute("user") User user, final RedirectAttributes redirectAttributes) {
    	
    		//check whether the username already exists?
        if (userRepository.getUserByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); //set encrypted password
            user.setCreatedDate(LocalDateTime.now()); //set system time
            userRepository.save(user); //create user on the database
            return "redirect:/login";  //if registering is success, redirect to login page
        } else
        	//if registering is unsuccessful, redirect to register page
        	redirectAttributes.addFlashAttribute("message", "Username already exists.Please change");
            return "redirect:/register"; 
    }
}