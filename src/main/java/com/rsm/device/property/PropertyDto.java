package com.rsm.device.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dawid on 04.05.2018 at 11:33.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDto implements Serializable {
    private String name;
    private String code;
    private String unit;
    private String stringValue;
    private Long longValue;
    private Double doubleValue;
    private ValueType valueType;

    public Serializable getValue() {
        switch (valueType) {
            case LONG:
                return longValue;
            case DOUBLE:
                return doubleValue;
            case STRING:
                return stringValue;
        }
        return null;
    }
}
