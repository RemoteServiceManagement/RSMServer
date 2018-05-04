package com.rsm.device.log;

import com.rsm.device.DeviceLogDto;
import com.rsm.elesticsearch.ESFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Created by Dawid on 03.05.2018 at 20:48.
 */
@Repository
@RequiredArgsConstructor
public class LogDeviceParameterRepository {
    private final ESFacade esFacade;

    public void save(DeviceLogDto deviceLogDto) {
        esFacade.putIndex("device", "log", deviceLogDto, deviceLogDto.getExternalId());
    }
}
