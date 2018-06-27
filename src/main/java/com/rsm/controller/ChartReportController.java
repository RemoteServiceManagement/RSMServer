package com.rsm.controller;

import com.rsm.common.BaseEntity;
import com.rsm.device.DeviceNotExistException;
import com.rsm.device.log.chart.DeviceChartService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dawid on 24.06.2018 at 15:13.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diagnosis/{reportId}/chart")
public class ChartReportController {
    private final DeviceChartService deviceChartService;
    private final ReportService reportService;

    @GetMapping()
    public String showChart(@PathVariable Long reportId, Model model) {
        model.addAttribute("reportId", reportId);
        model.addAttribute("deviceId", reportService.findById(reportId).map(Report::getDevice).map(BaseEntity::getId
        ).orElseThrow(DeviceNotExistException::new));
        model.addAttribute("chartPropertiesData", deviceChartService.getDevicePropertyChartData(reportId));
        return "diagnosis/diagnosis-charts";
    }
}
