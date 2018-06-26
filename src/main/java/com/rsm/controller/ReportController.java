package com.rsm.controller;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.Device;
import com.rsm.device.DeviceService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.report.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final CustomerService customerService;
    private final DeviceService deviceService;

    private static final String UPLOADED_FOLDER="src/main/resources/static/images/uploads/";

    @GetMapping("/reportForm")
    public String reportForm(Model model){
        Report report=new Report();
        report.setDevice(new Device());
        report.setReportStatus(ReportStatus.PENDING);
        report.setReportDate(LocalDate.now());
        List<Device> devices = customerService.getCurrent().map(Customer::getDevices).orElse(Collections.emptyList());
        model.addAttribute("report", report);
        model.addAttribute("devices", devices);
        return "addReportForm";
    }
    @PostMapping("/reportForm")
    public String reportFormPost(@ModelAttribute("report")Report report, BindingResult result, Model model,
                                 @RequestParam(name = "file",required = false) MultipartFile file,
                                 Principal principal){
        report.setDevice(deviceService.findById(report.getDevice().getId()).get());
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
        reportService.save(report);
        return "redirect:/dashboard/customerDashboard";
    }


}
