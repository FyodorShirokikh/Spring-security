package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index";
    }
    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }
    @GetMapping("/admin")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.showUsers());
        return "admin";
    }
    @GetMapping("/user")
    public String showUserDetails(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "showuser";
    }
    @GetMapping("/admin/new")
    public String processRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "adduser";
    }
    @PostMapping("/admin/process_register")
    public String processRegister(User user) {
        userService.registerDefaultUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.get(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "edituser";
    }
    @PostMapping("/admin/save")
    public String saveUser(User user) {
        userService.save(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
