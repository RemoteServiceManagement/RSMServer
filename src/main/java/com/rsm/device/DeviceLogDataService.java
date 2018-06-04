package com.rsm.device;

import com.rsm.device.log.LogDeviceInfo;
import com.rsm.device.log.remote.RemoteDeviceLogService;
import com.rsm.device.property.BasicPropertyDefinition;
import com.rsm.device.property.BasicPropertyService;
import com.rsm.device.property.PropertyDefinitionName;
import com.rsm.report.Report;
import com.rsm.report.ReportDoesNotExistException;
import com.rsm.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threeten.extra.Interval;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Date.from;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Created by Dawid on 03.06.2018 at 17:14.
 */

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceLogDataService {
    private final RemoteDeviceLogService remoteDeviceLogService;
    private final ReportRepository reportRepository;
    private final BasicPropertyService propertyService;

    public LogDeviceInfo getLogDeviceInfo(Long reportId) {
        Report report = getReport(reportId);
        return toLogDeviceInfo(report);
    }

    private Report getReport(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(ReportDoesNotExistException::new);
    }

    private LogDeviceInfo toLogDeviceInfo(Report report) {
        Interval availableInterval = getAvailableInterval(report);
        LogDeviceInfo logDeviceInfo = LogDeviceInfo.builder()
                .chosenDateFrom(getDate(report.getChosenDateFrom()))
                .chosenDateTo(getDate(report.getChosenDateTo()))
                .definitionNames(report.getChosenProperty().stream().map(this::toChosenProperty).collect(toList()))
                .availableDataFrom(from(availableInterval.getStart()))
                .availableDataTo(from(availableInterval.getEnd()))
                .build();

        logDeviceInfo.setDefinitionNames(mergeRemote(logDeviceInfo.getDefinitionNames(), report));
        return logDeviceInfo;
    }

    private java.util.Date getDate(Instant date) {
        return ofNullable(date).map(Date::from).orElse(null);
    }

    private List<PropertyDefinitionName> mergeRemote(List<PropertyDefinitionName> definitionNames, Report report) {
        List<BasicPropertyDefinition> devicePropertiesDefinition = remoteDeviceLogService
                .getDevicePropertiesDefinition(report.getReportedDeviceServiceCredential(),
                        report.getReportedDeviceExternalId());
        List<PropertyDefinitionName> remoteProperty = devicePropertiesDefinition.stream()
                .map(this::toNotChosenProperty).collect(toList());

        HashSet<PropertyDefinitionName> merged = new HashSet<>();
        merged.addAll(definitionNames);
        merged.addAll(remoteProperty);
        return new ArrayList<>(merged);
    }

    private PropertyDefinitionName toNotChosenProperty(BasicPropertyDefinition definition) {
        return new PropertyDefinitionName(definition.getName(), definition.getCode(), false);
    }


    private Interval getAvailableInterval(Report report) {
        return remoteDeviceLogService.getLogDeviceDateRange(report.getReportedDeviceServiceCredential(),
                report.getReportedDeviceExternalId());
    }

    private PropertyDefinitionName toChosenProperty(BasicPropertyDefinition definition) {
        return new PropertyDefinitionName(definition.getName(), definition.getCode(), true);
    }

    public void updateData(LogDeviceInfo logDeviceInfo, Long reportId) {
        Report report = getReport(reportId);
        propertyService.removeAll(report.getChosenProperty());

        Instant chosenDateFrom = logDeviceInfo.getChosenDateFrom().toInstant();
        report.setChosenDateFrom(chosenDateFrom);
        Instant chosenDateTo = logDeviceInfo.getChosenDateTo().toInstant();
        report.setChosenDateTo(chosenDateTo);
        report.setChosenProperty(toChosenProperty(logDeviceInfo.getDefinitionNames(), report));


        Interval interval = Interval.of(chosenDateFrom, chosenDateTo);
        List<String> chosenProperty = report.getChosenPropertyCode();

        reportRepository.save(report);
        remoteDeviceLogService.downloadDeviceLogs(report.getReportedDeviceServiceCredential(),
                report.getReportedDeviceExternalId(), interval, chosenProperty, reportId);
    }

    private List<BasicPropertyDefinition> toChosenProperty(List<PropertyDefinitionName> definitionNames, Report report) {
        return definitionNames
                .stream()
                .filter(PropertyDefinitionName::isChosen)
                .map(this::toBasicProperty)
                .peek(property -> property.setReport(report))
                .collect(toList());
    }

    private BasicPropertyDefinition toBasicProperty(PropertyDefinitionName property) {
        return new BasicPropertyDefinition(property.getName(), property.getCode());
    }
}
