package com.rsm.device.log.chart;

import com.google.gson.Gson;
import com.rsm.device.DeviceLogDto;
import com.rsm.device.log.LogDeviceParameterRepository;
import com.rsm.device.log.LogDto;
import com.rsm.device.property.BasicPropertyDefinition;
import com.rsm.device.property.BasicPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Created by Dawid on 24.06.2018 at 16:02.
 */
@Service
@RequiredArgsConstructor
public class DeviceChartService {
    private final LogDeviceParameterRepository repository;
    private final BasicPropertyService basicPropertyService;

    public List<ChartPropertyData> getDevicePropertyChartData(Long reportId) {
        DeviceLogDto deviceLog = repository.getDeviceLog(reportId.toString());
        List<BasicPropertyDefinition> propertyDefinitions = getDevicesProperties(reportId);

        return propertyDefinitions.stream().map(property -> toChartData(deviceLog.getLogs(), property)).collect(toList());
    }

    private ChartPropertyData toChartData(List<LogDto> logs, BasicPropertyDefinition property) {
        String chartData = new Gson().toJson(getChartData(logs, property.getCode()));

        return new ChartPropertyData(chartData, property.getName(),
                property.getUnit(), property.getCode());
    }

    private List<BasicPropertyDefinition> getDevicesProperties(Long reportId) {
        return Optional.ofNullable(basicPropertyService.getByReportId(reportId))
                .orElse(new ArrayList<>());
    }

    private List<SingleProperty> getChartData(List<LogDto> logs, String propertyCode) {
        return logs.stream()
                .map(log -> getProperty(propertyCode, log))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(SingleProperty::getDate))
                .collect(toList());
    }

    private List<SingleProperty> getProperty(String propertyCode, LogDto log) {
        return log.getProperties()
                .stream()
                .filter(property -> property.getCode().equals(propertyCode))
                .map(property -> new SingleProperty(log.getDateTime().toEpochMilli(), property.getValue()))
                .collect(toList());
    }
}
