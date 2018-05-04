package com.rsm.device.log;

import com.rsm.device.property.PropertyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * Created by Dawid on 04.05.2018 at 11:31.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDto implements Serializable {
    private Instant dateTime;
    private List<PropertyDto> properties;
}
