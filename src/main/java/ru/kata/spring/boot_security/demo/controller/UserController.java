package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
    public String processRegister(@ModelAttribute("user") User user) {
        userService.registerDefaultUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model) {
        User user = userService.get(id);
        List<Role> listRoles = userService.listOfRoles(user);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("role1c", "False");
        model.addAttribute("role2c", "False");
        for (Role role : listRoles) {
            if (role.getName().equals("ROLE_USER")) {
                model.addAttribute("role1c", "True");
            }
            if (role.getName().equals("ROLE_ADMIN")) {
                model.addAttribute("role2c", "True");
            }
        }
        return "edituser";
    }
    @PostMapping("/admin/save")
    public String saveUser(User user,@ModelAttribute("role1c") String role1c, @ModelAttribute("role2c") String role2c) {
        List<Role> listUserRoles = userService.listOfRoles(user);
        Set<Role> listRoles = new HashSet<>();
        if (!listUserRoles.isEmpty()) {
            for (Role role : listUserRoles) {
                if (((role.getName().equals("ROLE_USER")) && ((role1c.equals("true"))||(role1c.equals("1")))) || (role1c.equals("true"))) {
                    listRoles.add(roleService.findByName("ROLE_USER"));
                }
                if (((role.getName().equals("ROLE_ADMIN")) && ((role2c.equals("true"))||(role2c.equals("1")))) || (role2c.equals("true"))) {
                    listRoles.add(roleService.findByName("ROLE_ADMIN"));
                }
            }
        }
        else {
            if (role1c.equals("true")) {
                listRoles.add(roleService.findByName("ROLE_USER"));
            }
            if (role2c.equals("true")) {
                listRoles.add(roleService.findByName("ROLE_ADMIN"));
            }
        }
        user.setRoles(listRoles);
        userService.save(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
