package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.DeviceService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.report.ReportCopy;
import com.rsm.report.ReportCopyService;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportService;
import com.rsm.report.ReportStatus;
import com.rsm.report.SearchReportParam;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;


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
    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/uploads/";

    @GetMapping("")
    public String dashboard(Principal principal) {
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getRoles().contains(roleService.findRoleByRoleName(masterRole).orElse(null))) {
                return "redirect:/dashboard/masterDashboard/";
            } else if (user.getRoles().contains(roleService.findRoleByRoleName(employeeRole).orElse(null))) {
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
            List<Report> reports = ofNullable(optionalEmployee.get().getReports()).map(this::filterNotFinished).orElse(new ArrayList<>());
            int clientCounter = customerService.findAll().size();
            int employeeCounter = employeeService.findAll().size();
            model.addAttribute("clientCounter", clientCounter);
            model.addAttribute("employeeCounter", employeeCounter);
            model.addAttribute("reports", reports);
            model.addAttribute("reportCounter", reports.size());
        }
        return "employeeDashboard";
    }

    private List<Report> filterNotFinished(List<Report> reports) {
        return reports.stream().filter(report -> !report.getReportStatus().equals(ReportStatus.FINISHED)).collect(Collectors.toList());
    }

    @GetMapping("/customerDashboard")
    public String customerDashboard(Model model, Principal principal) {
        String username = principal.getName();
        Optional<Customer> optionalCustomer = customerService.findByUsername(username);
        if (optionalCustomer.isPresent()) {
            List<Report> reports = optionalCustomer.get().getReports();
            reports = ofNullable(reports).map(that -> that.stream()
                    .sorted(Comparator.comparing(Report::getReportDate).reversed())
                    .collect(Collectors.toList())).orElse(new ArrayList<>());
            int employeeCounter = employeeService.findAll().size();
            int deviceCounter = deviceService.findAll().size();
            model.addAttribute("reports", reports);
            model.addAttribute("reportCounter", reports.size());
            model.addAttribute("deviceCounter", deviceCounter);
            model.addAttribute("employeeCounter", employeeCounter);
        }
        return "customerDashboard";
    }

    @GetMapping("/customerDashboard/{reportId}/details")
    public String customerReportDetails(@PathVariable Long reportId, Model model, Principal principal) {
        Report report = reportService.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
        String username = principal.getName();
        Optional<Customer> optionalCustomer = customerService.findByUsername(username);
        if (optionalCustomer.isPresent()) {
            if (optionalCustomer.get().getReports().contains(report)) {
                model.addAttribute("report", report);
                model.addAttribute("reportCopies", report.getReportCopies());
            }
        }
        return "customerReportDetails";
    }

    @GetMapping("/masterDashboard")
    public String masterDashboard(Model model) {
        List<Report> reports = reportService.findUnassignedAndNotFinished();
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
        int unnasignedReportsSize = reportService.findUnassignedAndNotFinished().size();
        int clientCounter = customerService.findAll().size();
        int employeeCounter = employeeService.findAll().size();
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", employeeCounter);
        model.addAttribute("reports", reports);
        model.addAttribute("reportCounter", unnasignedReportsSize);
        model.addAttribute("queryParams", new SearchReportParam());
        model.addAttribute("statuses", getStatuses());
        return "allReportsMaster";
    }

    private List<ReportStatus> getStatuses() {
        return Arrays.stream(ReportStatus.values()).collect(Collectors.toList());
    }

    @PostMapping("/masterDashboard/allReports")
    public String masterDashboardAllReports(@ModelAttribute SearchReportParam searchReportParam, Model model) {
        Iterable<Report> reports = reportService.findByQuery(searchReportParam);
        int unnasignedReportsSize = reportService.findUnassignedAndNotFinished().size();
        int clientCounter = customerService.findAll().size();
        int employeeCounter = employeeService.findAll().size();
        model.addAttribute("clientCounter", clientCounter);
        model.addAttribute("employeeCounter", employeeCounter);
        model.addAttribute("reports", reports);
        model.addAttribute("reportCounter", unnasignedReportsSize);
        model.addAttribute("queryParams", searchReportParam);
        model.addAttribute("statuses", getStatuses());
        return "allReportsMaster";
    }

    @GetMapping("/customerDashboard/{reportId}/details/edit")
    public String editReport(@PathVariable Long reportId, Model model) {
        Optional<Report> optionalReport = reportService.findById(reportId);
        if (optionalReport.isPresent()) {
            ReportCopy reportCopy = new ReportCopy();
            reportCopy.setDescription(optionalReport.get().getDescription());
            model.addAttribute("report", reportCopy);
        }
        return "customerReportEdit";
    }

    @PostMapping("/customerDashboard/{reportId}/details/edit")
    public String editReportAddReportCopy(@PathVariable Long reportId, @ModelAttribute("report") ReportCopy reportCopy, BindingResult result, Model model,
                                          @RequestParam(name = "file", required = false) MultipartFile file,
                                          Principal principal) {
        Optional<Report> optionalReport = reportService.findById(reportId);
        if (optionalReport.isPresent()) {
            Report originalReport = optionalReport.get();
            if (file != null && !file.isEmpty()) {
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
