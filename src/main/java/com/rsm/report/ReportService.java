package com.rsm.report;


import java.util.List;
import java.util.Optional;

public interface ReportService {
    List<Report> findAll();
    Optional<Report> findById(Long id);
    void save(Report report);
    Report saveAndReturn(Report report);
    void delete(Report report);
    void deleteById(Long id);
    void attachReportToCustomer(Report report, String username);
    void attachReportToRandomEmployee(Report report);
    List<Report> findUnassignedAndNotFinished();

    List<Report> findByDeviceId(Long deviceId);

    Iterable<Report> findByQuery(SearchReportParam searchReportParam);
}
