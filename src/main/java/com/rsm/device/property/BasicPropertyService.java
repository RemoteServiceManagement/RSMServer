package com.rsm.device.property;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dawid on 04.06.2018 at 00:50.
 */
@Service
@RequiredArgsConstructor
public class BasicPropertyService {
    private final BasicPropertyRepository repository;

    public void removeAll(List<BasicPropertyDefinition> definitions) {
        repository.deleteAll(definitions);
    }

    public BasicPropertyDefinition getByReportIdAndCode(Long reportId, String code) {
        return repository.getByReport_IdAndCode(reportId, code);
    }

    public List<BasicPropertyDefinition> getByReportId(Long reportId) {
        return repository.getByReport_IdAndNumericalIsTrue(reportId);
    }
}
