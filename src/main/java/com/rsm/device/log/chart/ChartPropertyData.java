package com.rsm.device.log.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 24.06.2018 at 17:16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartPropertyData {
    private String jsonData;
    private String chartName;
    private String unit;
    private String propertyCode;
}
