package com.rsm.device.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 04.06.2018 at 21:58.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "unit")
public class BasicPropertyDefinitionNameDto {
    private String name;
    private String code;
    private String unit;

    public static BasicPropertyDefinitionNameDto valueOf(PropertyDefinitionName definitionName) {
        return new BasicPropertyDefinitionNameDto(definitionName.getName(), definitionName.getCode(), definitionName.getUnit());
    }
}
