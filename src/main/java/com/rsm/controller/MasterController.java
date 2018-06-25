package com.rsm.controller;

import com.rsm.configuration.EmailSender;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private EmailSender emailSender;


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

    @PostMapping("/assignReport/{reportId}")
    public String postAssignReport(@ModelAttribute("report") Report report,
                                   @ModelAttribute("reportId") String reportId) {
        Optional<Report> savedReportOptional = reportService.findById(Long.valueOf(reportId));
        if(savedReportOptional.isPresent()) {
            Report savedReport = savedReportOptional.get();
            savedReport.setEmployee(report.getEmployee());
            reportService.save(savedReport);
            emailSender.sendEmailReportAssigned(savedReport);
        }
        return "redirect:/dashboard/masterDashboard";
    }
}
