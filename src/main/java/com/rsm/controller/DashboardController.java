package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.Device;
import com.rsm.device.DeviceService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.*;
import com.rsm.role.RoleService;
import com.rsm.user.User;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
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
    private final ReportCopyService reportCopyService;
    private static final String masterRole = "MASTER";
    private static final String employeeRole = "EMPLOYEE";
    private static final String customerRole = "CUSTOMER";
    private static final String UPLOADED_FOLDER="src/main/resources/static/images/uploads/";

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
            int clientCounter = customerService.findAll().size();
            int employeeCounter = employeeService.findAll().size();
            model.addAttribute("clientCounter", clientCounter);
            model.addAttribute("employeeCounter", employeeCounter);
            model.addAttribute("reports", reports);
            model.addAttribute("reportCounter", reports.size());
        }
        return "employeeDashboard";
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

    @GetMapping("/customerDashboard/{reportId}/details")
    public String customerReportDetails(@PathVariable Long reportId, Model model, Principal principal) {
        Report report = reportService.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
        String username=principal.getName();
        Optional<Customer> optionalCustomer=customerService.findByUsername(username);
        if(optionalCustomer.isPresent()) {
            if(optionalCustomer.get().getReports().contains(report)) {
                model.addAttribute("report", report);
            }
        }
        return "customerReportDetails";
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

    @GetMapping("/customerDashboard/{reportId}/details/edit")
    public String editReport(@PathVariable Long reportId, Model model){
        Optional<Report> optionalReport=reportService.findById(reportId);
        if(optionalReport.isPresent()) {
            ReportCopy reportCopy = new ReportCopy();
            reportCopy.setDescription(optionalReport.get().getDescription());
            model.addAttribute("report", reportCopy);
        }
        return "customerReportEdit";
    }

    @PostMapping("/customerDashboard/{reportId}/details/edit")
    public String editReportAddReportCopy(@PathVariable Long reportId, @ModelAttribute("report")ReportCopy reportCopy, BindingResult result, Model model,
                                          @RequestParam(name = "file",required = false) MultipartFile file,
                                          Principal principal){
        Optional<Report> optionalReport=reportService.findById(reportId);
        if(optionalReport.isPresent()) {
            Report originalReport = optionalReport.get();
            if(file!=null && !file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    reportCopy.setReportPhoto(bytes);
                    Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                    Files.write(path, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reportCopy.setReport(originalReport);
            reportCopy.setReportDate(LocalDate.now());
            reportCopyService.save(reportCopy);
        }
        return "redirect:/dashboard/customerDashboard/{reportId}/details/";
    }

}
