package com.rsm.device.property;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Dawid on 04.06.2018 at 00:49.
 */
@Service
public interface BasicPropertyRepository extends CrudRepository<BasicPropertyDefinition, Long> {
}
