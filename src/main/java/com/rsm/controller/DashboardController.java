package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.DeviceService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.role.RoleService;
import com.rsm.user.User;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final ReportService reportService;
    private final RoleService roleService;
    private final DeviceService deviceService;
    private static final String masterRole = "MASTER";
    private static final String employeeRole = "EMPLOYEE";
    private static final String customerRole = "CUSTOMER";

    @GetMapping("")
    public String dashboard(Principal principal){
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getRoles().contains(roleService.findRoleByRoleName(masterRole).orElse(null))) {
                return "redirect:/dashboard/masterDashboard/";
            } else if(user.getRoles().contains(roleService.findRoleByRoleName(employeeRole).orElse(null))) {
                return "redirect:/dashboard/employeeDashboard/";
            } else if (user.getRoles().contains(roleService.findRoleByRoleName(customerRole).orElse(null))) {
                return "redirect:/dashboard/customerDashboard/";
            }
        }
        return "home";
    }

    @GetMapping("/employeeDashboard")
    public String employeeDashboard(Model model, Principal principal) {
        String username = principal.getName();
        Optional<Employee> optionalEmployee = employeeService.findByUsername(username);
        if (optionalEmployee.isPresent()) {
            List<Report> reports = optionalEmployee.get().getReports();
            model.addAttribute("reports", reports);
            model.addAttribute("reportCounter", reports.size());
        }
        return "employeeDashboard";
    }

    @GetMapping("/employeeDashboard/reportDetails/{reportId}")
    public String editReportDetails(@PathVariable("reportId") Long reportId, Model model) {
        Optional<Report> reportOptional = reportService.findById(reportId);
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            model.addAttribute("report", report);
            model.addAttribute("reportId", reportId);
        }
        return "reportDetails";
    }

    @PostMapping("/employeeDashboard/reportDetails")
    public String editReportDetailsPost(@ModelAttribute("report") Report report,
                                        @ModelAttribute("reportId") Long reportId) {
        Optional<Report> savedReportOptional = reportService.findById(reportId);
        if(savedReportOptional.isPresent()) {
            report.setTitle(savedReportOptional.get().getTitle());
            report.setDescription(savedReportOptional.get().getDescription());
            report.setReportDate(savedReportOptional.get().getReportDate());
            report.setReportPhoto(savedReportOptional.get().getReportPhoto());
            report.setDevice(savedReportOptional.get().getDevice());
            report.setCustomer(savedReportOptional.get().getCustomer());
            report.setEmployee(savedReportOptional.get().getEmployee());
            reportService.save(report);
        }
        return "redirect:/dashboard/employeeDashboard";
    }


    @GetMapping("/customerDashboard")
    public String customerDashboard(Model model,Principal principal) {
        String username=principal.getName();
        Optional<Customer> optionalCustomer=customerService.findByUsername(username);
        if(optionalCustomer.isPresent()){
            List<Report> reports=optionalCustomer.get().getReports();
            int employeeCounter = employeeService.findAll().size();
            int deviceCounter = deviceService.findAll().size();
            model.addAttribute("reports",reports);
            model.addAttribute("reportCounter",reports.size());
            model.addAttribute("deviceCounter", deviceCounter);
            model.addAttribute("employeeCounter", employeeCounter);
        }
        return "customerDashboard";
    }

    @GetMapping("/masterDashboard")
    public String masterDashboard(Model model) {
        List<Report> reports = reportService.findUnassigned();
        int allReportsSize = reportService.findAll().size();
        int clientCounter = customerService.findAll().size();
        int employeeCounter = employeeService.findAll().size();
        model.addAttribute("reports", reports);
        model.addAttribute("reportCounter", allReportsSize);
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", employeeCounter);
        return "masterDashboard";
    }

    @GetMapping("/masterDashboard/allReports")
    public String masterDashboardAllReports(Model model) {
        List<Report> reports = reportService.findAll();
        int unnasignedReportsSize = reportService.findUnassigned().size();
        int clientCounter = customerService.findAll().size();
        int employeeCounter = employeeService.findAll().size();
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", employeeCounter);
        model.addAttribute("reports", reports);
        model.addAttribute("reportCounter", unnasignedReportsSize);
        return "allReportsMaster";
    }

}
