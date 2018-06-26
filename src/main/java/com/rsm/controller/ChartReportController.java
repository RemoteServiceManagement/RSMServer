package com.rsm.controller;

import com.rsm.device.log.chart.DeviceChartService;
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

    @GetMapping()
    public String showChart(@PathVariable Long reportId, Model model) {
        model.addAttribute("reportId", reportId);
        model.addAttribute("chartPropertiesData", deviceChartService.getDevicePropertyChartData(reportId));
        return "diagnosis/diagnosis-charts";
    }
}
