package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.user.User;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
   private final UserService userService;
   private final EmployeeService employeeService;
   private final CustomerService customerService;
   @GetMapping("/employeeDashboard")
   public String employeeDashboard(Model model,Principal principal) {
       String username=principal.getName();
       Optional<User> userOptional=userService.findByUsername(username);
       if(userOptional.isPresent()){
            Optional<Employee> optionalEmployee=employeeService.findByUser(userOptional.get());
            List<Report> reports=optionalEmployee.get().getReports();
            model.addAttribute("reports",reports);
            model.addAttribute("reportCounter",reports.size());
       }
       return "employeeDashboard";
   }
    @GetMapping("/customerDashboard")
    public String customerDashboard(Model model,Principal principal) {
        String username=principal.getName();
        Optional<User> userOptional=userService.findByUsername(username);
        if(userOptional.isPresent()){
            Optional<Customer> optionalCustomer=customerService.findByUser(userOptional.get());
            List<Report> reports=optionalCustomer.get().getReports();
            model.addAttribute("reports",reports);
            model.addAttribute("reportCounter",reports.size());
        }
        return "customerDashboard";
    }

}
