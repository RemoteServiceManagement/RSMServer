package com.rsm.device.log;

import com.rsm.device.property.BasicPropertyDefinitionNameDto;
import com.rsm.device.property.PropertyDefinitionName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Dawid on 03.06.2018 at 16:43.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDeviceInfo {
    private List<PropertyDefinitionName> definitionNames;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date chosenDateFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date chosenDateTo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date availableDataFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date availableDataTo;

    public List<BasicPropertyDefinitionNameDto> getDefinitionNamesDto() {
        return Optional.ofNullable(definitionNames)
                .map(that -> that.stream()
                        .map(PropertyDefinitionName::valueOf)
                        .collect(Collectors.toList())).orElse(newArrayList());
    }
}
