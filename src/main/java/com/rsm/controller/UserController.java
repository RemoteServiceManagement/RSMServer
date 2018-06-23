package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.DeviceService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.user.User;
import com.rsm.user.UserDetails;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final DeviceService deviceService;

    @RequestMapping("/login")
    public String login() {
        return "user/login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", "Złe dane logowania!");
        return "user/login";
    }

    @RequestMapping("/userDetails")
    public String getUserDetails(Model model, Principal principal) {

        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if(optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
        }
        return "userDetails";
    }

    @RequestMapping("/allClients")
    public String getAllClients(Model model) {
        List<Customer> users = customerService.findAll();
        int employeeCounter = employeeService.findAll().size();
        int deviceCounter = deviceService.findAll().size();
        model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("clientCounter", users.size());
        model.addAttribute("employeeCounter", employeeCounter);
        model.addAttribute("users", users);
        return "allClientsList";
    }

    @RequestMapping("/allClients/{userId}")
    public String getClientDetails(@PathVariable("userId") Long userId, Model model) {
        Optional<Customer> optionalCustomer = customerService.findById(userId);
        if(optionalCustomer.isPresent()) {
            model.addAttribute("user", optionalCustomer.get());
        }
        return "userDetails";
    }

    @RequestMapping("/allEmployees")
    public String getAllEmployees(Model model) {
        List<Employee> users = employeeService.findAll();
        int clientCounter = customerService.findAll().size();
        int deviceCounter = deviceService.findAll().size();
        model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", users.size());
        model.addAttribute("users", users);
        return "allEmployeesList";
    }

    @RequestMapping("/allEmployees/{userId}")
    public String getEmployeeDetails(@PathVariable("userId") Long userId, Model model) {
        Optional<Employee> optionalEmployee = employeeService.findById(userId);
        if(optionalEmployee.isPresent()) {
            model.addAttribute("user", optionalEmployee.get());
        }
        return "userDetails";
    }
}
