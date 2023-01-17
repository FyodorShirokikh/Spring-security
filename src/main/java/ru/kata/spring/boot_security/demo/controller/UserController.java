package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;


@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/")
    public String showUsers() {
//    public String showUsers(Model model) {
//        model.addAttribute("users", userService.showUsers());
//        return "users";
        return "index";
    }
    @GetMapping("/admin")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.showUsers());
        return "admin";
    }

    @GetMapping("/edit")
    public String updateUserForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.showUser(id));
        return "showuser";
    }

    @GetMapping("/admin/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("adduser");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @GetMapping("/admin/edit/{id}")
    public ModelAndView editUser(@PathVariable(name = "id") Long id) {
        User user = userService.showUser(id);
        ModelAndView mav = new ModelAndView("adduser");
        mav.addObject("user", user);
        List<Role> roles = (List<Role>) roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }
//    @GetMapping("/addnew")
//    public String createUserForm(User user) {
//        return "adduser";
//    }
//    @PostMapping("/")
//    public String createUser(User user) {
//        userService.add(user);
//        return "redirect:/";
//    }
//    @GetMapping("/{id}/edit")
//    public String updateUserForm(Model model, @PathVariable("id") Long id) {
//        model.addAttribute("user", userService.showUser(id));
//        return "edituser";
//    }
//    @PatchMapping("/{id}")
//    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
//        userService.edit(id, user);
//        return "redirect:/";
//    }
    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
