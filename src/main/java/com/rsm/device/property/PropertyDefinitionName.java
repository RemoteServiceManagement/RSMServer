package com.rsm.device.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 03.06.2018 at 16:35.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "isChosen", callSuper = true)
public class PropertyDefinitionName extends BasicPropertyDefinitionNameDto {
    private boolean isChosen;

    public PropertyDefinitionName(String name, String code, boolean isChosen) {
        super(name, code);
        this.isChosen = isChosen;
    }
}
