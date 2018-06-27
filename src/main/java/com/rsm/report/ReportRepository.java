package com.rsm.report;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Report, Long>, QuerydslPredicateExecutor<Report> {
    List<Report> findAllByOrderByReportDateDesc();

    List<Report> findByDevice_IdOrderByReportDateDesc(Long deviceId);
}
