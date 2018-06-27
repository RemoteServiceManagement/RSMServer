package com.rsm.controller;

import com.rsm.device.Device;
import com.rsm.device.DeviceNotExistException;
import com.rsm.device.DeviceService;
import com.rsm.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dawid on 27.06.2018 at 16:19.
 */

@Controller
@RequestMapping("/report/{reportId}/device/")
@RequiredArgsConstructor
public class ReportDeviceController {
    private final ReportService reportService;
    private final DeviceService deviceService;

    @GetMapping("{deviceId}")
    public String showReports(@PathVariable Long deviceId, Model model, @PathVariable Long reportId) {
        Device device = deviceService.findById(deviceId).orElseThrow(DeviceNotExistException::new);
        model.addAttribute("reports", reportService.findByDeviceId(deviceId));
        model.addAttribute("device", device);
        model.addAttribute("reportId", reportId);
        return "device/device-report";
    }
}
