package com.rsm.device;

import com.rsm.device.log.LogDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dawid on 04.05.2018 at 11:27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLogDto implements Serializable {
    private String externalId;
    private List<LogDto> logs;
}
