package com.rsm.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 27.06.2018 at 22:16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReportParam {
    private ReportStatus reportStatus;
    private String customerIdentify;
    private String employeeIdentify;
}
