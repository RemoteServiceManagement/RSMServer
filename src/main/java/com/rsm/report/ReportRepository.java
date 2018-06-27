package com.rsm.report;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long> {
    List<Report> findAllByOrderByReportDateDesc();

    List<Report> findByDevice_IdOrderByReportDateDesc(Long deviceId);

    List<Report> findByQuery();
}
