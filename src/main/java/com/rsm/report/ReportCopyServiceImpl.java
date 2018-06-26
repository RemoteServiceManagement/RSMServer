package com.rsm.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportCopyServiceImpl implements ReportCopyService {
    private final ReportCopyRepository reportCopyRepository;

    @Override
    public void save(ReportCopy reportCopy) {
        reportCopyRepository.save(reportCopy);
    }

    @Override
    public Optional<ReportCopy> findById(Long id) {
        return reportCopyRepository.findById(id);
    }
}
