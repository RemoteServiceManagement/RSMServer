package com.rsm.report;

import java.util.Optional;

public interface ReportCopyService {

    void save(ReportCopy reportCopy);
    Optional<ReportCopy> findById(Long id);

}
