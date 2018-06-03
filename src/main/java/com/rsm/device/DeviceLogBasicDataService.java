package com.rsm.device;

import com.rsm.device.log.LogDeviceInfo;
import com.rsm.device.log.remote.RemoteDeviceLogService;
import com.rsm.property.BasicPropertyDefinition;
import com.rsm.property.PropertyDefinitionNameDto;
import com.rsm.report.Report;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threeten.extra.Interval;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Date.from;
import static java.util.Optional.ofNullable;

/**
 * Created by Dawid on 03.06.2018 at 17:14.
 */

@Service
@RequiredArgsConstructor
public class DeviceLogBasicDataService {
    private final RemoteDeviceLogService remoteDeviceLogService;
    private final ReportRepository reportRepository;

    public LogDeviceInfo getLogDeviceInfo(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
        return toLogDeviceInfo(report);
    }

    private LogDeviceInfo toLogDeviceInfo(Report report) {
        Interval availableInterval = getAvailableInterval(report);
        LogDeviceInfo logDeviceInfo = LogDeviceInfo.builder()
                .chosenDateFrom(getDate(report.getChosenDateFrom()))
                .chosenDateTo(getDate(report.getChosenDateTo()))
                .definitionNames(report.getChosenProperty().stream().map(this::toChosenProperty).collect(Collectors.toList()))
                .availableDataFrom(from(availableInterval.getStart()))
                .availableDataTo(from(availableInterval.getEnd()))
                .build();

        logDeviceInfo.setDefinitionNames(mergeRemote(logDeviceInfo.getDefinitionNames(), report));
        return logDeviceInfo;
    }

    private java.util.Date getDate(Instant date) {
        return ofNullable(date).map(Date::from).orElse(null);
    }

    private List<PropertyDefinitionNameDto> mergeRemote(List<PropertyDefinitionNameDto> definitionNames, Report report) {
        List<BasicPropertyDefinition> devicePropertiesDefinition = remoteDeviceLogService
                .getDevicePropertiesDefinition(report.getReportedDeviceServiceCredential(),
                        report.getReportedDeviceExternalId());
        List<PropertyDefinitionNameDto> remoteProperty = devicePropertiesDefinition.stream()
                .map(this::toNotChosenProperty).collect(Collectors.toList());

        HashSet<PropertyDefinitionNameDto> merged = new HashSet<>();
        merged.addAll(definitionNames);
        merged.addAll(remoteProperty);
        return new ArrayList<>(merged);
    }

    private PropertyDefinitionNameDto toNotChosenProperty(BasicPropertyDefinition definition) {
        return new PropertyDefinitionNameDto(definition.getName(), definition.getCode(), false);
    }


    private Interval getAvailableInterval(Report report) {
        return remoteDeviceLogService.getLogDeviceDateRange(report.getReportedDeviceServiceCredential(),
                report.getReportedDeviceExternalId());
    }

    private PropertyDefinitionNameDto toChosenProperty(BasicPropertyDefinition definition) {
        return new PropertyDefinitionNameDto(definition.getName(), definition.getCode(), true);
    }
}
