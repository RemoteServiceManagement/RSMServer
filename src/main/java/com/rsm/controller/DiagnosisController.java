package com.rsm.controller;

import com.rsm.device.DeviceLogDataService;
import com.rsm.device.log.LogDeviceInfo;
import com.rsm.report.Report;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dawid on 02.06.2018 at 18:40.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/master/diagnosis/")
public class DiagnosisController {
    private final ReportService reportService;
    private final DeviceLogDataService deviceLogDataService;

    @GetMapping("{reportId}/details")
    public String getReportDetails(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", gerReport(reportId));
        return "diagnosis/diagnosis-details";
    }

    private Report gerReport(Long reportId) {
        return reportService.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
    }

    @GetMapping("{reportId}/device/data")
    public String getDeviceData(@PathVariable Long reportId, Model model) {
        model.addAttribute("report", gerReport(reportId));
        model.addAttribute("basicLogDevice", deviceLogDataService.getLogDeviceInfo(reportId));
        return "diagnosis/diagnosis-data";
    }

    @PostMapping("{reportId}/device/data/update")
    public String updateDeviceLogs(@ModelAttribute LogDeviceInfo logDeviceInfo, @PathVariable Long reportId) {
        deviceLogDataService.updateData(logDeviceInfo, reportId);
        return  "redirect:/master/diagnosis/" + reportId + "/device/data";
    }
}
