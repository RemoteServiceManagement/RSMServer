package com.rsm.device;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    List<Device> findAll();
    Optional<Device> findById(Long id);
    void save(Device device);
    Device saveAndReturn(Device device);
    void delete(Device device);
    void deleteById(Long id);
}
