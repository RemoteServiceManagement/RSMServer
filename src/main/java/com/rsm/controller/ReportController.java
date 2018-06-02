package com.rsm.controller;

import com.rsm.device.Device;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.report.ReportStatus;
import lombok.RequiredArgsConstructor;
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


@RequiredArgsConstructor
@Controller
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private static final String UPLOADED_FOLDER="src/main/resources/static/images/uploads/";



    @GetMapping("/reportForm")
    public String reportForm(Model model){
        Report report=new Report();
        Device device=new Device();
        report.setDevice(device);
        report.setReportStatus(ReportStatus.PENDING);
        report.setReportDate(LocalDate.now());
        model.addAttribute("report",report);
        return "addReportForm";
    }
    @PostMapping("/reportForm")
    public String reportFormPost(@ModelAttribute("report")Report report, BindingResult result, Model model,
                                 @RequestParam(name = "file",required = false) MultipartFile file,
                                 Principal principal){
        if(file!=null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                report.setReportPhoto(bytes);
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reportService.attachReportToCustomer(report,principal.getName());
        //reportService.attachReportToRandomEmployee(report);
        reportService.save(report);
        return "redirect:/dashboard/customerDashboard";
    }


}
