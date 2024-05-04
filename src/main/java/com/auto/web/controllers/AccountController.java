package com.auto.web.controllers;

import com.auto.data.models.TuningOrders;
import com.auto.data.models.Users;
import com.auto.data.repositories.TuningOrdersRepository;

import com.auto.data.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private TuningOrdersRepository tuningOrdersRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String getAccount(Model model) {
        Users user = userService.getUserByEmail( SecurityContextHolder.getContext().getAuthentication().getName());
        //!TODO Manufacturer Model to make add car button work
        List<TuningOrders> orders = tuningOrdersRepository.findByUser_User_id(user.getUser_id());
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);

        return "account";
    }


}
