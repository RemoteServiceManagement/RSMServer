package com.rsm.device.log.remote;

import com.rsm.device.log.LogDto;
import com.rsm.device.property.PropertyDto;
import com.rsm.device.property.ValueType;
import com.service.remote.grpc.Log;
import com.service.remote.grpc.Property;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Created by Dawid on 04.05.2018 at 00:00.
 */
public class LogDeviceMapper {
    private List<Log> logs;

    public LogDeviceMapper(List<Log> logs) {
        this.logs = logs;
    }

    public static List<LogDto> map(List<Log> logs) {
        return new LogDeviceMapper(logs).map();
    }

    private List<LogDto> map() {
        return ofNullable(logs)
                .map(that -> that.stream()
                        .map(this::map)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    private LogDto map(Log log) {
        List<PropertyDto> propertyDtos = ofNullable(log.getPropertiesList())
                .map(this::mapProperties)
                .orElse(new ArrayList<>());
        return new LogDto(Instant.ofEpochSecond(log.getDateTime()), propertyDtos);
    }

    private List<PropertyDto> mapProperties(List<Property> properties) {
        return properties
                .stream()
                .map(LogDeviceMapper::toPropertyDto)
                .collect(Collectors.toList());
    }

    private static PropertyDto toPropertyDto(Property property) {
        PropertyDto propertyDto = PropertyDto.builder()
                .code(property.getCode())
                .name(property.getName())
                .unit(property.getUnit())
                .valueType(ValueType.valueOf(property.getValueType().name()))
                .build();

        switch (propertyDto.getValueType()) {
            case STRING:
                propertyDto.setStringValue(property.getStringValue());
                break;
            case DOUBLE:
                propertyDto.setDoubleValue(property.getDoubleValue());
                break;
            case LONG:
                propertyDto.setLongValue(property.getLongValue());
                break;
        }

        return propertyDto;

    }
}
