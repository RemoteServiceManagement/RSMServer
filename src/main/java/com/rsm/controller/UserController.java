package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.Device;
import com.rsm.device.DeviceService;
import com.rsm.device.log.remote.connection.RemoteServiceCredential;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.ReportService;
import com.rsm.role.RoleService;
import com.rsm.user.User;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final RoleService roleService;
    private final ReportService reportService;
    private static final String masterRole = "MASTER";


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
    public String getAllClients(Model model, Principal principal) {
        List<Customer> users = customerService.findAll();
        int reportCounter = getReportsSize(principal);
        int employeeCounter = employeeService.findAll().size();
        int deviceCounter = deviceService.findAll().size();
        model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("clientCounter", users.size());
        model.addAttribute("employeeCounter", employeeCounter);
        model.addAttribute("reportCounter", reportCounter);
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

    @GetMapping("/allClients/{userId}/devices")
    public String getCustomerDevices(@PathVariable Long userId, Model model) {
        model.addAttribute("devices", customerService.findCustomersDevices(userId));
        Optional<Customer> optionalCustomer = customerService.findById(userId);
        optionalCustomer.ifPresent(customer -> model.addAttribute("user", customer));
        return "customer/devices";
    }

    @GetMapping("/allClients/{userId}/addDeviceForm")
    public String reportForm(@PathVariable Long userId, Model model) {
        Device device = new Device();
        device.setRemoteServiceCredential(new RemoteServiceCredential());
        Optional<Customer> optionalCustomer = customerService.findById(userId);
        optionalCustomer.ifPresent(customer -> model.addAttribute("user", customer));
        model.addAttribute(device);
        return "customer/addDeviceForm";
    }

    @PostMapping("/allClients/{userId}/addDeviceForm")
    public String reportFormPost(@PathVariable Long userId, @ModelAttribute Device device,
                                 BindingResult result, Model model,
                                 Principal principal) {
        Optional<Customer> optionalCustomer = customerService.findById(userId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            device.setCustomer(customer);
            device.getRemoteServiceCredential().setCustomer(customer);
            deviceService.save(device);
            model.addAttribute("user", customer);
        }
        return "redirect:/allClients/{userId}/devices".replace("{userId}", userId.toString());
    }


    @RequestMapping("/allEmployees")
    public String getAllEmployees(Model model, Principal principal) {
        List<Employee> users = employeeService.findAll();
        int reportCounter = getReportsSize(principal);
        int clientCounter = customerService.findAll().size();
        int deviceCounter = deviceService.findAll().size();
        model.addAttribute("deviceCounter", deviceCounter);
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", users.size());
        model.addAttribute("reportCounter", reportCounter);
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

    public int getReportsSize(Principal principal) {
        Optional<Customer> optionalCustomer = customerService.findByUsername(principal.getName());
        Optional<Employee> optionalEmployee = employeeService.findByUsername(principal.getName());
        if(optionalCustomer.isPresent()) {
            return optionalCustomer.get().getReports().size();
        }
        if(optionalEmployee.isPresent()) {
            if (optionalEmployee.get().getRoles().contains(roleService.findRoleByRoleName(masterRole).orElse(null))) {
                return reportService.findUnassigned().size();
            } else {
                return optionalEmployee.get().getReports().size();
            }
        }
        return 0;
    }
}
