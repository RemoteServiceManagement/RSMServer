package com.rsm.device.log.remote;

import com.rsm.device.DeviceLogDto;
import com.rsm.device.log.LogDeviceParameterRepository;
import com.rsm.device.log.remote.connection.RemoteServiceCredential;
import com.rsm.device.log.remote.connection.RemoteServiceFactory;
import com.rsm.device.property.BasicPropertyDefinition;
import com.service.remote.grpc.DateRange;
import com.service.remote.grpc.DeviceBasicQuery;
import com.service.remote.grpc.Log;
import com.service.remote.grpc.LogBundle;
import com.service.remote.grpc.LogDeviceQuery;
import com.service.remote.grpc.LogDeviceServiceGrpc;
import com.service.remote.grpc.PropertyDefinitionBundle;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threeten.extra.Interval;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;
import static org.threeten.extra.Interval.of;

/**
 * Created by Dawid on 03.05.2018 at 14:45.
 */

@Service
@RequiredArgsConstructor
public class RemoteDeviceLogService {
    private final RemoteServiceFactory connectionFactory;
    private final LogDeviceParameterRepository repository;

    public Interval getLogDeviceDateRange(RemoteServiceCredential serviceCredential, String deviceExternalId) {
        ManagedChannel channel = connectionFactory.create(serviceCredential);
        LogDeviceServiceGrpc.LogDeviceServiceBlockingStub service = LogDeviceServiceGrpc.newBlockingStub(channel);
        DateRange dateRange = service.getDateRangeByDeviceExternalId(DeviceBasicQuery.newBuilder()
                .setDeviceExternalId(deviceExternalId)
                .build());
        channel.shutdown();
        return of(getDateTime(dateRange.getFrom()), getDateTime(dateRange.getTo()));
    }

    public void downloadDeviceLogs(RemoteServiceCredential serviceCredential, String deviceExternalId, Interval interval,
                                   List<String> propertyCodes, Long reportId) {
        ManagedChannel channel = connectionFactory.create(serviceCredential);
        LogDeviceServiceGrpc.LogDeviceServiceBlockingStub service = LogDeviceServiceGrpc.newBlockingStub(channel);
        Iterator<LogBundle> logs = service.getLogs(createGetLogsQuery(deviceExternalId, interval, propertyCodes));
        Iterable<LogBundle> logBundles = () -> logs;

        if (logs.hasNext()) {
            List<Log> logBundle = mapLogs(logBundles);
            repository.save(new DeviceLogDto(deviceExternalId, LogDeviceMapper.map(logBundle)), reportId);

        }
        channel.shutdown();
    }

    public boolean deviceExist(RemoteServiceCredential serviceCredential, String deviceExternalId) {
        ManagedChannel channel = connectionFactory.create(serviceCredential);
        LogDeviceServiceGrpc.LogDeviceServiceBlockingStub service = LogDeviceServiceGrpc.newBlockingStub(channel);
        return service.deviceExist(DeviceBasicQuery.newBuilder().setDeviceExternalId(deviceExternalId).build()).getExist();
    }

    private LogDeviceQuery createGetLogsQuery(String deviceExternalId, Interval interval, List<String> propertyCodes) {
        return LogDeviceQuery.newBuilder()
                .setDateRange(DateRange.newBuilder()
                        .setFrom(interval.getStart().getEpochSecond())
                        .setTo(interval.getEnd().getEpochSecond())
                        .build())
                .setDeviceExternalId(deviceExternalId)
                .addAllPropertiesCodes(propertyCodes)
                .build();
    }

    private List<Log> mapLogs(Iterable<LogBundle> logBundles) {
        return StreamSupport.stream(logBundles.spliterator(), false)
                .map(LogBundle::getLogsList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<BasicPropertyDefinition> getDevicePropertiesDefinition(RemoteServiceCredential serviceCredential,
                                                                       String deviceExternalId) {
        ManagedChannel channel = connectionFactory.create(serviceCredential);
        LogDeviceServiceGrpc.LogDeviceServiceBlockingStub service = LogDeviceServiceGrpc.newBlockingStub(channel);
        PropertyDefinitionBundle propertyDefinitionBundle = service.getDevicePropertyNames(DeviceBasicQuery.newBuilder()
                .setDeviceExternalId(deviceExternalId)
                .build());
        channel.shutdown();
        return ofNullable(propertyDefinitionBundle.getPropertyDefinitionList())
                .map(that -> that.stream()
                        .map(property -> new BasicPropertyDefinition(property.getName(), property.getCode()))
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());

    }

    private Instant getDateTime(long dataTimeEpochSeconds) {
        return Instant.ofEpochSecond(dataTimeEpochSeconds);
    }
}
