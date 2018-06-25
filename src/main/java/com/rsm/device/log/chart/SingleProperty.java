package com.rsm.device.log.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dawid on 24.06.2018 at 17:08.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleProperty {
    private Long date;
    private Serializable propertyValue;
}
