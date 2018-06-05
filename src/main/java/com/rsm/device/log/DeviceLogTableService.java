package com.rsm.device.log;

import com.google.common.collect.Lists;
import com.rsm.device.DeviceLogDataService;
import com.rsm.device.DeviceLogDto;
import com.rsm.device.property.BasicPropertyDefinitionNameDto;
import com.rsm.device.property.PropertyDto;
import com.rsm.pagination.PageInfo;
import com.rsm.pagination.PropertiesPage;
import com.rsm.pagination.PropertiesPageImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

/**
 * Created by Dawid on 04.06.2018 at 22:34.
 */
@Service
@RequiredArgsConstructor
public class DeviceLogTableService {
    public static final String DATE_NAME = "Data";
    public static final String DATE_CODE = "DATE";
    private final LogDeviceParameterRepository repository;
    private final DeviceLogDataService dataService;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault()).withZone(ZoneOffset.UTC);

    public PropertiesPage<Map<String, Serializable>, BasicPropertyDefinitionNameDto> getDeviceLog(Long reportId, int pageSize, int pageNumber) {
        DeviceLogDto deviceLog = repository.getDeviceLog(reportId.toString());
        return getLogs(deviceLog.getLogs(), pageNumber, pageSize)
                .map(pageInfo -> toPage(pageInfo, pageNumber, pageSize, reportId))
                .orElse(new PropertiesPageImpl<>(0, 0, 0, newArrayList(), newArrayList()));
    }

    private PropertiesPage<Map<String, Serializable>, BasicPropertyDefinitionNameDto>
    toPage(PageInfo<List<LogDto>> pageInfo, int pageNumber, int pageSize, Long reportId) {
        List<BasicPropertyDefinitionNameDto> definitionNamesDto = dataService.getDownloadedLogInfo(reportId);
        addDateHeader(definitionNamesDto);
        List<Map<String, Serializable>> logMaps = pageInfo.getContent()
                .stream()
                .map(this::toMap)
                .collect(toList());
        return new PropertiesPageImpl<>(pageInfo.getTotalPage(), pageNumber, pageSize, logMaps, definitionNamesDto);
    }

    private void addDateHeader(List<BasicPropertyDefinitionNameDto> definitionNamesDto) {
        if (!definitionNamesDto.isEmpty()) {
            definitionNamesDto.add(0, new BasicPropertyDefinitionNameDto(DATE_NAME, DATE_CODE, null));
        }
    }

    private Map<String, Serializable> toMap(LogDto logDto) {
        Map<String, Serializable> logs = logDto.getProperties()
                .stream()
                .collect(Collectors.toMap(PropertyDto::getCode, PropertyDto::getValue));
        logs.put(DATE_CODE, dateTimeFormatter.format(logDto.getDateTime()));
        return logs;
    }

    private Optional<PageInfo<List<LogDto>>> getLogs(List<LogDto> logs, int pageNumber, int pageSize) {
        List<LogDto> sortedLogs = logs.stream().sorted(comparing(LogDto::getDateTime)).collect(toList());
        List<List<LogDto>> partition = Lists.partition(sortedLogs, pageSize);

        if (pageNumber < partition.size() && pageNumber >= 0) {
            return of(new PageInfo<>(partition.get(pageNumber), partition.size()));
        }

        return empty();
    }
}
