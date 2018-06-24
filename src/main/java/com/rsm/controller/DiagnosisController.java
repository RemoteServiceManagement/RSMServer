package com.rsm.controller;

import com.rsm.device.DeviceLogDataService;
import com.rsm.device.log.DeviceLogTableService;
import com.rsm.device.log.LogDeviceInfo;
import com.rsm.report.Report;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportService;
import lombok.RequiredArgsConstructor;
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
    private final DeviceLogDataService deviceLogDataService;
    private final DeviceLogTableService logTableService;

    @GetMapping("{reportId}/details")
    public String getReportDetails(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", getReport(reportId));
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
        model.addAttribute("report", getReport(reportId));
        model.addAttribute("basicLogDevice", deviceLogDataService.getLogDeviceInfo(reportId));
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
        }
        return "reportDetails";
    }

    @PostMapping("/{reportId}/edit")
    public String editReportDetailsPost(@ModelAttribute("report") Report report,
                                        @ModelAttribute("reportId") Long reportId) {
        Optional<Report> savedReportOptional = reportService.findById(reportId);
        if(savedReportOptional.isPresent()) {
            Report savedReport = savedReportOptional.get();
            savedReport.setReportStatus(report.getReportStatus());
            savedReport.setPricing(report.getPricing());
            savedReport.setDiagnosis(report.getDiagnosis());
            reportService.save(savedReport);
        }
        return "redirect:/diagnosis/{reportId}/details";
    }
}
