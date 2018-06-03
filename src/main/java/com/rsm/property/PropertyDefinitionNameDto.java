package com.rsm.property;

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
@EqualsAndHashCode(exclude = "isChosen")
public class PropertyDefinitionNameDto {
    private String name;
    private String code;
    private boolean isChosen;


}
