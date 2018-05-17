package com.rsm.controller;

import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/master")
@RequiredArgsConstructor
public class MasterController {

    private final ReportService reportService;
    private final EmployeeService employeeService;

    @GetMapping("/assignReport/{reportId}")
    public String getReportToAssign(@PathVariable("reportId") Long reportId, Model model) {
        Optional<Report> reportOptional = this.reportService.findById(reportId);
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            model.addAttribute("report", report);
        }
        List<Employee> employees = this.employeeService.findAll();
        model.addAttribute("employees", employees);
        return "reportAssign";
    }

    @PostMapping("/assignReport")
    public String postAssignReport(@ModelAttribute("report") Report report,
                                   @ModelAttribute("reportId") Long reportId) {
        Optional<Report> savedReportOptional = reportService.findById(reportId);
        report.setTitle(savedReportOptional.get().getTitle());
        report.setDescription(savedReportOptional.get().getDescription());
        report.setReportDate(savedReportOptional.get().getReportDate());
        report.setReportPhoto(savedReportOptional.get().getReportPhoto());
        report.setDevice(savedReportOptional.get().getDevice());
        report.setReportStatus(savedReportOptional.get().getReportStatus());
        report.setCustomer(savedReportOptional.get().getCustomer());
        reportService.save(report);
        return "redirect:/dashboard/masterDashboard";
    }
}
