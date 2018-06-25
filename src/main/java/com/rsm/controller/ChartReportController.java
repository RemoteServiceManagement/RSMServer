package com.rsm.controller;

import com.rsm.device.log.chart.DeviceChartService;
import com.rsm.device.property.BasicPropertyDefinition;
import com.rsm.device.property.BasicPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Dawid on 24.06.2018 at 15:13.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/diagnosis/{reportId}/chart")
public class ChartReportController {
    private final DeviceChartService deviceChartService;
    private final BasicPropertyService basicPropertyService;

    @GetMapping
    public String showChart(@PathVariable Long reportId, Model model) {
        List<BasicPropertyDefinition> basicPropertyDefinitions = basicPropertyService.getByReportId(reportId);
        return showChart(reportId, basicPropertyDefinitions.stream().findFirst().map(BasicPropertyDefinition::getCode).orElse(""), model);
    }

    @GetMapping("/{propertyCode}")
    public String showChart(@PathVariable Long reportId, @PathVariable String propertyCode, Model model) {
        List<BasicPropertyDefinition> basicPropertyDefinitions = basicPropertyService.getByReportId(reportId);
        model.addAttribute("logDeviceParameters", basicPropertyDefinitions);
        model.addAttribute("reportId", reportId);
        model.addAttribute("chartPropertyData", deviceChartService.getDevicePropertyChartData(reportId, propertyCode));
        return "diagnosis/diagnosis-charts";
    }
}
