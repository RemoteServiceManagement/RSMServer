package com.rsm.controller;

import com.rsm.configuration.EmailSender;
import com.rsm.device.DeviceLogDataService;
import com.rsm.device.DeviceService;
import com.rsm.device.log.DeviceLogTableService;
import com.rsm.device.log.LogDeviceInfo;
import com.rsm.report.Report;
import com.rsm.report.ReportCopy;
import com.rsm.report.ReportCopyService;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportService;
import com.rsm.report.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Created by Dawid on 02.06.2018 at 18:40.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/diagnosis/")
public class DiagnosisController {
    private static final int PAGE_SIZE = 100;
    private final ReportService reportService;
    private final ReportCopyService reportCopyService;
    private final DeviceLogDataService deviceLogDataService;
    private final DeviceLogTableService logTableService;
    private final DeviceService deviceService;
    @Autowired
    private EmailSender emailSender;


    @GetMapping("{reportId}/details")
    public String getReportDetails(@PathVariable Long reportId, Model model) {
        Report report = getReport(reportId);
        model.addAttribute("report", report);
        model.addAttribute("deviceId", report.getDevice().getId());
        return "diagnosis/diagnosis-details";
    }

    private Report getReport(Long reportId) {
        return reportService.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
    }

    @GetMapping("{reportId}/device/data")
    public String showCar(@PathVariable Long reportId) {
        return "redirect:/diagnosis/" + reportId + "/device/data/1";
    }

    @GetMapping("{reportId}/device/data/{pageNumber}")
    public String showPage(@PathVariable Integer pageNumber, @PathVariable Long reportId, ModelMap model) {
        Report report = getReport(reportId);
        model.addAttribute("report", report);
        model.addAttribute("basicLogDevice", deviceLogDataService.getLogDeviceInfo(reportId));
        model.addAttribute("deviceId", report.getDevice().getId());
        model.addAttribute("deviceLog", logTableService.getDeviceLog(reportId, PAGE_SIZE, pageNumber - 1));
        return "diagnosis/diagnosis-data";
    }

    @PostMapping("{reportId}/device/data/update")
    public String updateDeviceLogs(@ModelAttribute LogDeviceInfo logDeviceInfo, @PathVariable Long reportId) {
        deviceLogDataService.updateData(logDeviceInfo, reportId);
        return  "redirect:/diagnosis/" + reportId + "/device/data";
    }


    @GetMapping("/{reportId}/edit")
    public String editReportDetails(@PathVariable("reportId") Long reportId, Model model) {
        Optional<Report> reportOptional = reportService.findById(reportId);
        if (reportOptional.isPresent()) {
            Report report = reportOptional.get();
            model.addAttribute("report", report);
            model.addAttribute("reportId", reportId);
            model.addAttribute("deviceId", report.getDevice().getId());
        }
        return "reportDetails";
    }

    @PostMapping("/{reportId}/edit")
    public String editReportDetailsPost(@ModelAttribute("report") Report report,
                                        @ModelAttribute("reportId") String reportId) {
        Optional<Report> savedReportOptional = reportService.findById(Long.valueOf(reportId));
        if(savedReportOptional.isPresent()) {
            Report savedReport = savedReportOptional.get();
            savedReport.setReportStatus(report.getReportStatus());
            savedReport.setPricing(report.getPricing());
            savedReport.setDiagnosis(report.getDiagnosis());
            if(report.getReportStatus().equals(ReportStatus.FINISHED)) {
                emailSender.sendEmailReportFinished(savedReport);
            }
            reportService.save(savedReport);
        }
        return "redirect:/diagnosis/{reportId}/details";
    }

    @GetMapping("{reportId}/customerDetails")
    public String getReportCustomerDetails(@PathVariable Long reportId, Model model) {
        Report report = getReport(reportId);
        report.setDiagnosis("");
        model.addAttribute("report", report);
        model.addAttribute("deviceId", report.getDevice().getId());
        model.addAttribute("customer", report.getCustomer());
        return "diagnosis/customer-details";
    }

    @PostMapping("{reportId}/mail")
    public String sendMailToCustomer(@ModelAttribute("report") Report report,
                                     @ModelAttribute("reportId") String reportId) {
        Optional<Report> savedReportOptional = reportService.findById(Long.valueOf(reportId));
        if(savedReportOptional.isPresent()) {
            emailSender.sendEmailUpdateReport(savedReportOptional.get(), report.getDiagnosis());

        }
        return "redirect:/diagnosis/{reportId}/customerDetails";
    }

    @GetMapping("{reportId}/edited")
    public String getReportEditedVersions(@PathVariable Long reportId, Model model) {
        Report report = getReport(reportId);
        model.addAttribute("report", report);
        model.addAttribute("reportCopies", report.getReportCopies());
        model.addAttribute("deviceId", report.getDevice().getId());
        return "diagnosis/report-copies-list";
    }

    @GetMapping("{reportId}/edited/{reportCopyId}")
    public String getReportEdited(@PathVariable Long reportId, @PathVariable Long reportCopyId, Model model) {
        Report report = getReport(reportId);
        Optional<ReportCopy> optionalReportCopy = reportCopyService.findById(reportCopyId);
        if(optionalReportCopy.isPresent()) {
            model.addAttribute("reportCopy", optionalReportCopy.get());
            model.addAttribute("deviceId", report.getDevice().getId());
        }
        return "diagnosis/report-copy";
    }
}
