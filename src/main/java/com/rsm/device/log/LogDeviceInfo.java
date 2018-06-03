package com.rsm.device.log;

import com.rsm.property.PropertyDefinitionNameDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by Dawid on 03.06.2018 at 16:43.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDeviceInfo {
    private List<PropertyDefinitionNameDto> definitionNames;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date chosenDateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date chosenDateTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date availableDataFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date availableDataTo;
}
