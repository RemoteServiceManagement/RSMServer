package com.rsm.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {
    PENDING("Oczekuje na akceptację"),
    ACCEPTED("W trakcie realizacji"),
    FINISHED("Ukończone");
    private final String reportStatusName;

}
