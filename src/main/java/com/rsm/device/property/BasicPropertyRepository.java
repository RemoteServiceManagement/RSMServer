package com.rsm.device.property;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dawid on 04.06.2018 at 00:49.
 */
@Service
public interface BasicPropertyRepository extends CrudRepository<BasicPropertyDefinition, Long> {
    BasicPropertyDefinition getByReport_IdAndCode(Long reportId, String code);

    List<BasicPropertyDefinition> getByReport_IdAndNumericalIsTrue(Long reportId);
}
